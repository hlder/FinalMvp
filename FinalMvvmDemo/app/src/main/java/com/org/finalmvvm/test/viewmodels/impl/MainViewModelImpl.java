package com.org.finalmvvm.test.viewmodels.impl;

import com.org.finalmvvm.Interceptor.Autowired;
import com.org.finalmvvm.Interceptor.ViewModel;
import com.org.finalmvvm.test.beans.Weather;
import com.org.finalmvvm.test.models.MainModel;
import com.org.finalmvvm.test.views.MainView;
import com.org.finalmvvm.test.viewmodels.MainViewModel;

@ViewModel
public class MainViewModelImpl implements MainViewModel{


    @Autowired
    MainView mainView;

    @Autowired
    MainModel mainModel;


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
