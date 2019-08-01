package com.halo.signup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.halo.signup.R;

public class MainActivity extends AppCompatActivity {

    private Button btn_driver ,btn_mechnical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        getSupportActionBar().setTitle("WELCOME");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_driver = findViewById(R.id.btn_driver);
        btn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this, activity_signin_activity.class);
                //i.putExtra("Driver",'d');
                startActivity(i);
            }
        });
        btn_mechnical=findViewById(R.id.btn_Mechanic);
        btn_mechnical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this, activity_signin_mechanic.class);
                //i.putExtra("Driver",'d');
                startActivity(i);
            }
        });
    }
}
