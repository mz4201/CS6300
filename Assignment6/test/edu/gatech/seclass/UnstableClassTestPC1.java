package edu.gatech.seclass;
import org.junit.Test;

public class UnstableClassTestPC1 {

    @Test
    public void Test1() {
        UnstableClass.unstableMethod1(3, 1);
    }

    @Test
    public void Test2() {
        UnstableClass.unstableMethod1(1, 8);
    }
}