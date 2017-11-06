package nl.daniquedejong.magisterwear;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wear.widget.drawer.WearableDrawerLayout;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.daniquedejong.magisterwear.activity.LaunchActivity;
import nl.daniquedejong.magisterwear.fragment.magister.CalendarFragment;
import nl.daniquedejong.magisterwear.fragment.magister.HomeFragment;
import nl.daniquedejong.magisterwear.utils.DateUtils;

public class MainActivity extends FragmentActivity implements HomeFragment.OnHomeFragmentReady{
    private WearableDrawerLayout mWearableDrawerLayout;
    private WearableNavigationDrawerView mWearableNavigationDrawer;
    private WearableActionDrawerView mWearableActionDrawer;
    private static final int DRAWER_SIZE = 2;

    private final WearableNavigationDrawerView.WearableNavigationDrawerAdapter mDrawerAdapter =
            new WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
    @Override
    public String getItemText(int pos) {
            return Integer.toString(pos);
            }

    @Override
    public Drawable getItemDrawable(int pos) {
            return getDrawable(android.R.drawable.star_on);
            }

    @Override
    public int getCount() {
            return DRAWER_SIZE;
            }
            };
    private FragmentManager fm;
    private MainActivity sa;
    public static Context mcontext;
            Magister magister;
    private User mUser;
    private School mSchool;
    private Fragment fragment;
            Profile mProfile;
    private boolean mError = false;

    public String currentLesson = "";
    public String currentLessonLocation = "";
    public String currentLessonTime = "";

    public static Date date;
    public static Appointment[] mAppointments;

    public RelativeLayout load;
    public String currentPage = "Home";
    public TextView login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (!getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoggedIn", false)) {
            Intent setupIntent = new Intent(MainActivity.this, LaunchActivity.class);
            finish();
            startActivity(setupIntent);
            return;
        }

        setContentView(R.layout.activity_main);
        mcontext = this;
        sa = (MainActivity) mcontext;
        fm = sa.getSupportFragmentManager();
        date = DateUtils.getToday();
        load = findViewById(R.id.loading);
        login_status = findViewById(R.id.login_status);

        mWearableActionDrawer = (WearableActionDrawerView) findViewById(R.id.bottom_action_drawer);
        mWearableActionDrawer.getController().peekDrawer();
        mWearableActionDrawer.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().toString().equals("Home")) {
                    if (currentPage.equals("Home")) {
                        mWearableActionDrawer.getController().peekDrawer();
                        return false;
                    }
                    currentPage = "Home";
                    loadHomeFragment();
                    mWearableActionDrawer.getController().peekDrawer();
                    return true;
                }
                if(menuItem.getTitle().toString().equals("Agenda")) {
                    if (currentPage.equals("Agenda")) {
                        mWearableActionDrawer.getController().peekDrawer();
                        return false;
                    }
                    currentPage = "Agenda";
                    loadCalendarFragment();
                    mWearableActionDrawer.getController().peekDrawer();
                    return true;
                }
                return false;
            }
        });
        loading(true);
        String account = getSharedPreferences("data", MODE_PRIVATE).getString("Profile", null);
        if (account == null) {
            mError = true;
        } else {
            mProfile = new Gson().fromJson(account, Profile.class);
            mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
            mSchool = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("School", null), School.class);
        }
        login();
    }
    private void loadHomeFragment(){
        Bundle data = new Bundle();
        fragment = new HomeFragment();
        fragment.setArguments(data);

        data.putString("name", mProfile.officialFirstNames);
        data.putString("currentlesson", currentLesson);
        data.putString("currentlessonlocation", currentLessonLocation);
        data.putString("currentlessontime", currentLessonTime);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment, "Home");
        ft.commit();
    }
    private void loadCalendarFragment() {
        Bundle data = new Bundle();
        fragment = new CalendarFragment();
        fragment.setArguments(data);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment, "Calendar");
        ft.commit();
    }
    public void onHomeFragmentReady() {
        loading(false);
    }
    private void login() {
        login_status.setText(getResources().getString(R.string.status_logging_in));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    magister = Magister.login(mSchool, mUser.username, mUser.password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.status_logged_in));
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.status_error_while_logging_in));
                        }
                    });
                } catch (ParseException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.status_error_while_logging_in));
                        }
                    });
                    e.printStackTrace();
                }
                if (magister != null) {
                    mProfile = magister.profile;
                    Log.d("Study", magister.currentStudy.toString());
                    Log.d("mProfile", mProfile.toString());
                    if (mProfile.nickname != null && mProfile.nickname != "null") {
                        mUser = new User(mUser.username, mUser.password, true);
                        getAppointments();
                    } else {
                        Log.d("Error", "Uknown error");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                login_status.setText(getResources().getString(R.string.msg_error));
                            }
                        });
                    }
                }
            }
        }).start();

    }

    public void getAppointments() {
        if (magister != null && !magister.isExpired()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login_status.setText(getResources().getString(R.string.status_getting_data));
                }
            });
            Date from = DateUtils.addDays(date, -7);
            Date until = DateUtils.addDays(date, 14);
            new MainActivity.AppointmentsTask(this, magister, from, until, 3).execute();

        } else {
            Log.d("Error", "getAppointments");
        }
    }

    public void setCurrentLesson() {
        for(Appointment appointment : mAppointments) {
            if(date.getDate() == appointment.startDate.getDate()) {

                String startLesson = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(appointment.startDate);
                String stopLesson = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(appointment.endDate);
                String now = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);

                boolean getALesson = false;
                try {
                    getALesson = isTimeBetweenTwoTime(startLesson, stopLesson, now);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(getALesson) {
                    currentLesson = appointment.description;
                    currentLessonLocation = appointment.location;
                    currentLessonTime = appointment.startDate.getHours() + ":" +
                            appointment.startDate.getMinutes() + " - " +
                            appointment.endDate.getHours() + ":" +
                            appointment.endDate.getMinutes();
                }
            }
        }
        loadHomeFragment();
    }


    class AppointmentsTask extends AsyncTask<Void, Void, Appointment[]> {
        private static final String TAG = "AppointmentsTask";

        public MainActivity activity;
        public Magister magister;
        public Date date1;
        public Date date2;
        public Integer whatToSave;

        public String error;


        public AppointmentsTask(MainActivity activity, Magister magister, Date date1, Date date2, Integer whatToSave) {
            this.activity = activity;
            this.magister = magister;
            this.date1 = date1;
            this.date2 = date2;
            this.whatToSave = whatToSave;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Appointment[] doInBackground(Void... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login_status.setText(getResources().getString(R.string.status_almost_done));
                }
            });
            try {
                AppointmentHandler appointmentHandler = new AppointmentHandler(magister);
                Appointment[] appointments = appointmentHandler.getAppointments(date1, date2);

                /*CalendarDB db = new CalendarDB(activity);
                db.addItems(appointments, activity);
                appointments = db.getAppointmentsByDate(activity.date);*/

                Log.d(TAG, "doInBackground: " + appointments.length);
                return appointments;
            } catch (IOException e) {
                Log.e(TAG, "Unable to retrieve data", e);
                return null;
            } catch (InvalidParameterException e) {
                Log.e(TAG, "Invalid Parameters", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Appointment[] appointments) {
            activity.mAppointments = appointments;
            setCurrentLesson();
        }
    }
    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)) {
            boolean valid = false;
            //Start Time
            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            java.util.Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                    && actualTime.before(calendar2.getTime())) {
                valid = true;
            }
            return valid;
        } else {
            throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
        }

    }
    public void loading(boolean value) {

        mWearableActionDrawer.setEnabled(!value);
        mWearableActionDrawer.setClickable(!value);
        mWearableActionDrawer.setIsLocked(value);
        mWearableActionDrawer.setVisibility(value ? View.INVISIBLE : View.VISIBLE);
        mWearableActionDrawer.setBackgroundColor(value ? getResources().getColor(R.color.nav_on_loading) : getResources().getColor(R.color.colorPrimaryDark));
        load.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
    }
}
