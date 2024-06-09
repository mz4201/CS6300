package edu.gatech.seclass.sdpencryptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText inputTextID;
    private EditText multiplierInputID;
    private EditText adderInputID;
    private Button cipherButtonID;
    private TextView resultTextID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextID = findViewById(R.id.inputTextID);
        multiplierInputID = findViewById(R.id.multiplierInputID);
        adderInputID = findViewById(R.id.adderInputID);
        cipherButtonID = findViewById(R.id.cipherButtonID);
        resultTextID = findViewById(R.id.resultTextID);
    }

    public void handleClick(View view){
        //track errors
        boolean hasError = false;

        //assign input text
        String value = inputTextID.getText().toString();

        //if input text is empty, number-less or letter-less, throw error
        if (value.isEmpty() || !value.matches(".*[a-zA-Z0-9].*")) {
            inputTextID.setError("Invalid Input Text");
            hasError = true;
        }

        //assign multiplier, input as string then convert to int
        String tempMultiplier = multiplierInputID.getText().toString();
        int multiplier = Integer.parseInt(tempMultiplier);
        boolean areCoprime = true;
        for (int i = 2; i <= Math.min(multiplier, 62); i++) {
            //check if both divisible by i, if it can, it's not coprime
            if (multiplier % i == 0 && 62 % i == 0) {
                areCoprime = false;
                break;
            }
        }
        if (!areCoprime || multiplier < 1){
            multiplierInputID.setError("Invalid Multiplier Input");
            hasError = true;
        }

        //assign adder, input as string then convert to int
        String tempAdder = adderInputID.getText().toString();
        int adder = Integer.parseInt(tempAdder);
        if (adder < 1) {
            adderInputID.setError("Invalid Adder Input");
            hasError = true;
        }

        //define result
        String result = encrypt(value, multiplier, adder);

        if (!hasError) {
            //no error, display result
            resultTextID.setText(result);
        }else {
            //if has error, clear output
            resultTextID.setText("");
        }
    }

    private String encrypt(String mystring, int arg1, int arg2) {

        char[] temp = mystring.toCharArray();

        // Building the encoded character list
        char[] encodedChar = new char[62];
        int indexEncodedChar = 0;
        // Add 0 - 9
        for (char digit = '0'; digit <= '9'; digit++) {
            encodedChar[indexEncodedChar++] = digit;
        }
        // Add Aa-Zz
        for (char uppercase = 'A'; uppercase <= 'Z'; uppercase++) {
            encodedChar[indexEncodedChar++] = uppercase;
            encodedChar[indexEncodedChar++] = Character.toLowerCase(uppercase);
        }

        // Formula loop
        for (int i = 0; i < temp.length; i++) {
            if (Character.isLetter(temp[i]) || Character.isDigit(temp[i])) {
                // Get the index of that character
                int index = -1;
                for (int j = 0; j < encodedChar.length; j++) {
                    if (encodedChar[j] == temp[i]) {
                        index = j;
                        break;
                    }
                }
                int newIndex = (index * arg1 + arg2) % 62;
                temp[i] = encodedChar[newIndex];
            }
        }
        return new String(temp);
    }

}
