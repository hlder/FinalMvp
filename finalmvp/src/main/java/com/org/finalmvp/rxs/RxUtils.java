package com.org.finalmvp.rxs;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    /**
     * 执行类
     */
    public static void execute(Object Object, Method method, Object ... params){
        execute(new RxCallable(Object,method,params));
    }

    private static void execute(Callable<ObservableSource<? extends Object>> callable){
        Observable<Object> observable=Observable.defer(callable);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxDisposableObserver());
    }




}
