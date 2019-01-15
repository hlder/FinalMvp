package com.org.finalmvp.proxy;

import com.org.finalmvp.rxs.RxDisposableObserver;
import com.org.finalmvp.rxs.RxDoNext;
import com.org.finalmvp.rxs.RxUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 创建代理类
 */
public class ProxyHandler implements InvocationHandler {
    public static final int TYPE_THREAD_MAIN=1;
    public static final int TYPE_THREAD_OTHER=2;

    private Object object;
    private int type=1;//判断是在哪个线程执行


    public static ProxyHandler newDynamicProxyHandler(){
        return new ProxyHandler();
    }
    public Object bind(Object object,int type){
        this.object=object;
        this.type=type;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args){

        String name=method.getName();
        if("getClass".equals(name)||"equals".equals(name)||"hashCode".equals(name)||"toString".equals(name)||"notify".equals(name)||"notifyAll".equals(name)||"wait".equals(name)){
            Object invoke = null;
            try {
                invoke = method.invoke(object, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return invoke;
        }
        //到子线程去执行方法,不需要返回什么
        if(TYPE_THREAD_MAIN==type){//需要在主线程执行
            Observable.just(new RxDoNext(object,method,args)).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new RxDisposableObserver());
        }
        if(TYPE_THREAD_OTHER==type){//需要在子线程中执行
            RxUtils.execute(object,method,args);
        }
        return null;
    }
}