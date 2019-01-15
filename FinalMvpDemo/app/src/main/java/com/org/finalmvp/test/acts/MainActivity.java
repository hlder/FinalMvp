package com.org.finalmvp.test.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.org.finalmvp.FinalMvp;
import com.org.finalmvp.Interceptor.Autowired;
import com.org.finalmvp.Interceptor.MvpView;
import com.org.finalmvp.test.R;
import com.org.finalmvp.test.mvpviews.MainMvpView;
import com.org.finalmvp.test.mvppresenters.MainMvpPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@MvpView
public class MainActivity extends AppCompatActivity implements MainMvpView {
    @Autowired
    MainMvpPresenter mainViewModel;

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this ) ;//初始化ButterKnife框架
        FinalMvp.init(this);//初始化finalMvp,框架自动扫描注解

//        也可以用这种方式，但是建议直接让activity实现View
//        FinalMvp.init(this,new MyMainView());//初始化finalMvp,框架自动扫描注解
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
//    @MvpView
//    public class MyMainView implements MainMvpView{
//        @Override
//        public void showWeatherText(String text) {
//            textView.setText("==========="+text);
//        }
//    }

}
