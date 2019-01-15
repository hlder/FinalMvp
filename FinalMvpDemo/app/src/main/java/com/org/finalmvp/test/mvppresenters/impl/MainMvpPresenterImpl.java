package com.org.finalmvp.test.mvppresenters.impl;

import com.org.finalmvp.Interceptor.Autowired;
import com.org.finalmvp.Interceptor.MvpPresenter;
import com.org.finalmvp.test.beans.Weather;
import com.org.finalmvp.test.mvpmodels.MainMvpModel;
import com.org.finalmvp.test.mvpviews.MainMvpView;
import com.org.finalmvp.test.mvpviews.TestMvpView;

@MvpPresenter
public class MainMvpPresenterImpl implements com.org.finalmvp.test.mvppresenters.MainMvpPresenter {


    @Autowired
    MainMvpView mainView;

    @Autowired
    MainMvpModel mainModel;

    @Autowired
    TestMvpView testView;


    @Override
    public void loadWeather() {
        /**
         * 此处做数据处理，处理完后，在主动让view去修改UI
         */

        Weather weather=mainModel.loadWeatherFromUrl();//获取天气预报信息
        String reslut="";
        if(weather!=null){
            reslut="城市:"+weather.getCity()+"  最低温度:"+weather.getTemp1()+"   最高温度:"+weather.getTemp2()+"    天气情况:"+weather.getWeather();
        }
        mainView.showWeatherText(reslut);

    }
}
