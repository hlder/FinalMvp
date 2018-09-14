package com.org.finalmvvm.test.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.org.finalmvvm.FinalMvvm;
import com.org.finalmvvm.Interceptor.Autowired;
import com.org.finalmvvm.Interceptor.View;
import com.org.finalmvvm.test.R;
import com.org.finalmvvm.test.views.MainView;
import com.org.finalmvvm.test.viewmodels.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@View
public class MainActivity extends AppCompatActivity implements MainView{
    @Autowired
    MainViewModel mainViewModel;

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this ) ;//初始化ButterKnife框架
        FinalMvvm.init(this);//初始化finalMvvm,框架自动扫描注解

//        也可以用这种方式，但是建议直接让activity实现View
//        FinalMvvm.init(this,new MyMainView());//初始化finalMvvm,框架自动扫描注解
    }

    @OnClick(R.id.button)
    public void onButtonClick(){
        //去viewmodel中加载数据,不需要管任何多线程的事
        mainViewModel.loadWeather();
    }

    @Override
    public void showWeatherText(String text) {
        textView.setText(text);
    }

//    也可以使用这种方式，但建议直接让activity实现View
//    @View
//    public class MyMainView implements MainView{
//        @Override
//        public void showWeatherText(String text) {
//            textView.setText("==========="+text);
//        }
//    }

}
