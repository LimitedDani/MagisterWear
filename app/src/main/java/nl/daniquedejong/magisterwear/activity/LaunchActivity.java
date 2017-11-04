package nl.daniquedejong.magisterwear.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

import nl.daniquedejong.magisterwear.MainActivity;
import nl.daniquedejong.magisterwear.R;

/**
 * Created by daniq on 11/3/2017.
 */

public class LaunchActivity extends WearableActivity {
    private Button login, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        login = (Button) findViewById(R.id.button_login);
        about = (Button) findViewById(R.id.button_about);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent setupIntent = new Intent(LaunchActivity.this, SetupActivity.class);
                startActivity(setupIntent);
            }
        });
        setAmbientEnabled();
    }
}
