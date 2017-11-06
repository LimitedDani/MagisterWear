package nl.daniquedejong.magisterwear.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import nl.daniquedejong.magisterwear.MainActivity;
import nl.daniquedejong.magisterwear.R;
import nl.daniquedejong.magisterwear.fragment.setup.LoginSchoolFragment;
import nl.daniquedejong.magisterwear.fragment.setup.SearchSchoolFragment;
import nl.daniquedejong.magisterwear.fragment.setup.SelectSchoolFragment;
import nl.daniquedejong.magisterwear.utils.DateUtils;

public class SetupActivity extends FragmentActivity implements SearchSchoolFragment.OnSearchButtonClick,
                                                                SelectSchoolFragment.OnSchoolItemClick,
                                                                LoginSchoolFragment.OnLoginButtonClick {

    public EditText schoolname;
    public Button search;
    private FragmentManager fm;
    private SetupActivity sa;
    private Context mcontext;
    private School school;
    public RelativeLayout load;
    public TextView login_status;
    Profile mProfile = new Profile();
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mcontext = this;
        sa = (SetupActivity) mcontext;
        fm = sa.getSupportFragmentManager();

        load = findViewById(R.id.loading);
        login_status = findViewById(R.id.login_status);
        loading(false);

        loadSearchSchoolFragment();
    }
    public void onSearchButtonClick(String schoolname) {
        loadSelectSchoolFragment(schoolname);
    }
    public void onSchoolItemClick(School school) {
        this.school = school;
        Log.d("School", school.name);
        loadLoginFragment();
    }
    public void onLoginButtonClick(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loading(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login_status.setText(getResources().getString(R.string.status_logging_in));
                    }
                });
                Magister magister = null;
                try {
                    magister = Magister.login(school, username, password);
                } catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.msg_error));
                            loading(false);
                            CharSequence text = "Combinatie niet correct";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(mcontext, text, duration);
                            toast.show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.msg_error));
                            loading(false);
                            CharSequence text = "Combinatie niet correct";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(mcontext, text, duration);
                            toast.show();
                        }
                    });
                } catch (final InvalidParameterException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.msg_error));
                            loading(false);
                            CharSequence text = "Combinatie niet correct";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(mcontext, text, duration);
                            toast.show();
                        }
                    });
                }
                if (magister != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_status.setText(getResources().getString(R.string.status_logged_in));
                        }
                    });

                    mProfile = magister.profile;
                    Log.d("Study", magister.currentStudy.toString());
                    Log.d("mProfile", mProfile.toString());
                    if (mProfile.nickname != null && mProfile.nickname != "null") {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                login_status.setText(getResources().getString(R.string.status_getting_data));
                            }
                        });

                        mUser = new User(username, password, true);
                        String Account = new Gson().toJson(mProfile, Profile.class);
                        String School = new Gson().toJson(school, School.class);

                        String User = new Gson().toJson(mUser, net.ilexiconn.magister.container.User.class);

                        String format = "yyyy-MM-dd-HH:mm:ss";
                        Date today = Calendar.getInstance().getTime();

                        SimpleDateFormat formatter = new SimpleDateFormat(format);


                        SharedPreferences.Editor editor = mcontext.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                        editor.putString("Profile", Account);
                        editor.putString("User", User);
                        editor.putString("School", School);
                        editor.putBoolean("LoggedIn", true);
                        editor.putString("LastLogin", formatter.format(today));
                        editor.putInt("DataVersion", 3);
                        editor.apply();

                        LaunchActivity.la.finish();
                        Intent i = new Intent(mcontext, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Log.d("Error", "Uknown error");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                login_status.setText(getResources().getString(R.string.msg_error));
                                loading(false);
                            }
                        });
                    }
                }
            }
        }).start();
    }
    private void loadSearchSchoolFragment() {
        Bundle data = new Bundle();
        Fragment fragment = new SearchSchoolFragment();
        fragment.setArguments(data);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment, "SearchSchool");
        ft.commit();
    }
    private void loadSelectSchoolFragment(String schoolname){
        Bundle data = new Bundle();
        data.putString("school", schoolname);
        Fragment fragment = new SelectSchoolFragment();
        fragment.setArguments(data);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment, "SelectSchool");
        ft.commit();
    }
    private void loadLoginFragment(){
        Bundle data = new Bundle();
        Fragment fragment = new LoginSchoolFragment();
        fragment.setArguments(data);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment, "LoginSchool");
        ft.commit();
    }
    public void loading(final boolean value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                load.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}