package com;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class Test {
    public  static ThreadLocal<Integer> threadLocal=new ThreadLocal<Integer>();
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
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10,new MyThreadFactory());

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

       Thread monitor=new Thread(new Runnable() {
           @Override
           public void run() {
               while (true){
                   try {
                       Thread.sleep(1000);
                       System.out.println("*************************"+"线程监听信息"+"***********************");
                       System.out.print("*");
                       for(Thread t:Thread.getAllStackTraces().keySet()){
                           System.out.print("线程名称："+t.getName());
                           System.out.print("         持有index：>        ");

                           Field field=t.getClass().getDeclaredField("threadLocals");
                           Object value=null;
                           if(field!=null){
                               if(field.isAccessible()){
                                   value = field.get(t);
                               }else {
                                   field.setAccessible(true);
                                   value = field.get(t);
                                   field.setAccessible(false);
                               }
                           }
                           if(value!=null)
                           System.out.println(value.toString());

                           System.out.print("        <*");
                       }



                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   } catch (NoSuchFieldException e) {

                       e.printStackTrace();
                       continue;
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   }
                   finally {
                       System.out.println("*************************"+"结束"+"***********************");
                   }
               }
           }
       });
       monitor.setDaemon(true);
       monitor.setName("monitor");
       monitor.start();




    }
    static class Task implements Runnable{
        public ThreadLocal<Integer> mythreadLoacl;
        @Override
        public void run() {
            while (true){
                int index=objPool.request2();
                objPool.get(index).x= (int) (Math.random()*100);
                System.out.print("当前线程 "+index+" 的实数:");
                mythreadLoacl=threadLocal;
                threadLocal.set(new Integer(index));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
class MyThreadFactory implements ThreadFactory{
    private AtomicInteger atomicInteger=new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        int c=atomicInteger.incrementAndGet();
        String name="create no " + c + " Threads";
        System.out.println(name);
        return new Thread(r,name);
    }
}