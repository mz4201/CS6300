package edu.gatech.seclass.moditext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
        "Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE"
            + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

    /* ----------------------------- Test Utilities ----------------------------- */

    /**
     * Returns path of a new "input.txt" file with specified contents written
     * into it. The file will be created using {@link TempDir TempDir}, so it
     * is automatically deleted after test execution.
     * 
     * @param contents the text to include in the file
     * @return a Path to the newly written file, or null if there was an
     *         issue creating the file
     */
    private Path createFile(String contents) {
        return createFile(contents, "input.txt");
    }

    /**
     * Returns path to newly created file with specified contents written into
     * it. The file will be created using {@link TempDir TempDir}, so it is
     * automatically deleted after test execution.
     * 
     * @param contents the text to include in the file
     * @param fileName the desired name for the file to be created
     * @return a Path to the newly written file, or null if there was an
     *         issue creating the file
     */
    private Path createFile(String contents, String fileName) {
        Path file = tempDirectory.resolve(fileName);
        try {
            Files.writeString(file, contents);
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Takes the path to some file and returns the contents within.
     * 
     * @param file the path to some file
     * @return the contents of the file as a String, or null if there was an
     *         issue reading the file
     */
    private String getFileContent(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ------------------------------- Test Cases ------------------------------- */

    @Test
    // Frame 1:
    public void moditextTest1() {
        String input = "Test 2";
        String expected = "test 2" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "/tmp/nonExisting.txt"};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 2: no new line at end of file -> error
    public void moditextTest2() {
        String input = "Test 2";
        String expected = "test 2" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 3: empty file
    public void moditextTest3() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 4: no input file
    public void moditextTest4() {
        String input = "Test 4 Line 1" + System.lineSeparator()
                + "Test 4 Line 2" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r"};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

/*
    @Test
    // Frame 5: repeated option, only last occurrence applies
    public void moditextTest5() {
        String input = "Test 5" + System.lineSeparator();
        String expectedWrongValue = "Tes" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-t","3","-t","5", inputFile.toString()};
        Main.main(args);

        //the wrong value is not the same as output.
        Assertions.assertNotEquals(expectedWrongValue, getFileContent(inputFile));
        //no error messsage because it's not user issue
        Assertions.assertFalse(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 6:
    // did not follow programmed order
    public void moditextTest6() {
        String input = "Test 6 Line 1" + System.lineSeparator()
                + "This is Line 2" + System.lineSeparator();
        String expectedWrongValue = "This is " + System.lineSeparator()
                + "Test 6 L" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "-t","8",inputFile.toString()};
        Main.main(args);

        //the wrong value is not the same as output.
        Assertions.assertNotEquals(expectedWrongValue, getFileContent(inputFile));
        //no error messsage because it's not user issue
        Assertions.assertFalse(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
*/
    @Test
    // Frame 7:
    // unrecognized option
    public void moditextTest7() {
        String input = "Test 7" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-v","2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile)
        );
    }

    @Test
    // Frame 8:
    // empty k parameter
    public void moditextTest8() {
        String input = "Test 8" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 9:
    // "" k parameter
    public void moditextTest9() {
        String input = "Test 9" + System.lineSeparator();
        String expected = "Test 9" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 10:
    // k parameter is more than file length
    public void moditextTest10() {
        String input = "Test 10" + System.lineSeparator();
        String expected = System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "Test 10 parameter is more than file length", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 11:
    // using both p and t
    public void moditextTest11() {
        String input = "Test 11 for presence of both p and t" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p","a","2","-t","2",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 12:
    // non integer input for p or t
    public void moditextTest12() {
        String input = "Test 12" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-p","*","4.5",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 13:
    // null p input - error
    public void moditextTest13() {
        String input = "Test 13" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-p",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 14:
    //p input is not char and num
    public void moditextTest14() {
        String input = "Test 14" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-p","5","notChar",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 15:
    // p maxPadding range is <1 and > 100
    public void moditextTest15() {
        String input = "Test 15" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-p","a","0",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 16:
    // t range is <0 and >100
    public void moditextTest16() {
        String input = "Test 16" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-t","-1",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 17:
    // g is present without f
    public void moditextTest17() {
        String input = "Test 17" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-g",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 18:
    // neither f and g are present
    public void moditextTest18() {
        String input = "Test 18 Line 1" + System.lineSeparator()
        + "Test 18 Line 2" + System.lineSeparator();
        String expected = "Test 18 Li" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","2","-t","10", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 19:
    // invalid f style parameter
    public void moditextTest19() {
        String input = "Test 19" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f","underline","19", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 20:
    // null f style parameter
    public void moditextTest20() {
        String input = "Test 20" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f","","20", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 21:
    // empty f substring parameter
    public void moditextTest21() {
        String input = "Test 21" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f","bold","", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 22:
    // null f substring parameter
    public void moditextTest22() {
        String input = "Test 22" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f","bold", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 23:
    // error when someone specifiy a g parameter
    public void moditextTest23() {
        String input = "Test 23" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f","bold","-g","wrongParam", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 24:
    // not using -r
    public void moditextTest24() {
        String input = "Okay, here is how this is going to work." + System.lineSeparator()
                + "No shouting!" + System.lineSeparator()
                + "Does that make sense?" + System.lineSeparator()
                + "Alright, good meeting." + System.lineSeparator();
        String expected = "Okay, here is how this is going to work." + System.lineSeparator()
                + "Alright, good meeting." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", ".", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 25:
    // error when -r given parameter
    public void moditextTest25() {
        String input = "Test 25 Line 1" + System.lineSeparator()
                + "Test 25 Line 2" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r","badParam", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 26:
    public void moditextTest26() {
        String input = "Test 26 Line 1" + System.lineSeparator()
                + "Test 26 Line 2" + System.lineSeparator()
                + "Test 26 Line 26" + System.lineSeparator();
        String expected = "Test **2**6 Line 26" + System.lineSeparator()
                + "aTest **2**6 Line 2" + System.lineSeparator()
                + "aTest **2**6 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-f","bold","2","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 27:
    public void moditextTest27() {
        String input = "Test 27 Line 1" + System.lineSeparator()
                + "Test 27 Line 2" + System.lineSeparator()
                + "Test 27 Line 27" + System.lineSeparator();
        String expected = "Test *2*7 Line 27" + System.lineSeparator()
                + "aTest *2*7 Line 2" + System.lineSeparator()
                + "aTest *2*7 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-f","italic","2","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 28:
    public void moditextTest28() {
        String input = "Test 28 Line 1" + System.lineSeparator()
                + "Test 28 Line 2" + System.lineSeparator()
                + "Test 28 Line 28" + System.lineSeparator();
        String expected = "Test `2`8 Line 28" + System.lineSeparator()
                + "aTest `2`8 Line 2" + System.lineSeparator()
                + "aTest `2`8 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-f","code","2","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 29:
    public void moditextTest29() {
        String input = "Test 29 Line 1" + System.lineSeparator()
                + "Test 29 Line 2" + System.lineSeparator()
                + "Test 29 Line 29" + System.lineSeparator();
        String expected = "Test **2**9 Line **2**9" + System.lineSeparator()
                + "aTest **2**9 Line **2**" + System.lineSeparator()
                + "aTest **2**9 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-g","-f","bold","2","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 30:
    public void moditextTest30() {
        String input = "Test 30 Line 1" + System.lineSeparator()
                + "Test 30 Line 2" + System.lineSeparator()
                + "Test 30 Line 30" + System.lineSeparator();
        String expected = "Test *3*0 Line *3*0" + System.lineSeparator()
                + "aTest *3*0 Line 2" + System.lineSeparator()
                + "aTest *3*0 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-g","-f","italic","3","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 31:
    public void moditextTest31() {
        String input = "Test 31 Line 1" + System.lineSeparator()
                + "Test 31 Line 2" + System.lineSeparator()
                + "Test 31 Line 31" + System.lineSeparator();
        String expected = "Test `3`1 Line `3`1" + System.lineSeparator()
                + "aTest `3`1 Line 2" + System.lineSeparator()
                + "aTest `3`1 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-p","a","15","-g","-f","code","3","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 32:
    public void moditextTest32() {
        String input = "Test 32 Line 1" + System.lineSeparator()
                + "Test 32 Line 2" + System.lineSeparator()
                + "Test 32 Line 32" + System.lineSeparator();
        String expected = "Test **3**2 Li" + System.lineSeparator()
                + "Test **3**2 Li" + System.lineSeparator()
                + "Test **3**2 Li" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","10","-f","bold","3","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 33:
    public void moditextTest33() {
        String input = "Test 33 Line 1" + System.lineSeparator()
                + "Test 33 Line 2" + System.lineSeparator()
                + "Test 33 Line 33" + System.lineSeparator();
        String expected = "Test *3*3 Li" + System.lineSeparator()
                + "Test *3*3 Li" + System.lineSeparator()
                + "Test *3*3 Li" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","10","-f","italic","3","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 34:
    public void moditextTest34() {
        String input = "Test 34 Line 1" + System.lineSeparator()
                + "Test 34 Line 2" + System.lineSeparator()
                + "Test 34 Line 34" + System.lineSeparator();
        String expected = "Test `3`4 Li" + System.lineSeparator()
                + "Test `3`4 Li" + System.lineSeparator()
                + "Test `3`4 Li" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","10","-f","code","3","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 35:
    public void moditextTest35() {
        String input = "Test 35 Line 1" + System.lineSeparator()
                + "Test 35 Line 2" + System.lineSeparator()
                + "Test 35 Line 35" + System.lineSeparator();
        String expected = "T**e**st 35 Lin**e**" + System.lineSeparator()
                + "T**e**st 35 Lin**e**" + System.lineSeparator()
                + "T**e**st 35 Lin**e**" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","12","-g","-f","bold","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 36:
    public void moditextTest36() {
        String input = "Test 36 Line 1" + System.lineSeparator()
                + "Test 36 Line 2" + System.lineSeparator()
                + "Test 36 Line 36" + System.lineSeparator();
        String expected = "T*e*st 36 Lin*e*" + System.lineSeparator()
                + "T*e*st 36 Lin*e*" + System.lineSeparator()
                + "T*e*st 36 Lin*e*" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","12","-g","-f","italic","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 37:
    public void moditextTest37() {
        String input = "Test 37 Line 1" + System.lineSeparator()
                + "Test 37 Line 2" + System.lineSeparator()
                + "Test 37 Line 37" + System.lineSeparator();
        String expected = "T`e`st 37 Lin`e`" + System.lineSeparator()
                + "T`e`st 37 Lin`e`" + System.lineSeparator()
                + "T`e`st 37 Lin`e`" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-t","12","-g","-f","code","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 38:
    public void moditextTest38() {
        String input = "Test 38 Line 1" + System.lineSeparator()
                + "Test 38 Line 2" + System.lineSeparator()
                + "Test 38 Line 38" + System.lineSeparator();
        String expected = "T**e**st 38 Line 38" + System.lineSeparator()
                + "T**e**st 38 Line 2" + System.lineSeparator()
                + "T**e**st 38 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-f","bold","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 39:
    public void moditextTest39() {
        String input = "Test 39 Line 1" + System.lineSeparator()
                + "Test 39 Line 2" + System.lineSeparator()
                + "Test 39 Line 39" + System.lineSeparator();
        String expected = "T*e*st 39 Line 39" + System.lineSeparator()
                + "T*e*st 39 Line 2" + System.lineSeparator()
                + "T*e*st 39 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-f","italic","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 40:
    public void moditextTest40() {
        String input = "Test 40 Line 1" + System.lineSeparator()
                + "Test 40 Line 2" + System.lineSeparator()
                + "Test 40 Line 40" + System.lineSeparator();
        String expected = "T`e`st 40 Line 40" + System.lineSeparator()
                + "T`e`st 40 Line 2" + System.lineSeparator()
                + "T`e`st 40 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-f","code","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 41:
    public void moditextTest41() {
        String input = "Test 41 Line 1" + System.lineSeparator()
                + "Test 41 Line 2" + System.lineSeparator()
                + "Test 41 Line 41" + System.lineSeparator();
        String expected = "T**e**st 41 Lin**e** 41" + System.lineSeparator()
                + "T**e**st 41 Lin**e** 2" + System.lineSeparator()
                + "T**e**st 41 Lin**e** 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k","Line","-g","-f","bold","e","-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 42:
    public void moditextTest42() {
            String input = "Test 42 Line 1" + System.lineSeparator()
                    + "Test 42 Line 2" + System.lineSeparator()
                    + "Test 42 Line 42" + System.lineSeparator();
            String expected = "T*e*st 42 Lin*e* 42" + System.lineSeparator()
                    + "T*e*st 42 Lin*e* 2" + System.lineSeparator()
                    + "T*e*st 42 Lin*e* 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-k","Line","-g","-f","italic","e","-r", inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 43:
    public void moditextTest43() {
            String input = "Test 43 Line 1" + System.lineSeparator()
                    + "Test 43 Line 2" + System.lineSeparator()
                    + "Test 43 Line 43" + System.lineSeparator();
            String expected = "T`e`st 43 Lin`e` 43" + System.lineSeparator()
                    + "T`e`st 43 Lin`e` 2" + System.lineSeparator()
                    + "T`e`st 43 Lin`e` 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-k","Line","-g","-f","code","e","-r", inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 44:
    public void moditextTest44() {
        String input = "Test 44 Line 1" + System.lineSeparator()
                + "Test 44 Line 2" + System.lineSeparator()
                + "Test 44 Line 44" + System.lineSeparator();
        String expected = "oooooT**e**st 44 Line 44" + System.lineSeparator()
                + "ooooooT**e**st 44 Line 2" + System.lineSeparator()
                + "ooooooT**e**st 44 Line 1" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p","o","20","-f","bold","e","-r",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 45:
    public void moditextTest45() {
            String input = "Test 45 Line 1" + System.lineSeparator()
                    + "Test 45 Line 2" + System.lineSeparator()
                    + "Test 45 Line 45" + System.lineSeparator();
            String expected = "oooooT*e*st 45 Line 45" + System.lineSeparator()
                    + "ooooooT*e*st 45 Line 2" + System.lineSeparator()
                    + "ooooooT*e*st 45 Line 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-p","o","20","-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 46:
    public void moditextTest46() {
            String input = "Test 46 Line 1" + System.lineSeparator()
                    + "Test 46 Line 2" + System.lineSeparator()
                    + "Test 46 Line 46" + System.lineSeparator();
            String expected = "oooooT`e`st 46 Line 46" + System.lineSeparator()
                    + "ooooooT`e`st 46 Line 2" + System.lineSeparator()
                    + "ooooooT`e`st 46 Line 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-p","o","20","-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 47:
    public void moditextTest47() {
            String input = "Test 47 Line 1" + System.lineSeparator()
                    + "Test 47 Line 2" + System.lineSeparator()
                    + "Test 47 Line 47" + System.lineSeparator();
            String expected = "oooooT**e**st 47 Lin**e** 47" + System.lineSeparator()
                    + "ooooooT**e**st 47 Lin**e** 2" + System.lineSeparator()
                    + "ooooooT**e**st 47 Lin**e** 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-p","o","20","-g","-f","bold","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 48:
    public void moditextTest48() {
            String input = "Test 48 Line 1" + System.lineSeparator()
                    + "Test 48 Line 2" + System.lineSeparator()
                    + "Test 48 Line 48" + System.lineSeparator();
            String expected = "oooooT*e*st 48 Lin*e* 48" + System.lineSeparator()
                    + "ooooooT*e*st 48 Lin*e* 2" + System.lineSeparator()
                    + "ooooooT*e*st 48 Lin*e* 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-p","o","20","-g","-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 49:
    public void moditextTest49() {
            String input = "Test 49 Line 1" + System.lineSeparator()
                    + "Test 49 Line 2" + System.lineSeparator()
                    + "Test 49 Line 49" + System.lineSeparator();
            String expected = "oooooT`e`st 49 Lin`e` 49" + System.lineSeparator()
                    + "ooooooT`e`st 49 Lin`e` 2" + System.lineSeparator()
                    + "ooooooT`e`st 49 Lin`e` 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-p","o","20","-g","-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 50:
    public void moditextTest50() {
            String input = "Test 50 Line 1" + System.lineSeparator()
                    + "Test 50 Line 2" + System.lineSeparator()
                    + "Test 50 Line 50" + System.lineSeparator();
            String expected = "T**e**st 50 Line" + System.lineSeparator()
                    + "T**e**st 50 Line" + System.lineSeparator()
                    + "T**e**st 50 Line" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-f","bold","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 51:
    public void moditextTest51() {
            String input = "Test 51 Line 1" + System.lineSeparator()
                    + "Test 51 Line 2" + System.lineSeparator()
                    + "Test 51 Line 51" + System.lineSeparator();
            String expected = "T*e*st 51 Line" + System.lineSeparator()
                    + "T*e*st 51 Line" + System.lineSeparator()
                    + "T*e*st 51 Line" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 52:
    public void moditextTest52() {
            String input = "Test 52 Line 1" + System.lineSeparator()
                    + "Test 52 Line 2" + System.lineSeparator()
                    + "Test 52 Line 52" + System.lineSeparator();
            String expected = "T`e`st 52 Line" + System.lineSeparator()
                    + "T`e`st 52 Line" + System.lineSeparator()
                    + "T`e`st 52 Line" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 53:
    public void moditextTest53() {
            String input = "Test 53 Line 1" + System.lineSeparator()
                    + "Test 53 Line 2" + System.lineSeparator()
                    + "Test 53 Line 53" + System.lineSeparator();
            String expected = "T**e**st 53 Lin**e**" + System.lineSeparator()
                    + "T**e**st 53 Lin**e**" + System.lineSeparator()
                    + "T**e**st 53 Lin**e**" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-g","-f","bold","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 54:
    public void moditextTest54() {
            String input = "Test 54 Line 1" + System.lineSeparator()
                    + "Test 54 Line 2" + System.lineSeparator()
                    + "Test 54 Line 54" + System.lineSeparator();
            String expected = "T*e*st 54 Lin*e*" + System.lineSeparator()
                    + "T*e*st 54 Lin*e*" + System.lineSeparator()
                    + "T*e*st 54 Lin*e*" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-g","-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 55:
    public void moditextTest55() {
            String input = "Test 55 Line 1" + System.lineSeparator()
                    + "Test 55 Line 2" + System.lineSeparator()
                    + "Test 55 Line 55" + System.lineSeparator();
            String expected = "T`e`st 55 Lin`e`" + System.lineSeparator()
                    + "T`e`st 55 Lin`e`" + System.lineSeparator()
                    + "T`e`st 55 Lin`e`" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-t","12","-g","-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 56:
    public void moditextTest56() {
            String input = "Test 56 Line 1" + System.lineSeparator()
                    +"Test 56 Line 2" + System.lineSeparator();
            String expected = "T**e**st 56 Line 2" + System.lineSeparator()
                    + "T**e**st 56 Line 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-f","bold","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 57:
    public void moditextTest57() {
            String input = "Test 57 Line 1" + System.lineSeparator()
                    +"Test 57 Line 2" + System.lineSeparator();
            String expected = "T*e*st 57 Line 2" + System.lineSeparator()
                    + "T*e*st 57 Line 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 58:
    public void moditextTest58() {
            String input = "Test 58 Line 1" + System.lineSeparator()
                    +"Test 58 Line 2" + System.lineSeparator();
            String expected = "T`e`st 58 Line 2" + System.lineSeparator()
                    + "T`e`st 58 Line 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 59:
    public void moditextTest59() {
            String input = "Test 59 Line 1" + System.lineSeparator()
                    +"Test 59 Line 2" + System.lineSeparator();
            String expected = "T**e**st 59 Lin**e** 2" + System.lineSeparator()
                    + "T**e**st 59 Lin**e** 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-g","-f","bold","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 60:
    public void moditextTest60() {
            String input = "Test 60 Line 1" + System.lineSeparator()
                    +"Test 60 Line 2" + System.lineSeparator();
            String expected = "T*e*st 60 Lin*e* 2" + System.lineSeparator()
                    + "T*e*st 60 Lin*e* 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-g","-f","italic","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // Frame 61:
    public void moditextTest61() {
            String input = "Test 61 Line 1" + System.lineSeparator()
                    +"Test 61 Line 2" + System.lineSeparator();
            String expected = "T`e`st 61 Lin`e` 2" + System.lineSeparator()
                    + "T`e`st 61 Lin`e` 1" + System.lineSeparator();

            Path inputFile = createFile(input);
            String[] args = {"-g","-f","code","e","-r",inputFile.toString()};
            Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

/*new*/
    @Test
    public void moditextTest62() {
        String input = System.lineSeparator();
        String expected = "aa" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-p", "a", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest63() {
        String input = "Hello, world!" + System.lineSeparator()
                + System.lineSeparator()
                + "How are you?" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest64() {
        String input = "Hey, mind rotating this for me?" + System.lineSeparator()
                + "*" + System.lineSeparator()
                + "**" + System.lineSeparator()
                + "***" + System.lineSeparator()
                + "****" + System.lineSeparator()
                + "*****" + System.lineSeparator()
                + "Thanks!" + System.lineSeparator();

        String expected = "Hey, mind rotating this for me?" + System.lineSeparator()
                + "----*" + System.lineSeparator()
                + "---**" + System.lineSeparator()
                + "--***" + System.lineSeparator()
                + "-****" + System.lineSeparator()
                + "*****" + System.lineSeparator()
                + "Thanks!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p", "-", "5", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest65() {
        String input = "The vibrant red roses bloomed in the garden" + System.lineSeparator()
                + "She wore a beautiful blue dress to the party" + System.lineSeparator()
                + "The sky turned into a brilliant shade of blue" + System.lineSeparator()
                + "His favorite color is red, her favorite is blue" + System.lineSeparator();
        String expected = "The" + System.lineSeparator()
                + "She" + System.lineSeparator()
                + "The" + System.lineSeparator()
                + "His" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-t", "2", "-t", "6", "-t", "3", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest66() {
        String input = "Integers in Java are written using the keyword int." + System.lineSeparator()
                + "An int is 32-bits in most programming languages." + System.lineSeparator()
                + "Java is no exception." + System.lineSeparator()
                + "C++ however has uint, which is an int holding positive numbers." + System.lineSeparator();
        String expected = "Integers in Java are written using the keyword `int`." + System.lineSeparator()
                + "An `int` is 32-bits in most programming languages." + System.lineSeparator()
                + "Java is no exception." + System.lineSeparator()
                + "C++ however has u`int`, which is an int holding positive numbers." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f", "code", "int", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest67() {
        String input = "Write your name." + System.lineSeparator()
                + "Write the date." + System.lineSeparator()
                + "Answer questions 1-4." + System.lineSeparator()
                + "Ignore all other instructions and turn this in as-is." + System.lineSeparator();
        String expected = "Ignore all other instructions and turn this in as-is." + System.lineSeparator()
                + "Answer questions 1-4." + System.lineSeparator()
                + "Write the date." + System.lineSeparator()
                + "Write your name." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest68() {
        String input = "- has everyone packed? Check." + System.lineSeparator()
                + "- Does the car contain enough gas? Check." + System.lineSeparator()
                + "- Fun will be had? Check." + System.lineSeparator();
        String expected = "- has everyone packed? *Check*." + System.lineSeparator()
                + "- Fun will be had? *Check*." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f", "italic", "Check", "-k", "contain", "-k", "ha", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest69() {
        String input = "-red paint goes well with purple paint." + System.lineSeparator()
                + "-teal is a type of blue and green." + System.lineSeparator()
                + "-roses are either red or purple." + System.lineSeparator();
        String expected = "-red paint goes well with purple paint." + System.lineSeparator()
                + "-roses are either red or purple." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void moditextTest70() {
        String input = "Once upon a time, here was a hen." + System.lineSeparator()
                + "When this hen left the den, it roamed all of the land." + System.lineSeparator()
                + "All of it, until the hen got to the end." + System.lineSeparator();
        String expected = "--All of it, until the **hen** got to the end." + System.lineSeparator()
                + "W**hen** this **hen** left the den, it roamed all of the land." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "the", "-p", "-", "42", "-g", "-f", "bold", "hen", "-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    // p maxPadding range is > 100
    public void moditextTest71() {
        String input = "Test 71" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {"-p","*","101",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

        @Test
        //p input is not char
        public void moditextTest72() {
            String input = "Test 72" + System.lineSeparator();;

            Path inputFile = createFile(input);
            String[] args = {"-p","cc","5",inputFile.toString()};
            Main.main(args);

            Assertions.assertTrue(capture.stdout().isEmpty());
            Assertions.assertEquals(usageStr, capture.stderr());
            Assertions.assertEquals(input, getFileContent(inputFile));
        }

        @Test
        // t range is >100
        public void moditextTest73() {
            String input = "Test 73" + System.lineSeparator();;

            Path inputFile = createFile(input);
            String[] args = {"-t","101",inputFile.toString()};
            Main.main(args);

            Assertions.assertTrue(capture.stdout().isEmpty());
            Assertions.assertEquals(usageStr, capture.stderr());
            Assertions.assertEquals(input, getFileContent(inputFile));
        }

        @Test
        // t range is non integer
        public void moditextTest74() {
            String input = "Test 74" + System.lineSeparator();;

            Path inputFile = createFile(input);
            String[] args = {"-t","c",inputFile.toString()};
            Main.main(args);

            Assertions.assertTrue(capture.stdout().isEmpty());
            Assertions.assertEquals(usageStr, capture.stderr());
            Assertions.assertEquals(input, getFileContent(inputFile));
        }

    @Test
    public void moditextTest75() {
        String input = "Apply multiple formats here." + System.lineSeparator();
        String expected = "Apply multipl*e* formats here." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "-g", "-f", "italic", "e", "-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

}
