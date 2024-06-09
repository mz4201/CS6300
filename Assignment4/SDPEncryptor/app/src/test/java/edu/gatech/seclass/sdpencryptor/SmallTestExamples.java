package edu.gatech.seclass.sdpencryptor;

import static org.junit.Assert.fail;

import android.view.View;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/**
 * This is a Georgia Tech provided code example for use in assigned private GT
 * repositories. Students and other users of this template code are advised not
 * to share it with other students or to make it available on publicly viewable
 * websites including repositories such as github and gitlab.  Such sharing may
 * be investigated as a GT honor code violation. Created for CS6300.
 */

@RunWith(RobolectricTestRunner.class)
public class SmallTestExamples {

    private MainActivity activity;
    private RobolectricViewAssertions rva = new RobolectricViewAssertions();

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        rva.setActivity(activity);
    }

    @Test(timeout = 500)
    public void Screenshot1() {
        rva.replaceText(R.id.inputTextID, "Cat & Dog");
        rva.replaceText(R.id.multiplierInputID, "5");
        rva.replaceText(R.id.adderInputID, "8");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);

        rva.assertTextViewText(R.id.resultTextID, "D15 & Idz");
    }

    @Test(timeout = 500)
    public void Screenshot2() {
        rva.replaceText(R.id.inputTextID, "Up with the White And Gold!");
        rva.replaceText(R.id.multiplierInputID, "1");
        rva.replaceText(R.id.adderInputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);

        rva.assertTextViewText(R.id.resultTextID, "uQ XJUI UIF wIJUF aOE gPME!");
    }

    @Test(timeout = 500)
    public void Screenshot3() {
        rva.replaceText(R.id.inputTextID, "abcdefg");
        rva.replaceText(R.id.multiplierInputID, "5");
        rva.replaceText(R.id.adderInputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);

        rva.assertTextViewText(R.id.resultTextID, "X4CHMRW");
    }

    @Test(timeout = 500)
    public void trigger() {
        rva.replaceText(R.id.inputTextID, "__trigger__");
        rva.replaceText(R.id.multiplierInputID, "5");
        rva.replaceText(R.id.adderInputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);

        rva.assertTextViewText(R.id.resultTextID, "__ZPBWWMP__");
    }

    @Test(timeout = 500)
    public void errorTest1() {
        rva.replaceText(R.id.inputTextID, "");
        rva.replaceText(R.id.multiplierInputID, "0");
        rva.replaceText(R.id.adderInputID, "0");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);

        rva.assertTextViewError(R.id.inputTextID, "Invalid Input Text");
        rva.assertTextViewError(R.id.multiplierInputID, "Invalid Multiplier Input");
        rva.assertTextViewError(R.id.adderInputID, "Invalid Adder Input");
        rva.assertTextViewText(R.id.resultTextID, "");
    }

    @Test(timeout = 500)
    public void gradingTest13() {
        rva.replaceText(R.id.inputTextID, "Panda Cat");
        rva.replaceText(R.id.multiplierInputID, "23");
        rva.replaceText(R.id.adderInputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.cipherButtonID);
        rva.assertTextViewText(R.id.resultTextID, "v6SF6 b6B");
    }
}
