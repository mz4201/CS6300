package edu.gatech.seclass;
import org.junit.Test;
import static org.junit.Assert.*;


/*
Test    a	    b==0    c>0    d!=0	            Result
1	    true	True				            1
2	    false	False	True			        2
3	    false	False	False			        3
4	    false	False	True	True		    2
5	    false	False	True	False	True	2
6	    false	False	True	True	True	2
7	    false	False	False	False	True	3
*/

public class UnstableClassTestMCDC4 {
    @Test
    public void Test1() {
        int result = UnstableClass.unstableMethod4(true, 2, 2, 2);
        assertEquals(1, result);
    }

    @Test
    public void Test2() {
        int result = UnstableClass.unstableMethod4(false, 0, 2, 2);
        assertEquals(2, result);
    }

    @Test
    public void Test3() {
        int result = UnstableClass.unstableMethod4(false, 2, 0, 0);
        assertEquals(3, result);
    }

    @Test
    public void Test4() {
        int result = UnstableClass.unstableMethod4(false, 0, 2, 0);
        assertEquals(2, result);
    }

    @Test
    public void Test5() {
        int result = UnstableClass.unstableMethod4(false, 0, 0, 2);
        assertEquals(2, result);
    }

    @Test
    public void Test6() {
        int result = UnstableClass.unstableMethod4(false, 0, 2, 2);
        assertEquals(2, result);
    }

    @Test
    public void Test7() {
        int result = UnstableClass.unstableMethod4(false, 2, 0, 2);
        assertEquals(3, result);
    }
}
