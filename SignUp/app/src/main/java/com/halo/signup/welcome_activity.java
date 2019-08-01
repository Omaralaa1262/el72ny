package com.halo.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class welcome_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
    }

    public void function_supporter(View view) {
        startActivity(new Intent(getApplicationContext(),signin__activity.class));
    }

    public void function_customer(View view) {
        startActivity(new Intent(getApplicationContext(),signin__activity.class));

    }
}
