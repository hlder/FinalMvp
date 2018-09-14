package com.org.finalmvvm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FinalMvvmActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FinalMvvm.init(this);
    }
}
