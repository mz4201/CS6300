package edu.gatech.seclass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnstableClassTestSC4 {

    @Test
    public void Test1() {
        int result = UnstableClass.unstableMethod4(true, 0, 0, 0);
        assertEquals(1, result);
    }

    @Test
    public void Test2() {
        int result = UnstableClass.unstableMethod4(false, 0, 0, 0);
        assertEquals(2, result);
    }

    @Test
    public void Test3() {
        int result = UnstableClass.unstableMethod4(false, 1, 0, 1);
        assertEquals(3, result);
    }
}
