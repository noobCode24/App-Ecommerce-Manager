package com.example.app_ecommerce.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_ecommerce.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Paper.init(this);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }catch (Exception ex){

                }finally {
                    if(Paper.book().read("user") == null){
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent main = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}