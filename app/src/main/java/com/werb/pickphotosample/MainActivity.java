package com.werb.pickphotosample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.werb.pickphotoview.PickPhotoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPickPhoto();
            }
        });
    }

    private void startPickPhoto(){
        new PickPhotoView.Builder(MainActivity.this)
                .setPickPhotoSize(9)
                .setShowCamera(false)
                .setSpanCount(3)
                .start();
    }
}
