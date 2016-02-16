package com.example.blah.mobilestudio.FileViewer;

import com.example.blah.mobilestudio.FileViewer.JavaFormatter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Stephen on 15/02/2016.
 */
public class JavaFormatterTest {
    @Test
    public void TextHighlightingTest1(){
        String testInput = "if";
        assertEquals("<font color='#0000ff'><b>if</b></font>", JavaFormatter.highlightGreenKeywords(testInput));
    }
}
