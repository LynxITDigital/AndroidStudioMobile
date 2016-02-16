package com.example.blah.mobilestudio.FileViewer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stephen on 15/02/2016.
 * A class that does colour highlighting on the opened files
 */
public class JavaFormatter {
    private static final String BLUE_BOLD = "<font color='#00ff00'><b>";
    private static final String BOLD_CLOSE = "</b></font>";
    private static final String KEYWORD_REGEX = "package|import|private|protected|public|new|class|if|else|while|for|try|catch|finally|return";
    private static final String COMMENT_REGEX = "";
    private static final String COMMENT_REPLACEMENT = "#COMMENT";
    private static final String ANNOTATION_REGEX= "";
    private static final String STRING_REGEX = "";
    private static final String STRING_REPLACEMENT = "#STRING";

    /**
     *
     * @param input - a string of Java code, with text that needs to be highlighted
     * @return - the input string with html tags around the relevant higlighted strings
     */
    public static String highlightBlueKeywords(String input){
        Pattern p = Pattern.compile(KEYWORD_REGEX);
        Matcher m = p.matcher(input);
        String returnValue = input;
        // Pick out a relevant keyword
        while(m.find()){
            String candidate = m.group();
            if(isKeyword(candidate)){
                returnValue = m.replaceFirst(BLUE_BOLD + candidate + BOLD_CLOSE);
            }
        }

        return returnValue;
    }

    // Removes all comments from a string and replaces them with the word "COMMENT", while storing those lines
    public static String removeComments(String input){
        return null;
    }

    // Replaces all comments in the text
    public static String replaceComments(String input){
        return null;
    }

    public static String removeStrings(String input){
        return null;
    }

    public static String replaceStrings(String input){
        return null;
    }

    // Highlights all annotations in the text grey
    private static String highlightAnnotations(String input){
        return null;
    }

    private static boolean isKeyword(String candidate) {
        return true;
    }

}
