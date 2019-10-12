package com;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class ObjPool<T> {

    public static int DEFULT_POOL_SIZE=5;


    private StampedLock s1;

    private int x=0;

    private T[] objs;

    private int poolSize;
    private AtomicInteger cursor;
    private int cursorN;


    public ObjPool(int poolSize,Class<T> type){
        if (poolSize<1){
            throw new IllegalArgumentException("线程池大小不能小于0");
        }
        init(poolSize,type);
    }

    public ObjPool(Class<T> type){
        init(DEFULT_POOL_SIZE,type);


    }
    public void init(int poolSize,Class<T> type){
        objs= (T[]) Array.newInstance(type,poolSize);
        s1=new StampedLock();
        cursor=new AtomicInteger(0);
        cursorN=0;
    }

    public T  request(){
//        long stamp=s1.
//
//        if(s1)

        return objs[0];
    }





    public ObjPool getObjPool() {
        return this;
    }



}
