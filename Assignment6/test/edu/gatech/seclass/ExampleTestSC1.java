package edu.gatech.seclass;

import org.junit.jupiter.api.Test;

/**
 * This is a Georgia Tech provided code example for use in assigned
 * private GT repositories. Students and other users of this template
 * code are advised not to share it with other students or to make it
 * available on publicly viewable websites including repositories such
 * as GitHub and GitLab.  Such sharing may be investigated as a GT
 * honor code violation. Created for CS6300 Summer 2024.
 *
 * Junit test class provided for the White-Box Testing Assignment.
 * This class should not be altered.  Follow the directions to create
 * similar test classes when required.
 */

public class  ExampleTestSC1 {

    // This is an example of a test suite supposed to fail with an uncaught
    // exception.
    // Notes:
    // - Make sure NOT to use the "expected" notation and NOT to catch
    //   the exception.
    // - Unlike for normal test, there is no need to have an assertion
    //   in the test.
    @Test
    public void Test1() { UnstableClass.exampleMethod1(1); }

    // This is another example of a test supposed to fail with an
    // uncaught exception.
    @Test
    public void Test2() { UnstableClass.exampleMethod1(100); }
}
