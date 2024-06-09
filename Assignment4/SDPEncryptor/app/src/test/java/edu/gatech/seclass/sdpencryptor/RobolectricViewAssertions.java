package edu.gatech.seclass.sdpencryptor;

import static org.junit.Assert.fail;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// DO NOT ALTER THIS CLASS. Use it's methods to write meaningful robolectric
// tests with enriched output. See more about robolectric at:
// https://robolectric.org

/**
 * Provides utility methods to assert conditions on views within a Robolectric
 * test environment for Android. This class simplifies common tasks such as
 * asserting values of TextViews, clicking buttons, and handling UI updates. It
 * is designed to be used with Robolectric tests to facilitate writing more
 * expressive and meaningful test cases by providing detailed assertions and
 * actions related to Android views. The class requires an Activity context to
 * interact with the UI elements, which must be set before invoking its methods.
 */
public class RobolectricViewAssertions {

    private Activity activity;
    private static final long CLICK_TIMEOUT_MILLIS = 500;

    /**
     * Default constructor for RobolectricViewAssertions.
     * Initializes a new instance of the class without setting an activity.
     * The activity must be set using setActivity(Activity activity) before using
     * this class's methods.
     */
    public RobolectricViewAssertions() {
    }

    /**
     * Ensures that the activity has been set; other methods cannot be called
     * before it's set
     */
    private void ensureActivitySet() {
        if (activity == null) {
            throw new IllegalStateException(
                "Activity has not been set. Please set an Activity before using this method.");
        }
    }
    /**
     * Constructs a detailed description of a View, including its ID, resource
     * name, visibility, enabled state, and focusability.
     *
     * @param view The View to describe.
     * @return     A descriptive string of the View's attributes.
     */
    private String getViewDetails(View view) {
        ensureActivitySet();
        String fullResourceName = view.getResources().getResourceName(view.getId());
        String resName =
            fullResourceName.substring(fullResourceName.lastIndexOf("/") + 1);
        return String.format("ID: %d, res-name: %s, visibility: %s, enabled: %s, focusable: %s",
            view.getId(),
            resName,
            view.getVisibility() == View.VISIBLE ? "VISIBLE" : "INVISIBLE",
            view.isEnabled(),
            view.isFocusable());
    }

    /**
     * Asserts the text of a TextView matches the expected text,
     * providing the text view details if not.
     *
     * @param viewId          The ID of the TextView to check.
     * @param expectedText    The text we expect the TextView to contain.
     * @throws AssertionError if the actual text does not match the expected text.
     */
    public void assertTextViewText(int viewId, String expectedText) {
        ensureActivitySet();
        TextView textView = activity.findViewById(viewId);
        String actualText = textView.getText().toString();

        if (!expectedText.equals(actualText)) {
            String message =
                String.format("Expected: \"%s\" but was: \"%s\"%nView Details: %s",
                              expectedText, actualText, getViewDetails(textView));
            fail(message);
        }
    }

    /**
     * Asserts the error message of a TextView matches the expected text,
     * providing the text view details if not.
     *
     * @param viewId          The ID of the TextView to check.
     * @param expectedError   The expected error message of the TextView.
     * @throws AssertionError if the actual error message does not match the
     *                        expected message.
     */
    public void assertTextViewError(int viewId, String expectedError) {
        ensureActivitySet();
        TextView textView = activity.findViewById(viewId);
        CharSequence errorText = textView.getError();

        if (errorText == null || !expectedError.equals(errorText.toString())) {
            String actualError = errorText != null ? errorText.toString() : "null";
            String message = String.format(
                "Expected Error: \"%s\" but was: \"%s\"%nView Details: %s",
                expectedError, actualError, getViewDetails(textView));
            fail(message);
        }
    }

    /**
     * Replaces the text of a TextView or EditText identified by viewId with the
     * specified string.
     *
     * @param viewId                 The ID of the view to update.
     * @param newText                The new text to set in the TextView or EditText.
     * @throws IllegalStateException if the view with the given ID is not a
     *                               TextView or EditText.
     */
    public void replaceText(int viewId, String newText) {
        ensureActivitySet();
        View view = activity.findViewById(viewId);

        if (!(view instanceof TextView)) {
            throw new IllegalStateException("The view with ID " + viewId + " is not a TextView or EditText.");
        }

        activity.runOnUiThread(() -> ((TextView)view).setText(newText));
    }

    /**
     * Attempts to click a button with a specific timeout defined in this class.
     * This method addresses the limitation that setting timeouts directly in
     * tests does not affect operations performed on separate threads, such as
     * button clicks.
     *
     * @param buttonId       The ID of the button to click.
     * @param timeoutMessage Message to display if the operation times out.
     */
    public void clickWithTimeout(int buttonId,
                                 String timeoutMessage) {
        ensureActivitySet();
        View button = activity.findViewById(buttonId);
        if (!(button instanceof Button)) {
            throw new IllegalStateException(
                "Provided ID does not correspond to a Button.");
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> clickFuture = executor.submit(() -> {
            button.performClick();
            return null;
        });

        try {
            clickFuture.get(CLICK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            clickFuture.cancel(true);
            fail(timeoutMessage);
        } catch (InterruptedException | ExecutionException e) {
            fail(String.format(
                "Error during button click. \n Button Info: %s \n Exception: %s",
                getViewDetails(button), e.toString()));
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Attempts to click a button with a specific timeout.
     * Provides a view related message for ease of use.
     *
     * @param buttonId The ID of the button to click.
     */
    public void clickWithTimeoutAndDefaultMessage(int buttonId) {
        ensureActivitySet();
        View buttonView = activity.findViewById(buttonId);
        clickWithTimeout(
            buttonId,
            String.format(
                "A timeout occurred while clicking a button.\nButton Details: %s",
                getViewDetails(buttonView)));
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
