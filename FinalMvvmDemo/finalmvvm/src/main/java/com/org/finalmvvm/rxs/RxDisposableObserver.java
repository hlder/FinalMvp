package com.org.finalmvvm.rxs;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;

import io.reactivex.observers.DisposableObserver;

public class RxDisposableObserver extends DisposableObserver<Object> {
    @Override public void onComplete() {
        //这里是主线程
    }
    @Override public void onError(Throwable e) {
        //这里是主线程
    }
    @Override public void onNext(Object object) {
        //这里是主线程
        if(object==null){
            return;
        }
        if(object instanceof RxDoNext){
            RxDoNext doNext= (RxDoNext) object;
            try {
                if(doNext.object instanceof Activity){
                    Activity temA= (Activity) doNext.object;
                    if(temA.isDestroyed()){//如果activity被销毁了，下面就不执行了，防止出现错误
                        return;
                    }
                }
                if(doNext.args!=null&&doNext.args.length>0){
                    doNext.method.invoke(doNext.object,doNext.args);
                }else{
                    doNext.method.invoke(doNext.object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
};