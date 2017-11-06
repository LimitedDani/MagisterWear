package nl.daniquedejong.magisterwear.fragment.magister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.School;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.daniquedejong.magisterwear.MainActivity;
import nl.daniquedejong.magisterwear.R;
import nl.daniquedejong.magisterwear.adapter.CalendarAdapter;
import nl.daniquedejong.magisterwear.adapter.CalendarRecAdapter;
import nl.daniquedejong.magisterwear.adapter.Controller;
import nl.daniquedejong.magisterwear.callback.ScalingScrollLayoutCallback;

public class CalendarFragment extends Fragment{
    OnSchoolItemClick mCallback;
    public Context context;
    public WearableRecyclerView mWearableRecyclerView;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayList;
    public School[] mSchools;
    public ArrayList<School> SchoolObjects = new ArrayList<School>();

    public FrameLayout mMainFrameLayout;
    public CalendarRecAdapter mCustomRecyclerAdapter;
    public TextView text_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mWearableRecyclerView = (WearableRecyclerView) view.findViewById(R.id.list_calendar);
        mMainFrameLayout = (FrameLayout) view.findViewById(R.id.app_title);
        text_date = (TextView) view.findViewById(R.id.text_date);
        //Context
        context = container.getContext();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {


            ArrayList<Appointment> dataModels = new ArrayList<>();
            for(Appointment appointment : MainActivity.mAppointments) {
                if(MainActivity.date.getDate() == appointment.startDate.getDate()) {
                    dataModels.add(appointment);
                }
            }
            String day;
            String month;
            switch (MainActivity.date.getDay()) {
                case (0):
                    day = "Zondag";
                    break;
                case (1):
                    day = "Maandag";
                    break;
                case (2):
                    day = "Dinsdag";
                    break;
                case (3):
                    day = "Woensdag";
                    break;
                case (4):
                    day = "Donderdag";
                    break;
                case (5):
                    day = "Vrijdag";
                    break;
                case (6):
                    day = "Zaterdag";
                    break;
                case (7):
                    day = "Zondag";
                    break;
                default:
                    day = "Maandag";
                    break;
            }
            switch (MainActivity.date.getMonth()) {
                case 1:  month = "jan";       break;
                case 2:  month = "feb";      break;
                case 3:  month = "maa";         break;
                case 4:  month = "apr";         break;
                case 5:  month = "mei";           break;
                case 6:  month = "jun";          break;
                case 7:  month = "jul";          break;
                case 8:  month = "aug";        break;
                case 9:  month = "sep";     break;
                case 10: month = "okt";       break;
                case 11: month = "nov";      break;
                case 12: month = "dec";      break;
                default: month = "inv"; break;
            }
            text_date.setText(day + " " + MainActivity.date.getDate() + " " + month);
            // Aligns the first and last items on the list vertically centered on the screen.
            mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

            // Customizes scrolling so items farther away form center are smaller.
            ScalingScrollLayoutCallback scalingScrollLayoutCallback =
                    new ScalingScrollLayoutCallback();
            mWearableRecyclerView.setLayoutManager(
                    new WearableLinearLayoutManager(context, scalingScrollLayoutCallback));

            // Improves performance because we know changes in content do not change the layout size of
            // the RecyclerView.
            mWearableRecyclerView.setHasFixedSize(true);

            // Specifies an adapter (see also next example).
            mCustomRecyclerAdapter = new CalendarRecAdapter(
                    dataModels,
                    new Controller(this));

            mWearableRecyclerView.setAdapter(mCustomRecyclerAdapter);
        }
    }
    public interface OnSchoolItemClick {
        public void onSchoolItemClick(School school);
    }
    public void itemSelected(String data) {

        String notificationStyle = data;

        switch (notificationStyle) {


            default:
                // continue below.
        }
    }
}
