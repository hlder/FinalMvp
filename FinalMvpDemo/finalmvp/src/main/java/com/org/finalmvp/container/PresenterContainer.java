package com.org.finalmvp.container;

import android.content.Context;
import android.util.Log;

import com.org.finalmvp.Interceptor.MvpModel;
import com.org.finalmvp.Interceptor.MvpPresenter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * 初始化时进行扫描，存储代码中所有的Presenter
 */
public class PresenterContainer {
    private static List<Class> list=null;

    private static List<Class> initList(Context context){
        if(list==null){
            list=new ArrayList<>();
            searchPresenters(context);
        }
        return list;
    }

    public static Class findPresenter(Context context,Class superClass){
        initList(context);
        Log.d("dddd","list:"+list.size());
        for(Class item:list){
            Log.d("dddd","item:"+item.getName());
            if(superClass.isAssignableFrom(item)){//存在继承关系
                return item;
            }
        }
        return null;
    }


    /**
     * 搜索所有的Presenter
     * @param mContext
     */
    private static void searchPresenters(Context mContext) {

        ClassLoader classLoader = mContext.getClassLoader();
        String packageCodePath = mContext.getPackageCodePath();

        File file=new File(packageCodePath);
        File FileD=file.getParentFile();

        if(FileD.isDirectory()){
            File[]files=FileD.listFiles();

            for(File itemFile:files){
                String name="aaaaaaaaaaaaaaaaaaa"+itemFile.getName();
                String hz=name.substring(name.length()-4,name.length());
                if(".apk".equals(hz)){
                    loadOneFile(itemFile,classLoader);
                }
            }
        }
    }


    private static void loadOneFile(File file,ClassLoader classLoader){
        try {
            DexFile df = new DexFile(file);
            for (Enumeration iter = df.entries(); iter.hasMoreElements(); ) {
                String clsName=""+iter.nextElement();
//                Log.d("dddd","clsName："+clsName);
                try {
//                    Class cls=classLoader.loadClass(clsName);
                    Class cls=Class.forName(clsName);
                    Annotation vm= cls.getAnnotation(MvpPresenter.class);
                    Annotation model= cls.getAnnotation(MvpModel.class);
                    if(vm!=null||model!=null){
                        list.add(cls);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }catch (NoClassDefFoundError e){
                    e.printStackTrace();
                }
//                if(clsName.contains(""+packageName)){//检测包名下的所有类，非包名的不管
//
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
