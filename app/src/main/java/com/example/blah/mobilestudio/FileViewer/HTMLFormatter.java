package com.example.blah.mobilestudio.FileViewer;

import com.example.blah.mobilestudio.parser.html.HTMLLexer;
import com.example.blah.mobilestudio.parser.html.HTMLParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by Ye He on 18/02/2016.
 */
public class HTMLFormatter extends TextFormatter {

    private HTMLColorScheme htmlColorScheme;

    public HTMLFormatter() {
        setColorScheme(new HTMLLightColourScheme());
    }

    public void setColorScheme(HTMLColorScheme htmlColorScheme) {
        this.htmlColorScheme = htmlColorScheme;
    }

    @Override
    public String highlightSourceCode(String input) {
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(input);
        HTMLLexer htmlLexer = new HTMLLexer(antlrInputStream);
        CommonTokenStream tokens = new CommonTokenStream(htmlLexer);
        HTMLParser htmlParser = new HTMLParser(tokens);
        ParseTree tree = htmlParser.htmlDocument();
        ParseTreeWalker walker = new ParseTreeWalker();
        HTMLHighlightListener extractor = new HTMLHighlightListener(tokens, htmlColorScheme);
        // initiate walk of tree with listener
        walker.walk(extractor, tree);

        // get back ALTERED stream
        String text = extractor.getRewriter().getText();
        return text;
    }
}
