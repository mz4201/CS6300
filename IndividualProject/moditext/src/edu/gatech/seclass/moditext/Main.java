package edu.gatech.seclass.moditext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;


public class Main {

    private static final String separator = System.lineSeparator();
    private static boolean error = false;

    public static void main(String[] args) {
        //read arguments
        List<String> arguments = Arrays.asList(args);
        //check if no arguments, or if no file added
        if (arguments.isEmpty() || !arguments.get(arguments.size() - 1).endsWith(".txt")) {
            usage();
            error = true;
            System.out.println("usage 1");
        }
        //read file
        String fileName = arguments.get(arguments.size() - 1);
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            // Check if the last line is not a line separator
            if (!fileContent.endsWith(separator)) {
                usage();
                error = true;
                System.out.println("usage 2");
            }
        } catch (IOException e) {
            //file not found, file cannot be read, network/system/disk issues etc.
            e.printStackTrace();
            usage();
            error = true;
            System.out.println("usage 3");
        }

        //check the options for errors and put in order
        List<String> originalOptions = new ArrayList<>(arguments.subList(0, arguments.size() - 1));
        List<String> options = optionsChecker(originalOptions);

        // Process the options
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);

            if (option.equals("-k")) {
                String substring = options.get(++i);
                fileContent = keepLines(fileContent, substring);

            } else if (option.equals("-p")) {
                char symbol = options.get(++i).charAt(0);
                int maxPadding = Integer.parseInt(options.get(++i));
                fileContent = padBeginning(fileContent, symbol, maxPadding);

            } else if (option.equals("-t")) {
                int num = Integer.parseInt(options.get(++i));
                fileContent = trimLines(fileContent, num);

            } else if (option.equals("-f")) {
                String style = options.get(++i);
                String subStr = options.get(++i);
                boolean global = options.contains("-g");
                fileContent = formatText(fileContent, style, subStr, global);

            } else if (option.equals("-r")) {
                fileContent = reverseLines(fileContent);

            }
        }
        //System.out.println(error);

            // Output the final content
            System.out.println(fileContent);

    }

    //this is a tool to reorganize the options.
    //- identify errors in the option parameters (out of range, invalid options, invalid parameters, etc.)
    //- de-deduplicate by keeping the last repeated option
    //- return the options in a list as specified order to be run
    private static List<String> optionsChecker(List<String> options) {
        //valid options in order
        List<String> validOptions = Arrays.asList("-k", "-p", "-t", "-g", "-f", "-r");

        Map<String, List<String>> optionsMap = new LinkedHashMap<>();
        boolean hasPadOption = false;
        boolean hasTrimOption = false;
        if(options.isEmpty()){
            return Collections.emptyList();
        }
        //for each input options
        for (int i = 0; i < options.size(); i++) {
            //initiate current option
            String option = options.get(i);

            //check if it's a valid option

            if (!validOptions.contains(option)) {
                usage();
                error = true;
                System.out.println("usage 4");
            }

            //initiate parameter list
            List<String> params = new ArrayList<>();

            switch (option) {
                //-p <symbol> <maxPadding>
                case "-p":
                    //cannot have -t
                    if (hasTrimOption) {
                        usage();
                        error = true;
                        System.out.println("usage 5");
                    }
                    hasPadOption = true;
                    //check if param is char and int
                    if (i + 2 >= options.size() || options.get(i + 1).length() != 1 || !isInteger(options.get(i + 2))) {
                        usage();
                        error = true;
                        System.out.println("usage 6");
                    }
                    //check if maxPadding is within range
                    int maxPadding = Integer.parseInt(options.get(i + 2));
                    if (maxPadding < 1 || maxPadding > 100) {
                        usage();
                        error = true;
                        System.out.println("usage 7");
                    }
                    //add parameter to list
                    params.add(options.get(i + 1));
                    params.add(options.get(i + 2));
                    //skip index for padding param
                    i += 2;
                    break;
                //-t <num>
                case "-t":
                    //cannot have -p
                    if (hasPadOption) {
                        usage();
                        error = true;
                        System.out.println("usage 8");
                    }
                    hasTrimOption = true;
                    //check if param is int
                    if (i + 1 >= options.size() || !isInteger(options.get(i + 1))) {
                        usage();
                        error = true;
                        System.out.println("usage 9");
                    }
                    //check if num is within range
                    int num = Integer.parseInt(options.get(i + 1));
                    if (num < 0 || num > 100) {
                        usage();
                        error = true;
                        System.out.println("usage 10");
                    }
                    //add parameter to list
                    params.add(options.get(i + 1));
                    //skip next index for trim param
                    i++;
                    break;
                //-k <string>
                case "-k":
                    //check if there is substring param
                    if (i + 1 >= options.size()) {
                        usage();
                        error = true;
                        System.out.println("usage 11");
                    }
                    //add parameter to list
                    params.add(options.get(i + 1));
                    //skip next index for keep line param
                    i++;
                    break;
                //-f <style> <substring>
                //-g
                case "-f":
                    if (i + 2 >= options.size()) {
                        usage();
                        error = true;
                        System.out.println("usage 12");
                    }
                    //check if style param is correct
                    String style = options.get(i + 1);
                    if (!style.equals("bold") && !style.equals("italic") && !style.equals("code")) {
                        usage();
                        error = true;
                        System.out.println("usage 13");
                    }
                    //check if substring is not empty
                    if (options.get(i + 2).isEmpty()) {
                        usage();
                        error = true;
                        System.out.println("usage 14");
                    }
                    //add parameter to list
                    params.add(style);
                    params.add(options.get(i + 2));
                    //skip next index for format param
                    i += 2;
                    break;
                //-g
                case "-g":
                    //if exists without -f, error out
                    if (!options.contains("-f")) {
                        usage();
                        error = true;
                        System.out.println("usage 15");
                    }
                    break;
            }

            //add current option and its parameter to optionsMap
            optionsMap.put(option, params);
        }

        // Create a list of ordered options
        List<String> orderedOptions = new ArrayList<>();
        //loop through validOptions in order
        //if the option is present in optionsMap, add it and its parameter in orderedOptions.
        for (String opt : validOptions) {
            if (optionsMap.containsKey(opt)) {
                orderedOptions.add(opt);
                orderedOptions.addAll(optionsMap.get(opt));
            }
        }

        return orderedOptions;

    }

    //check if it's valid integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //keep lines
    public static String keepLines(String content, String keepLines) {

        //split the content into lines
        String[] lines = content.split(separator);

        // store filtered lines
        StringBuilder result = new StringBuilder();

        // For each line
        for (String line : lines) {
            // Check if the line contains the substring
            if (line.contains(keepLines)) {
                // If so, add the line to the result with a separator
                if (result.length() > 0) {
                    result.append(separator); // Append separator only if it's not the first line
                }
                result.append(line);
            }
        }

        return result.toString();
    }

    //pad beginning
    public static String padBeginning(String content, char symbol, int maxPadding) {

        //split the content into lines
        String[] lines = content.split(separator);

        // store formatted lines
        StringBuilder result = new StringBuilder();

        // for each line
        for (String line : lines) {
            // calculate how much char to add to reach maxPadding
            int paddingLength = Math.max(0, maxPadding - line.length());

            // Create the new line with char
            StringBuilder paddedLine = new StringBuilder();
            for (int i = 0; i < paddingLength; i++) {
                paddedLine.append(symbol);
            }
            paddedLine.append(line);

            // add the new line to result
            result.append(paddedLine.toString()).append(separator);

        }
        // Remove the last separator
        if (result.length() > 0) {
            result.setLength(result.length() - separator.length());
        }
        // Return the final padded content
        return result.toString();
    }

    //trim lines
    public static String trimLines(String content, int num) {
        //split the content into lines
        String[] lines = content.split(separator);

        //iterate through each line and trim
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > num) {
                lines[i] = lines[i].substring(0, num);
            }
        }

        //join the lines back
        //String result = String.join(separator, lines);

        /*
        // Ensure that the result ends with a line separator
        if (!result.endsWith(separator)) {
            result += separator;
        }

         */

        return String.join(separator, lines);
    }

    //format text ;global replace
    public static String formatText(String content, String style, String substring, boolean global) {
        //split the content into lines
        String[] lines = content.split(separator);

        String styledSubstring = null;

        // Escape special characters in the substring
        String escapedSubstring = Pattern.quote(substring);

        // set up the styles
        switch (style.toLowerCase()) {
            case "bold":
                styledSubstring = "**" + substring + "**";
                break;
            case "italic":
                styledSubstring = "*" + substring + "*";
                break;
            case "code":
                styledSubstring = "`" + substring + "`";
                break;
            default:
                // Invalid style; print usage and exit
                usage();
                error = true;
                System.out.println("usage 16");
        }

        // iterate through each line
        for (int i = 0; i < lines.length; i++) {
            if (global) {
                //replace all occurrences of the substring if global is true
                lines[i] = lines[i].replaceAll(escapedSubstring, styledSubstring);
            } else {
                //replace only the first occurrence of the substring
                lines[i] = lines[i].replaceFirst(escapedSubstring, styledSubstring);
            }
        }

        //join the lines back
        //String result = String.join(separator, lines);
/*
        // Ensure that the result ends with a line separator
        if (!result.endsWith(separator)) {
            result += separator;
        }

 */
        return String.join(separator, lines);
    }

    //reverse lines
    public static String reverseLines(String content) {
        //split the content into lines
        String[] lines = content.split(separator);

        //store formatted lines
        StringBuilder result = new StringBuilder();

        //iterate through each line and add back in reverse order
        for (int i = lines.length - 1; i >= 0; i--) {
            result.append(lines[i]);
            // Append the line separator if it's not the last line
            if (i > 0) {
                result.append(separator);
            }
        }
        /*
        // Ensure that the result ends with a line separator
        if (!result.toString().endsWith(separator)) {
            result.append(separator);
        }

         */
        // Return the reversed content as a single string
        return result.toString();
    }

    private static void usage() {
        System.err.println("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE"+separator);
    }
}

