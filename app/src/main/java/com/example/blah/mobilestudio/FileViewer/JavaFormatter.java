package com.example.blah.mobilestudio.FileViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stephen on 15/02/2016.
 * A class that does colour highlighting on the opened files
 */
public class JavaFormatter {
    private static final String BLUE_BOLD = "<font color='#00ff00'><b>";
    private static final String BOLD_CLOSE = "</b></font>";
    private static final String GREEN_ITALICS = "<font color='2e8b57'><i>";
    private static final String ITALICS_CLOSE = "</i></font>";
    private static final String GREY_TEXT = "<font color='#d3d3d3'>";
    private static final String GREY_CLOSE = "</font>";
    private static final String KEYWORD_REGEX = "package|import|private|protected|public|new|class|if|else|while|for|try|catch|finally|return";
    private static final String SINGLE_LINE_COMMENT_REPLACEMENT = "#SCOMMENT";
    private static final String MULTI_LINE_COMMENT_REPLACEMENT = "#MCOMMENT";
    private static final String ANNOTATION_REGEX= "^(\\s*@[a-zA-Z0-9]+\\s*)+";
    private static final String STRING_REGEX = "\"";
    private static final String STRING_REPLACEMENT = "#STRING";
    private static final String SINGLE_LINE_COMMENT_REGEX = "//.*$";
    private static final String MULTI_LINE_COMMENT_REGEX = "/\\*.*?\\*/";
    // Each string in this list represents a line that is to be replaced by a comment.
    private List<String> singleLineComments;
    private List<String> multiLineComments;
    private List<String> strings;

    public JavaFormatter(){
        singleLineComments = new ArrayList<String>();
        multiLineComments = new ArrayList<String>();
        strings = new ArrayList<String>();
    }

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
    public String highlightComments(String input){
        // Replace all patterns that could potentially be confused for comment replacements
        String returnString = input.replaceAll(SINGLE_LINE_COMMENT_REPLACEMENT, "#/SCOMMENT");
        returnString = returnString.replaceAll(MULTI_LINE_COMMENT_REPLACEMENT, "#/MCOMMENT");
        returnString = returnString.replaceAll(STRING_REPLACEMENT, "#/STRING");
        returnString = replaceMultiLineComments(returnString);
        returnString = replaceSingleLineComments(returnString);
        returnString = replaceStrings(returnString);
        returnString = highlightAnnotations(returnString);

        // Return all the comments back to the text
        for(int i = 0; i < singleLineComments.size(); i++){
            returnString = returnString.replaceFirst(SINGLE_LINE_COMMENT_REPLACEMENT, GREEN_ITALICS + singleLineComments.get(i) + ITALICS_CLOSE);
        }

        for(int i = 0; i < multiLineComments.size(); i++){
            returnString = returnString.replaceFirst(MULTI_LINE_COMMENT_REPLACEMENT, GREEN_ITALICS + multiLineComments.get(i) + ITALICS_CLOSE);
        }

        for(int i = 0; i < strings.size(); i++){
            returnString = returnString.replaceFirst(STRING_REPLACEMENT, GREEN_ITALICS + strings.get(i) + ITALICS_CLOSE);
        }

        // Return all the strings back to the text
        // Return those patterns to their original form
        returnString = returnString.replaceAll("#/SCOMMENT", SINGLE_LINE_COMMENT_REPLACEMENT);
        returnString = returnString.replaceAll("#/MCOMMENT", MULTI_LINE_COMMENT_REPLACEMENT);
        returnString = returnString.replaceAll("#/STRING", STRING_REPLACEMENT);
        return returnString;
    }

    public String removeComments(String input){

    }

    private String replaceSingleLineComments(String input){
        Pattern pattern = Pattern.compile(SINGLE_LINE_COMMENT_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            singleLineComments.add(matcher.group());
        }

        return matcher.replaceAll(SINGLE_LINE_COMMENT_REPLACEMENT);
    }

    private String replaceMultiLineComments(String input){
        Pattern pattern = Pattern.compile(MULTI_LINE_COMMENT_REGEX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            multiLineComments.add(matcher.group());
        }

        return matcher.replaceAll(MULTI_LINE_COMMENT_REPLACEMENT);
    }

    private String replaceStrings(String input){
        Pattern pattern = Pattern.compile(STRING_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            strings.add(matcher.group());
        }

        return matcher.replaceAll(STRING_REPLACEMENT);
    }

    // Highlights all annotations in the text grey
    private static String highlightAnnotations(String input){
        Pattern pattern = Pattern.compile(ANNOTATION_REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        String returnString = input;
        while(matcher.find()){
            String token = matcher.group();
            String replacement = GREY_TEXT + matcher.group() + GREY_CLOSE;
            returnString = returnString.replace(token, replacement);
        }

        return returnString;
    }

    private static boolean isKeyword(String candidate) {
        return true;
    }

}
