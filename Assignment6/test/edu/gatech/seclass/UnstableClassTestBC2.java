package edu.gatech.seclass;
import org.junit.Test;

public class UnstableClassTestBC2 {

    @Test
    //100% branch coverage, does not reveal fault
    public void Test1() {
        UnstableClass.unstableMethod2(3, 1);
    }

    @Test
    public void Test2() {
        UnstableClass.unstableMethod2(1, 11);
    }
}
