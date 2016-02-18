package com.example.blah.mobilestudio.FileViewer;

/**
 * Created by yehe on 18/02/2016.
 */
public class TextFormatter {
    public String highlightSourceCode(String text) {
        return text.replaceAll("\n", "<br />\n");
    }
}
