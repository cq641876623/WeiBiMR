package com;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class Test {
    public int x=0;
    public static ObjPool<Test> objPool;

    @Override
    public String toString() {
        return
                " " + x +
                ' ';
    }

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
//        Object o=Class.forName(Integer.class.getName());


        objPool =new ObjPool<Test>(10,Test.class);
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);

//        int index=objPool.request2();
//        Test a=objPool.get(index);
//        a.x=1;
////        a=1;
//        System.out.println(objPool.get(index).x);
//        System.out.println(objPool.get(index).hashCode());
//
//
////       String[] a=null;
////       String b="1,2,3,4";
////       String[] c=b.split(",");
////
       for(int i=0;i<Runtime.getRuntime().availableProcessors()*10+4;i++){
           executorService.submit(new Task());
       }




    }
    static class Task implements Runnable{
        @Override
        public void run() {
            while (true){
                int index=objPool.request2();
                objPool.get(index).x= (int) (Math.random()*100);
                System.out.print("当前线程 "+index+" 的实数:");
                System.out.print("index "+index+":");
                for(int i=0;i<objPool.objs.length;i++){
                    if(i==index){
                        System.out.print("  >>>");
                    }
                    System.out.print(objPool.objs[i]);
                    if(i==index){
                        System.out.print("<<<  ");
                    }
                }
                System.out.println();
                objPool.free(index);
                System.out.println("释放："+index);
            }

        }
    }
}
