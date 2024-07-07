package edu.gatech.seclass;
import org.junit.Test;

public class UnstableClassTestBC3 {
    @Test
    public void Test1() {
        UnstableClass.unstableMethod3(2, 1);
    }

    @Test
    public void Test2() {
        UnstableClass.unstableMethod3(0, 5);

    }

    @Test
    public void Test3() {
            UnstableClass.unstableMethod3(5, 0);
    }

    @Test
    public void Test4() {
        UnstableClass.unstableMethod3(2, 12);
    }

    @Test
    public void Test5() {
        UnstableClass.unstableMethod3(2, 10);
    }

}
