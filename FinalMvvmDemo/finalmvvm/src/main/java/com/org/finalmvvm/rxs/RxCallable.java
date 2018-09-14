package com.org.finalmvvm.rxs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class RxCallable implements Callable<ObservableSource<? extends Object>> {
    private Object vm;
    private Method method;
    private Object[] params;
    public RxCallable(Object vm,Method method,Object ... params){
        this.vm=vm;
        this.method=method;
        this.params=params;
    }
    @Override
    public ObservableSource<? extends Object> call() {
        //这里是子线程,可以执行延时操作
        try {
            if(params!=null&&params.length>0){
                method.invoke(vm,params);
            }else{
                method.invoke(vm);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Observable.just("");
    }
}