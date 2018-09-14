
使用各种架构的目的无非是让代码变的简洁，易读。并且在多人开发中可以展现出无限的魅力。不同的层可以让不同的人开发，互相独立并互相影响！
框架就是将原本需要一大堆代码的统一起来,来简化代码的编辑。
# mvvm
Model-View-ViewModel的简写，mvvm就是一种设计模式，不懂的自己百度一下。在实际开发中也可以省去model变为vvm。
# FinalMvvm
我们在Android开发中用的最多的无非就是多线程，为了避免手机卡顿，我们在做耗时操作时必须在子线程中执行，而执行完成后又需要在主线程中去做UI操作。
这样就会需要写很多的代码，并且还要考虑子线程主线程的关系，逻辑复杂了之后就容易产生一些意想不到的bug让我们头疼。<br/>
这里需要感谢rxjava，让多线程变得不用我们管，哈哈！没错，finalMvvm使用了rxjava进行多线程处理，并将rxjava和mvvm完美集合。
我们写代码的时候无需关心rxjava，只需要根据
规则编辑代码，便可以编辑出简洁易读并且性能优异的优秀代码，哈哈- -<br/>
那就让我们来看看这个神秘的finalMvvm吧！

# 上代码
1.MainView
<pre>
public interface MainView {
    /**
     * 显示天气的文字
     */
    void showWeatherText(String text);

}
</pre>

2.MainActivity需要实现MainView接口,并且需要在class上加上@View注解以表示是View层
<pre>
@View
public class MainActivity extends AppCompatActivity implements MainView{
    @Autowired
    MainViewModel mainViewModel;
    
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        FinalMvvm.init(this);//初始化finalMvvm,框架自动扫描注解
    }
    @Override
    public void showWeatherText(String text) {
        textView.setText(text);
    }
}
</pre>

3.ViewModel
<pre>
public interface MainViewModel {
    void loadWeather();
}
</pre>
4.MainViewModelImpl实现ViewModel,并且需要在class上方加上注解@ViewModel,以表示是ViewModel层
<pre>
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
</pre>


5.MainModel直接新建一个model类加上@Model注解
<pre>
@Model
public class MainModel {
    /**
     * 从服务器获取天气数据，对数据进行基本处理，转成需要的格式(如json，bean，string等)
     * 不需要多线程，直接同步执行就行了
     */
    public Weather loadWeatherFromUrl(){
        //执行http请自行去处理
        String jsonStr=doHttp("http://www.weather.com.cn/data/cityinfo/101010100.html");
        JSONObject jo=JSON.parseObject(jsonStr);
        JSONObject weatherinfo=jo.getJSONObject("weatherinfo");
        Weather weather=weatherinfo.toJavaObject(Weather.class);
        return weather;
    }
}
</pre>

# 完结
到这里就完成了，这样就层次很清晰了。并且适合多人开发。
