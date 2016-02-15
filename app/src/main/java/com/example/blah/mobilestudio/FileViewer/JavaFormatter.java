package com.example.blah.mobilestudio.FileViewer;

/**
 * Created by Stephen on 15/02/2016.
 * A class that does colour highlighting on the opened files
 */
public class JavaFormatter {
    private static final String GREEN_BOLD = "<font color='#0000ff'><b>";
    private static final String BOLD_CLOSE = "</b></font>";
    private static final String KEYWORD_REGEX = "\\s(import|private|protected|public|new|class|if|else|while|for|try|catch|finally|return)\\s";

    /**
     *
     * @param input - a string of Java code, with text that needs to be highlighted
     * @return -
     */
    public static String highlightKeywords(String input){
        return input.replaceAll(KEYWORD_REGEX, GREEN_BOLD + "\\1" + BOLD_CLOSE);
    }
}
