package edu.gatech.seclass;
import org.junit.Test;

public class UnstableClassTestSCBC2 {

    @Test
    //division by zero fault
    public void Test1() {
        UnstableClass.unstableMethod2(0, 5);
    }

    @Test
    // 100% statement coverage, <100% branch coverage
    public void Test2() {
        UnstableClass.unstableMethod2(2, 5);
    }
}
