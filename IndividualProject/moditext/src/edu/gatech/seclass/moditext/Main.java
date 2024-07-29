package edu.gatech.seclass.moditext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    private static final String separator = System.lineSeparator();
    public static boolean errorStat = false;
    public static boolean  emptyOutput = false;

    public static void main(String[] args) {
        errorStat = false;
        emptyOutput = false;
        // Read arguments
        List<String> arguments = Arrays.asList(args);

        // Check if no arguments, or if no file added
        if (arguments.isEmpty() || !arguments.get(arguments.size() - 1).endsWith(".txt")) {
            usage();
            return;
        }

        if (arguments.size() == 1 && arguments.get(0).endsWith(".txt")) {
            emptyOutput = true;
        }

        // Read file
        String fileName = arguments.get(arguments.size() - 1);
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            // Check if the last line is not a line separator
            if (!fileContent.isEmpty() && !fileContent.endsWith(separator)) {
                usage();
                return;
            }
        } catch (IOException e) {
            // Handle file not found or other IO exceptions
            usage();
            return;
        }

        // Check the options for errors and put in order
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
        if (!errorStat & !emptyOutput) {
            // Output the final content
            System.out.println(fileContent);
        }
    }

    // Method to reorganize the options, check for errors, and return ordered options
    private static List<String> optionsChecker(List<String> options) {
        // Valid options in order
        List<String> validOptions = Arrays.asList("-k", "-p", "-t", "-g", "-f", "-r");

        Map<String, List<String>> optionsMap = new LinkedHashMap<>();
        boolean hasPadOption = false;
        boolean hasTrimOption = false;

        if (options.isEmpty()) {
            return Collections.emptyList();
        }

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);

            if (!validOptions.contains(option)) {
                usage();
                return Collections.emptyList();
            }

            List<String> params = new ArrayList<>();

            switch (option) {
                case "-p":
                    if (hasTrimOption) {
                        usage();
                        return Collections.emptyList();
                    }
                    hasPadOption = true;
                    if (i + 2 >= options.size() || options.get(i + 1).length() != 1 || !isInteger(options.get(i + 2))) {
                        usage();
                        return Collections.emptyList();
                    }
                    int maxPadding = Integer.parseInt(options.get(i + 2));
                    if (maxPadding < 1 || maxPadding > 100) {
                        usage();
                        return Collections.emptyList();
                    }
                    params.add(options.get(i + 1));
                    params.add(options.get(i + 2));
                    i += 2;
                    break;
                case "-t":
                    if (hasPadOption) {
                        usage();
                        return Collections.emptyList();
                    }
                    hasTrimOption = true;
                    if (i + 1 >= options.size() || !isInteger(options.get(i + 1))) {
                        usage();
                        return Collections.emptyList();
                    }
                    int num = Integer.parseInt(options.get(i + 1));
                    if (num < 0 || num > 100) {
                        usage();
                        return Collections.emptyList();
                    }
                    params.add(options.get(i + 1));
                    i++;
                    break;
                case "-k":
                    if (i + 1 >= options.size()) {
                        usage();
                        return Collections.emptyList();
                    }
                    params.add(options.get(i + 1));
                    i++;
                    break;
                case "-f":
                    if (i + 2 >= options.size()) {
                        usage();
                        return Collections.emptyList();
                    }
                    String style = options.get(i + 1);
                    if (!style.equals("bold") && !style.equals("italic") && !style.equals("code")) {
                        usage();
                        return Collections.emptyList();
                    }
                    if (options.get(i + 2).isEmpty()) {
                        usage();
                        return Collections.emptyList();
                    }
                    params.add(style);
                    params.add(options.get(i + 2));
                    i += 2;
                    break;
                case "-g":
                    if (!options.contains("-f")) {
                        usage();
                        return Collections.emptyList();
                    }
                    break;
            }

            optionsMap.put(option, params);
        }

        List<String> orderedOptions = new ArrayList<>();
        for (String opt : validOptions) {
            if (optionsMap.containsKey(opt)) {
                orderedOptions.add(opt);
                orderedOptions.addAll(optionsMap.get(opt));
            }
        }

        return orderedOptions;
    }

    // Check if it's a valid integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Keep lines containing the specified substring
    public static String keepLines(String content, String keepLines) {
        if (keepLines.length() > content.length()) {
            emptyOutput = true; // Return an empty result
            return "";
        }
        String[] lines = content.split(separator);
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (line.contains(keepLines)) {
                if (result.length() > 0) {
                    result.append(separator);
                }
                result.append(line);
            }
        }
        // Ensure the result is empty if no lines matched
        return result.toString();
    }

    // Pad the beginning of lines with the specified symbol up to maxPadding
    public static String padBeginning(String content, char symbol, int maxPadding) {
        String[] lines = content.split(separator);
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            int paddingLength = Math.max(0, maxPadding - line.length());
            StringBuilder paddedLine = new StringBuilder();
            for (int i = 0; i < paddingLength; i++) {
                paddedLine.append(symbol);
            }
            paddedLine.append(line);
            result.append(paddedLine.toString()).append(separator);
        }
        if (result.length() > 0) {
            result.setLength(result.length() - separator.length());
        }
        return result.toString();
    }

    // Trim lines to the specified length
    public static String trimLines(String content, int num) {
        String[] lines = content.split(separator);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > num) {
                lines[i] = lines[i].substring(0, num);
            }
        }
        return String.join(separator, lines);
    }

    // Format text with the specified style and substring, optionally globally
    public static String formatText(String content, String style, String substring, boolean global) {
        String[] lines = content.split(separator);
        String styledSubstring = null;
        String escapedSubstring = Pattern.quote(substring);

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
                usage();
                return content;
        }

        for (int i = 0; i < lines.length; i++) {
            if (global) {
                lines[i] = lines[i].replaceAll(escapedSubstring, styledSubstring);
            } else {
                lines[i] = lines[i].replaceFirst(escapedSubstring, styledSubstring);
            }
        }

        return String.join(separator, lines);
    }

    // Reverse the order of lines
    public static String reverseLines(String content) {
        String[] lines = content.split(separator);
        StringBuilder result = new StringBuilder();
        for (int i = lines.length - 1; i >= 0; i--) {
            result.append(lines[i]);
            if (i > 0) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    // Print usage information and set errorStat to true
    private static void usage() {
        errorStat = true;  // Set errorStat to true whenever usage is called
        System.err.println("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE");
    }
}
