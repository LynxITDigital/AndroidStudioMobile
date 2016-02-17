package com.example.blah.mobilestudio.FileViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stephen on 17/02/2016.
 */
public class CommentFormatter {

    private static final String GREEN_ITALICS = "<font color='2e8b57'><i>";
    private static final String ITALICS_CLOSE = "</i></font>";
    private static final String SINGLE_LINE_COMMENT_REPLACEMENT = "#SCOMMENT";
    private static final String MULTI_LINE_COMMENT_REPLACEMENT = "#MCOMMENT";
    private static final String SINGLE_LINE_COMMENT_REGEX = "//.*$";
    private static final String MULTI_LINE_COMMENT_REGEX = "/\\*.*?\\*/";

    // Each string in these lists represents a line that is to be replaced by a comment.
    private List<String> singleLineComments;
    private List<String> multiLineComments;

    public CommentFormatter() {
        singleLineComments = new ArrayList<>();
        multiLineComments = new ArrayList<>();
    }

    public String removeComments(String input) {
        // Replace all patterns that could potentially be confused for comment replacements
        String returnString = input.replaceAll(SINGLE_LINE_COMMENT_REPLACEMENT, "#/SCOMMENT");
        returnString = returnString.replaceAll(MULTI_LINE_COMMENT_REPLACEMENT, "#/MCOMMENT");
        returnString = replaceMultiLineComments(returnString);
        returnString = replaceSingleLineComments(returnString);

        return returnString;
    }

    private String replaceSingleLineComments(String input) {
        Pattern pattern = Pattern.compile(SINGLE_LINE_COMMENT_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            singleLineComments.add(matcher.group());
        }

        return matcher.replaceAll(SINGLE_LINE_COMMENT_REPLACEMENT);
    }

    private String replaceMultiLineComments(String input) {
        Pattern pattern = Pattern.compile(MULTI_LINE_COMMENT_REGEX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            multiLineComments.add(matcher.group());
        }

        return matcher.replaceAll(MULTI_LINE_COMMENT_REPLACEMENT);
    }

    public String replaceComments(String input) {

        String returnString = input;
        // Return all the comments back to the text
        for (int i = 0; i < singleLineComments.size(); i++) {
            returnString = returnString.replaceFirst(SINGLE_LINE_COMMENT_REPLACEMENT, GREEN_ITALICS + singleLineComments.get(i) + ITALICS_CLOSE);
        }

        for (int i = 0; i < multiLineComments.size(); i++) {
            returnString = returnString.replaceFirst(MULTI_LINE_COMMENT_REPLACEMENT, GREEN_ITALICS + multiLineComments.get(i) + ITALICS_CLOSE);
        }

        // Return all the strings back to the text
        // Return those patterns to their original form
        returnString = returnString.replaceAll("#/SCOMMENT", SINGLE_LINE_COMMENT_REPLACEMENT);
        returnString = returnString.replaceAll("#/MCOMMENT", MULTI_LINE_COMMENT_REPLACEMENT);
        return returnString;
    }

}