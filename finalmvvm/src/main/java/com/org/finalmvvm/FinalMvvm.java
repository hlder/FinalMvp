package com.org.finalmvvm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.org.finalmvvm.Interceptor.Autowired;
import com.org.finalmvvm.Interceptor.Model;
import com.org.finalmvvm.Interceptor.View;
import com.org.finalmvvm.Interceptor.ViewModel;
import com.org.finalmvvm.container.ViewModelContainer;
import com.org.finalmvvm.model.FinalModel;
import com.org.finalmvvm.proxy.ProxyHandler;
import com.org.finalmvvm.view.FinalView;
import com.org.finalmvvm.vm.FinalViewModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FinalMvvm {


    /**
     * 调用这个方法表示activity就是一个View，需要实现FinalView借口或者在类上面添加@View注解
     * @param activity
     */
    public static void init(Activity activity) {
        init(activity,activity);
    }
    /**
     * 搜索activity中所有viewModel字断
     * @param activity
     */

    public static void init(Activity activity,Object view) {
        Field fds[]=activity.getClass().getDeclaredFields();
        for(Field f:fds){
            Autowired an = f.getAnnotation(Autowired.class);
            if(an!=null){
                Class cls=f.getType();
                try {
                    if(cls.isInterface()){//去获取它继承的class
                        cls=ViewModelContainer.findViewModel(activity,cls);
                    }

                    Annotation annotation= cls.getAnnotation(ViewModel.class);
                    if(!FinalViewModel.class.isAssignableFrom(cls)&&annotation==null){//表示是不是finalViewModel的子类，不能创建
                        try {
                            throw new Exception("viewModel必须继承FinalViewModel,或使用注解@ViewModel");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("dddd","======================viewModel必须继承FinalViewModel,或使用注解@ViewModel");
                        return;
                    }

                    Object vmObject=cls.newInstance();


                    //检索viewModel下的所有属性
                    searchChilds(activity,vmObject,view);

                    //需要在子线程中执行
                    Object proxyObj= ProxyHandler.newDynamicProxyHandler().bind(vmObject, ProxyHandler.TYPE_THREAD_OTHER);
                    f.setAccessible(true);
                    f.set(activity,proxyObj);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索vm下所有view,vm,model
     */
    public static void searchChilds(Context context, Object viewModel , Object view){
        Field fds[]=viewModel.getClass().getDeclaredFields();//查找vm下的所有属性
        for(Field f:fds){
            Autowired an = f.getAnnotation(Autowired.class);
            if(an!=null){
                try {
                    Class cls=f.getType();//属性的类

                    f.setAccessible(true);
                    Annotation anView=null;
                    Annotation anViewModel= cls.getAnnotation(ViewModel.class);
                    Annotation anModel= cls.getAnnotation(Model.class);

                    if(cls.isAssignableFrom(view.getClass())){//View不需要自己新建对象，拿到原始对象就行了
                        anView=view.getClass().getAnnotation(View.class);
                    }

                    if(FinalView.class.isAssignableFrom(cls)||anView!=null){//如果是view需要创建代理view
                        //创建view的代理，需要在主线程执行
                        Object proxyObj= ProxyHandler.newDynamicProxyHandler().bind(view, ProxyHandler.TYPE_THREAD_MAIN);
                        f.set(viewModel,proxyObj);
                    }else{
                        if(cls.isInterface()){//如果是接口则需要去寻找类
                            cls=ViewModelContainer.findViewModel(context,cls);
                            //寻找完后重新获取注解
                            anViewModel= cls.getAnnotation(ViewModel.class);
                            anModel= cls.getAnnotation(Model.class);
                        }
                    }
                    if(FinalModel.class.isAssignableFrom(cls)||anModel!=null){//如果是model，直接新建一个model
                        try {
                            f.set(viewModel,cls.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }else if(FinalViewModel.class.isAssignableFrom(cls)||anViewModel!=null){//如果是viewModel，直接新建一个
                        try {
                            f.set(viewModel,cls.newInstance());
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
