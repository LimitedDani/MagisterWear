package nl.daniquedejong.magisterwear.fragment.magister;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.daniquedejong.magisterwear.R;
public class HomeFragment extends Fragment {
    OnHomeFragmentReady mCallback;
    Context context;
    TextView welcome_message;
    TextView current_lesson;
    View view;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        welcome_message = (TextView) view.findViewById(R.id.text_welcome);
        current_lesson = (TextView) view.findViewById(R.id.current_lesson);
        //Context
        context = container.getContext();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        String name = getArguments().getString("name");
        String currentlesson = getArguments().getString("currentlesson");
        String currentlessonlocation = getArguments().getString("currentlessonlocation");
        String currentlessontime = getArguments().getString("currentlessontime");

        welcome_message.setText(getResources().getString(R.string.text_welcome) + " " + name);
        if(currentlesson != null && !currentlesson.isEmpty()) {
            current_lesson.setText(currentlesson.split(" ")[0] + " - " + currentlesson.split(" ")[2] + " - " + currentlessonlocation
                    + "\n" + currentlessontime);
        } else {
            current_lesson.setText(getResources().getString(R.string.text_no_results));
        }
        try {
            mCallback = (HomeFragment.OnHomeFragmentReady) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDoneLoading");
        }
        if (activity != null && view != null) {
            mCallback.onHomeFragmentReady();
        }
    }
    public interface OnHomeFragmentReady {
        public void onHomeFragmentReady();
    }
}
