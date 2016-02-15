package com.example.blah.mobilestudio.fileViewer;

import com.example.blah.mobilestudio.FileViewer.JavaFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * Created by Stephen on 15/02/2016.
 */
@RunWith(JUnit4.class)
public class JavaFormatterTest {
    @Test
    public void TextHighlightingTest1(){
        String testInput = "if";
        assertEquals("<font color='#0000ff'><b>if</b></font>", JavaFormatter.highlightKeywords(testInput));
    }
}
