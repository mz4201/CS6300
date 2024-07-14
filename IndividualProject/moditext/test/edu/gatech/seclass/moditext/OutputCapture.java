package edu.gatech.seclass.moditext;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * JUnit 5 extension to capture standard output and error printed during
 * a program's execution.
 */
public class OutputCapture implements BeforeEachCallback, AfterEachCallback {
    private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errStream = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(outStream);
    private final PrintStream err = new PrintStream(errStream);
    private final PrintStream outOrig = System.out;
    private final PrintStream errOrig = System.err;

    /**
     * Resets the capturing streams and reverts printing to original stream.
     */
    private void reset() {
        outStream.reset();
        errStream.reset();
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /**
     * Reverts standard output and error to original stream before each test.
     */
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        System.setOut(out);
        System.setErr(err);
    }

    /**
     * Resets standard output and error after each test.
     */
    @Override
    public void afterEach(ExtensionContext extensionContext) {
        reset();
    }

    /**
     * Resets stream to print out a custom error message.
     * @param error message to print
     */
    public void printError(String error) {
        reset();
        System.err.println(error);
    }

    /**
     * Returns captured standard output.
     * @return standard output
     */
    public String stdout() {
        return outStream.toString();
    }

    /**
     * Returns captured standard error.
     * @return standard error
     */
    public String stderr() {
        return errStream.toString();
    }
}
