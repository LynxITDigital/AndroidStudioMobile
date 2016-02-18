package com.example.blah.mobilestudio.FileViewer;

import com.example.blah.mobilestudio.parser.java.JavaLexer;
import com.example.blah.mobilestudio.parser.java.JavaParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by Stephen on 18/02/2016.
 */
public class JavaTextFormatter extends TextFormatter {
    private JavaColorScheme javaColorScheme;

    /**
     * Creating an empty JavaTextFormatter will format the text with the default colourscheme,
     * set as LightColour.
     */
    public JavaTextFormatter() {
        setJavaColorScheme(new LightColour());
    }

    public JavaColorScheme getJavaColorScheme() {
        return javaColorScheme;
    }

    public void setJavaColorScheme(JavaColorScheme javaColorScheme) {
        this.javaColorScheme = javaColorScheme;
    }

    @Override
    public String highlightSourceCode(String input) {
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(input);
        JavaLexer javaLexer = new JavaLexer(antlrInputStream);
        CommonTokenStream tokens = new CommonTokenStream(javaLexer);
        JavaParser javaParser = new JavaParser(tokens);
        ParseTree tree = javaParser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        JavaHighlightListener extractor = new JavaHighlightListener(tokens, getJavaColorScheme());
        // initiate walk of tree with listener
        walker.walk(extractor, tree);

        // get back ALTERED stream
        String text = extractor.getRewriter().getText();

        CommentFormatter commentFormatter = new CommentFormatter();
        String textWithoutComments = commentFormatter.removeComments(text);
        return commentFormatter.replaceComments(textWithoutComments).replaceAll("\n", "<br />\n");
    }

}
