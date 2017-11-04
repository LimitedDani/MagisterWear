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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.ilexiconn.magister.container.School;

import java.util.ArrayList;

import nl.daniquedejong.magisterwear.R;

public class LoginSchoolFragment extends Fragment{
    OnLoginButtonClick mCallback;
    private Context context;
    private EditText username;
    private EditText password;
    private Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_login_school, container, false);

        username = view.findViewById(R.id.input_username);
        password = view.findViewById(R.id.input_password);
        login = view.findViewById(R.id.button_login);
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
                mCallback = (OnLoginButtonClick) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnLoginButtonClick");
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean error = false;
                    String Username = username.getText().toString();
                    String Password = password.getText().toString();
                    if (Username == null || Username.length() == 0 || Username == "") {
                        error = true;
                    }
                    if (Password == null || Password.length() == 0 || Password == "") {
                        error = true;
                    }
                    if(!error) {
                        mCallback.onLoginButtonClick(Username, Password);
                    }
                }
            });
        }
    }
    public interface OnLoginButtonClick {
        public void onLoginButtonClick(String username, String password);
    }
}
