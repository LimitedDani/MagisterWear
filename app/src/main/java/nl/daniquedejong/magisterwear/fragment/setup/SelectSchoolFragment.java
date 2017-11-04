package nl.daniquedejong.magisterwear.fragment.setup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ilexiconn.magister.container.School;

import java.util.ArrayList;

import nl.daniquedejong.magisterwear.R;

public class SelectSchoolFragment extends Fragment{
    OnSchoolItemClick mCallback;
    private Context context;

    public ListView schoolnames;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayList;
    public School[] mSchools;
    public ArrayList<School> SchoolObjects = new ArrayList<School>();
    public Thread mSearchThread;
    public boolean clickAvailable = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_select_school, container, false);

        schoolnames = (ListView) view.findViewById(R.id.listview_schools);

        //Context
        context = container.getContext();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            arrayList = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayList);
            schoolnames.setAdapter(adapter);
            mSearchThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSchools = School.findSchool(getArguments().getString("school").replace(" ", "%20"));
                        arrayList.clear();
                        SchoolObjects.clear();
                        if ((mSchools.length == 0) || (getArguments().getString("school").length() <= 3)) {
                            arrayList.clear();
                            arrayList.add(getString(R.string.text_no_results));
                            Log.d("error", "No schools found");
                            clickAvailable = false;
                            return;
                        }
                        int i =0;
                        for (School school : mSchools
                                ) {
                            if (i <= 19) {
                                arrayList.add(school.name);
                                SchoolObjects.add(school);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                i++;
                            } else {
                                break;
                            }
                        }
                        clickAvailable = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mSearchThread.start();
            try {
                mCallback = (OnSchoolItemClick) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnSearchButtonClick");
            }
            schoolnames.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    mCallback.onSchoolItemClick(SchoolObjects.get(position));
                }
            });
        }
    }
    public interface OnSchoolItemClick {
        public void onSchoolItemClick(School school);
    }
}
