package com.org.finalmvp.rxs;

import java.lang.reflect.Method;

public class RxDoNext {
    Object object;
    Method method;
    Object[]args;
    public RxDoNext(Object object,Method method,Object[]args){
        this.object=object;
        this.method=method;
        this.args=args;
    }
}
