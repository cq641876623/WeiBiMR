package com;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Test002 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Test[] a= (Test[]) Array.newInstance(Test.class,10);

        Arrays.fill(a,(Test)Class.forName(Test.class.getName()).newInstance());

        for(int i=0;i<a.length;i++){
            System.out.println(a[i]);
        }
    }
}
