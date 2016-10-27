package com.zero.mbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ui.SwitchButton;

public class MainActivity extends AppCompatActivity {
    private SwitchButton mSwitchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitchButton= (SwitchButton) findViewById(R.id.switchbutton);
//        mSwitchButton.setSwitchButtonBackgroundResource(R.drawable.switch_background);
//        mSwitchButton.setSwitchButtonResource(R.drawable.slide_button);
//
//        mSwitchButton.setState(true);

        mSwitchButton.setOnSwitchStateUpdateListener(new SwitchButton.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(getApplicationContext(), "state: " + state, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
