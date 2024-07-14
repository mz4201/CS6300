package edu.gatech.seclass.moditext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MainTest {
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
    public void exampleTest1() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest2() {
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
    public void exampleTest3() {
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
    public void exampleTest4() {
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
    public void exampleTest5() {
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
    public void exampleTest6() {
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
    public void exampleTest7() {
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
    public void exampleTest8() {
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
    public void exampleTest9() {
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
    public void exampleTest10() {
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
    public void exampleTest11() {
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
}
