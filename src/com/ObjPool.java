package com;

public class ObjPool {
    private volatile static ObjPool objPool;

    

    public ObjPool() {



    }

    public static ObjPool getObjPool() {
        if(objPool==null){
            synchronized (ObjPool.class){
                if(objPool!=null){
                    objPool=new ObjPool();
                }
            }
        }
        return  objPool;
    }
}
