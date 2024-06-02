package edu.gatech.seclass;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Timeout.ThreadMode;


/**
 * Junit test class created for use in Georgia Tech CS6300.
 * <p>
 * This class is provided to interpret your grades via junit tests
 * and as a reminder, should NOT be posted in any public repositories,
 * even after the class has ended.
 */

@Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
public class MyStringTest {

    private MyStringInterface myString;

    @BeforeEach
    public void setUp() {
        myString = new MyString();
    }

    @AfterEach
    public void tearDown() {
        myString = null;
    }

    @Test
    // Description: First count number example in the interface documentation
    public void testCountAlphabeticWords1() {
        myString.setString("My numbers are 11, 96, and thirteen");
        assertEquals(5, myString.countAlphabeticWords());
    }

    @Test
    // Description: Second count number example in the interface documentation
    public void testCountAlphabeticWords2() {
        myString.setString("i#love 2 pr00gram.");
        assertEquals(4, myString.countAlphabeticWords());
    }

    @Test
    // Description: Test CountAlphabeticWords with null string
    public void testCountAlphabeticWords3() {
        assertThrows(NullPointerException.class, () -> myString.countAlphabeticWords());
    }

    @Test
    // Description: Test for another example with countAlphabeticWords
    public void testCountAlphabeticWords4() {
        myString.setString("?chi!kenNu33et@s");
        assertEquals(4, myString.countAlphabeticWords());
    }

    @Test
    // Description: Test for string with no alphabetical words
    public void testSetString1() {
        assertThrows(IllegalArgumentException.class, () -> myString.setString("11@0#3$"));
    }

    @Test
    // Description: Sample encryption 1
    public void testEncrypt1() {
        myString.setString("Cat & 5 DogS");
        assertEquals("aY0 & J fBXs", myString.encrypt(5, 3));
    }

    @Test
    // Description: Test if arg1 is not an integer coprime to 62
    public void testEncrypt2() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(2,3));
    }

    @Test
    // Description: Test if arg1 is not an integer between 0 and 62
    public void testEncrypt3() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(63,3));
    }

    @Test
    // Description: Test if arg2 is not an integer between 1 and 62
    public void testEncrypt4() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(5,-1));
    }

    @Test
    // Description: Test if both arg1 and arg2 are incorrect
    public void testEncrypt5() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(-1,62));
    }

    @Test
    // Description: Test for another encrypt example
    public void testEncrypt6() {
        myString.setString("Wendy's 4for4 Value Meal, tast3 so g00d!");
        assertEquals("bDE2T'I mKLBm zGVWD yDGV, PGIPJ IL R772!", myString.encrypt(7, 7));
    }

    @Test
    // Description: First convert digits example in the interface documentation
    public void testConvertDigitsToNamesInSubstring1() {
        myString.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        myString.convertDigitsToNamesInSubstring(17, 23);
        assertEquals("I'd b3tt3r put sZerome dOneSix1ts in this 5tr1n6, right?", myString.getString());
    }

    @Test
    // Description: Second convert digits example in the interface documentation
    public void testConvertDigitsToNamesInSubstring2() {
        myString.setString("abc416d");
        myString.convertDigitsToNamesInSubstring(2, 7);
        assertEquals("abcFourOneSixd", myString.getString());
    }

    @Test
    // Description: If firstPosition  is greater than finalPosition
    public void testConvertDigitsToNamesInSubstring3() {
        myString.setString("abc416d");
        assertThrows(IllegalArgumentException.class, () -> myString.convertDigitsToNamesInSubstring(6,3));
    }

    @Test
    // Description: If finalPosition is out of bound
    public void testConvertDigitsToNamesInSubstring4() {
        myString.setString("abc416d");
        assertThrows(MyIndexOutOfBoundsException.class, () -> myString.convertDigitsToNamesInSubstring(2,15));
    }

    @Test
    // Description: Test for another convertDigitsToNamesInSubstring example
    public void testConvertDigitsToNamesInSubstring5() {
        myString.setString("Today is 6/2/2024, and I am 26 years old turning 27 years old this year.");
        myString.convertDigitsToNamesInSubstring(2, 20);
        assertEquals("Today is Six/Two/TwoZeroTwoFour, and I am 26 years old turning 27 years old this year.", myString.getString());
    }
}
