package nl.daniquedejong.magisterwear.fragment.setup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import nl.daniquedejong.magisterwear.R;

public class SearchSchoolFragment extends Fragment{
    OnSearchButtonClick mCallback;
    private Context context;
    private EditText schoolname;
    private Button search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_search_school, container, false);

        schoolname = (EditText) view.findViewById(R.id.input_schoolname);
        search = (Button) view.findViewById(R.id.button_search);

        //Context
        context = container.getContext();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            try {
                mCallback = (OnSearchButtonClick) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnSearchButtonClick");
            }
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onSearchButtonClick(schoolname.getText().toString());
                }
            });
        }
    }
    public interface OnSearchButtonClick {
        public void onSearchButtonClick(String schoolname);
    }
}
