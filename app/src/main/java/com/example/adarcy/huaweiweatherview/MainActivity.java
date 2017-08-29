package com.example.adarcy.huaweiweatherview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final HuaWeiWeatherView hwv = (HuaWeiWeatherView) findViewById(R.id.hwv);
        final LinearLayout ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        hwv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hwv.changeAngle(200);
            }
        });

        hwv.setAngleColorListener(new HuaWeiWeatherView.OnAngleColorListener() {
            @Override
            public void setAngleColor(int red, int green) {
                int back = Color.argb(100, red, green, 0);
                ll_layout.setBackgroundColor(back);
            }
        });
    }
}
