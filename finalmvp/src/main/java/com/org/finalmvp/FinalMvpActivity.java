package com.org.finalmvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FinalMvpActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FinalMvp.init(this);
    }
}
