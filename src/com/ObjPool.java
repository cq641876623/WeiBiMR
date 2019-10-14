package com;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.locks.StampedLock;

public class ObjPool<T> {

    public static int DEFULT_POOL_SIZE=5;


    private StampedLock s1;


    public T[] objs;
    private boolean[] flag;

    private int poolSize;

    private Class<T> tClass;


    public ObjPool(int poolSize,Class<T> type){
        if (poolSize<1){
            throw new IllegalArgumentException("线程池大小不能小于0");
        }
        init(poolSize,type);
    }

    public ObjPool(Class<T> type){
        init(DEFULT_POOL_SIZE,type);


    }
    private void init(int poolSize,Class<T> type){
        this.tClass=type;
        objs= (T[]) Array.newInstance(type,poolSize);
        s1=new StampedLock();
        flag=new boolean[poolSize];
        try {
            for(int i=0;i<objs.length;i++){
                objs[i]=(T)type.newInstance();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public T  get(int index){
        return objs[index];
    }




    public int request2(){

        int re=-1;
        long stamp=s1.tryOptimisticRead();
        String[] achive=null;

        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0;i<flag.length;i++){
            if(flag[i])continue;
            stringBuilder.append(i+",");
        }
        if (stringBuilder.length()==0) System.out.println("当前无可用对象");
        else {
            String temp = stringBuilder.substring(0, stringBuilder.length() - 1);
            achive = temp.split(",");
        }
        if(s1.validate(stamp)){
            long stampW=s1.writeLock();
            try{
                int index=-1;
                for(int i=0;i<achive.length;i++){
                    index=Integer.parseInt(achive[i]);
                    if(flag[index])continue;
                    else {
                        flag[index]=true;
                        re=index;
                        break;
                    }

                }
            }finally {
                s1.unlock(stampW);
            }
            if(re==-1){
                System.out.println("写锁:无可用对象");
            }
        }else {
            long stampW=s1.writeLock();
            try {
                for(int i=0;i<flag.length;i++){
                    if(flag[i])continue;
                    stringBuilder.append(i+",");
                }
                if (stringBuilder.length()==0) System.out.println("当前无可用对象");
                else {
                    String temp = stringBuilder.substring(0, stringBuilder.length() - 1);
                    achive = temp.split(",");
                    int index = -1;
                    for (int i = 0; i < achive.length; i++) {
                        index = Integer.parseInt(achive[i]);
                        if (flag[index]) continue;
                        else {
                            flag[index]=true;
                            re = index;
                            break;
                        }

                    }
                    if (re == -1) {
                        System.out.println("写锁:无可用对象");
                    }
                }
            }finally {
                s1.unlock(stampW);
            }
         }
        System.out.println("获取对象index:"+re);
        return  re;
    }

    public void free(int index){
        long stamp=s1.writeLock();
        try{
            flag[index]=false;
        }finally {
            s1.unlock(stamp);
        }
    }





    public ObjPool getObjPool() {
        return this;
    }



}
