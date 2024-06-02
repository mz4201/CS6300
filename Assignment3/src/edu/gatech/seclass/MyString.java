package edu.gatech.seclass;

public class MyString implements MyStringInterface {
    String mystring;

    @Override
    //Returns the current string
    public String getString(){
        return mystring;
    }

    @Override
    //Set the value of the current string
    public void setString(String string){
        //check if string is easterEgg
        if (string.equals(easterEgg)){
            throw new IllegalArgumentException();
        }
        //check if string is empty
        if (string.isEmpty()){
            throw new IllegalArgumentException();
        }
        //check if string contains only non-digits/non-alphabetic char

        if (!string.matches(".*[a-zA-Z0-9].*")) {
            throw new IllegalArgumentException();
        }

        this.mystring = string;
    }


    @Override
    //Returns the number of alphabetic words in current string
    public int countAlphabeticWords(){
        if(this.mystring == null){
            throw new NullPointerException();
        }

        //replace non-alphabetic words with space
        String cleanInput = this.mystring.replaceAll("[^a-zA-Z]"," ");
        //split into array
        String[] words = cleanInput.split("\\s+");
        //remove empty string from string array
        int count = 0;
        for (String wordCount : words) {
            if (!wordCount.trim().isEmpty()) {
                count++;
            }
        }
        String[] trimmedWords = new String[count];
        int index = 0;
        for (String word : words) {
            if (!word.trim().isEmpty()) {
                trimmedWords[index++] = word.trim();
            }
        }
        return trimmedWords.length;
    }

    @Override
    public String encrypt(int arg1, int arg2) {
        if (this.mystring == null){
            throw new NullPointerException();
        }
        //check if coPrime
        boolean areCoprime = true;
        for (int i = 2; i <= Math.min(arg1, 62); i++){
            //check if both divisible by i, if it can, it's not coprime
            if (arg1 % i == 0 && 62 % i == 0) {
                areCoprime = false;
                break;
            }
        }

        if (arg1<0 || arg1 >= 62 || !areCoprime){
            throw new IllegalArgumentException();
        }

        if(arg2 <1 || arg2 >= 62){
            throw new IllegalArgumentException();
        }

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

    @Override
    public void convertDigitsToNamesInSubstring(int firstPosition, int finalPosition) {
        if (this.mystring == null){
            throw new NullPointerException();
        }
        if (firstPosition < 1 || firstPosition > finalPosition){
            throw new IllegalArgumentException();
        }
        if (finalPosition > this.mystring.length() ){
            throw new MyIndexOutOfBoundsException();
        }

        String currString = this.mystring.substring(firstPosition-1, finalPosition);
        String[] numberList = {"Zero","One","Two","Three","Four","Five","Six","Seven","Eight","Nine"};
        String[] digitList = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < digitList.length; i++) {
            currString = currString.replaceAll(digitList[i], numberList[i]);
        }
        setString(mystring.substring(0, firstPosition - 1)+currString+mystring.substring(finalPosition));
    }
}
