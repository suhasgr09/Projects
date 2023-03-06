package edu.stevens.cs522.hello;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowActivity extends MainActivity {


    public final static String MESSAGE_KEY = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        String message = getIntent().getStringExtra(MESSAGE_KEY);

        TextView text_view=new TextView(this);
        text_view.setTextSize(50);
        text_view.setTextColor(Color.BLACK);
        text_view.setPadding(11,500,11,11);
        text_view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        text_view.setText(new StringBuilder().append(message).toString());



        ViewGroup layout= findViewById(R.id.activity_show);
        layout.addView(text_view);
    }
}