package edu.gatech.seclass;
import org.junit.Test;

public class UnstableClassTestBC1 {

    @Test
    public void Test1() {
        UnstableClass.unstableMethod1(3, 1);
    }

    @Test
    //dvision by zero fault
    public void Test2() {
        UnstableClass.unstableMethod1(0, 2);
    }
}