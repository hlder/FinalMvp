package com.org.finalmvvm.container;

import android.content.Context;

import com.org.finalmvvm.Interceptor.Model;
import com.org.finalmvvm.Interceptor.ViewModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * 初始化时进行扫描，存储代码中所有的ViewModel
 */
public class ViewModelContainer {
    private static List<Class> list=null;

    private static List<Class> initList(Context context){
        if(list==null){
            list=new ArrayList<>();
            searchViewModels(context);
        }
        return list;
    }

    public static Class findViewModel(Context context,Class superClass){
        initList(context);
        for(Class item:list){
            if(superClass.isAssignableFrom(item)){//存在继承关系
                return item;
            }
        }
        return null;
    }






    /**
     * 搜索所有的ViewModel
     * @param mContext
     */
    private static void searchViewModels(Context mContext) {
        try {
            String packageCodePath = mContext.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration iter = df.entries(); iter.hasMoreElements(); ) {
                String clsName=""+iter.nextElement();
                if(clsName.contains(""+mContext.getPackageName())){//检测包名下的所有类，非包名的不管
                    try {
                        Class cls=Class.forName(clsName);
                        Annotation vm= cls.getAnnotation(ViewModel.class);
                        Annotation model= cls.getAnnotation(Model.class);
                        if(vm!=null||model!=null){
                            list.add(cls);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
