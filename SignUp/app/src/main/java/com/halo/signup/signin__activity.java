package com.halo.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class signin__activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin__activity);
    }
    public void function_signup(View view) {
        startActivity(new Intent(getApplicationContext(),signin__activity.class));

    }
}
