package com.org.finalmvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.org.finalmvp.Interceptor.Autowired;
import com.org.finalmvp.Interceptor.MvpModel;
import com.org.finalmvp.Interceptor.MvpPresenter;
import com.org.finalmvp.Interceptor.MvpView;
import com.org.finalmvp.container.PresenterContainer;
import com.org.finalmvp.model.FinalModel;
import com.org.finalmvp.proxy.ProxyHandler;
import com.org.finalmvp.view.FinalView;
import com.org.finalmvp.presenter.FinalPresenter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FinalMvp {


    /**
     * 调用这个方法表示activity就是一个View，需要实现FinalView借口或者在类上面添加@View注解
     * @param activity
     */
    public static void init(Activity activity) {
        init(activity,activity);
    }
    /**
     * 搜索activity中所有presenter字断
     * @param activity
     */

    public static void init(Activity activity,Object view) {
        initBaseFields(activity,activity,view);
        if(activity!=view){
            initBaseFields(activity,view,view);
        }
    }

    private static void initBaseFields(Activity activity,Object object,Object view){
        Field fds[]=object.getClass().getDeclaredFields();
        for(Field f:fds){
            Autowired an = f.getAnnotation(Autowired.class);
            if(an!=null){
                Class cls=f.getType();
                try {
                    if(cls.isInterface()){//去获取它继承的class
                        cls=PresenterContainer.findPresenter(activity,cls);
                    }
                    Annotation annotation= cls.getAnnotation(MvpPresenter.class);
                    if(!FinalPresenter.class.isAssignableFrom(cls)&&annotation==null){//表示是不是finalPresenter的子类，不能创建
                        try {
                            throw new Exception("Presenter必须继承FinalPresenter,或使用注解@MvpPresenter");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("dddd","======================Presenter必须继承FinalPresenter,或使用注解@MvpPresenter");
                        return;
                    }

                    Object vmObject=cls.newInstance();


                    //检索Presenter下的所有属性
                    searchChilds(activity,vmObject,view);

                    //需要在子线程中执行,创建代理类
                    Object proxyObj= ProxyHandler.newDynamicProxyHandler().bind(vmObject, ProxyHandler.TYPE_THREAD_OTHER);
                    f.setAccessible(true);
                    f.set(object,proxyObj);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索vm下所有view,vm,model
     */
    public static void searchChilds(Context context, Object Presenter , Object view){
        Field fds[]=Presenter.getClass().getDeclaredFields();//查找vm下的所有属性
        for(Field f:fds){
            Autowired an = f.getAnnotation(Autowired.class);
            if(an!=null){
                try {
                    Class cls=f.getType();//属性的类

                    f.setAccessible(true);
                    Annotation anView=null;
                    Annotation anPresenter= cls.getAnnotation(MvpPresenter.class);
                    Annotation anModel= cls.getAnnotation(MvpModel.class);

                    if(cls.isAssignableFrom(view.getClass())){//View不需要自己新建对象，拿到原始对象就行了
                        anView=view.getClass().getAnnotation(MvpView.class);
                    }

                    if(FinalView.class.isAssignableFrom(cls)||anView!=null){//如果是view需要创建代理view
                        //创建view的代理，需要在主线程执行
                        Object proxyObj= ProxyHandler.newDynamicProxyHandler().bind(view, ProxyHandler.TYPE_THREAD_MAIN);
                        f.set(Presenter,proxyObj);
                    }else{
                        if(cls.isInterface()){//如果是接口则需要去寻找类
                            Class tempCls=PresenterContainer.findPresenter(context,cls);
                            if(tempCls!=null){
                                cls=tempCls;
                            }
//                            cls=PresenterContainer.findPresenter(context,cls);

                            //寻找完后重新获取注解
                            anPresenter= cls.getAnnotation(MvpPresenter.class);
                            anModel= cls.getAnnotation(MvpModel.class);
                        }
                    }
                    if(FinalModel.class.isAssignableFrom(cls)||anModel!=null){//如果是model，直接新建一个model
                        try {
                            f.set(Presenter,cls.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }else if(FinalPresenter.class.isAssignableFrom(cls)||anPresenter!=null){//如果是Presenter，直接新建一个
                        try {
                            f.set(Presenter,cls.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
