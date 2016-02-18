package com.example.blah.mobilestudio.FileViewer;

import com.example.blah.mobilestudio.parser.html.HTMLParser;
import com.example.blah.mobilestudio.parser.html.HTMLParserBaseListener;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

/**
 * Created by Ye He on 18/02/2016.
 */
public class HTMLHighlightListener extends HTMLParserBaseListener {
    private TokenStreamRewriter rewriter;
    private HTMLColorScheme htmlColorScheme;

    public HTMLHighlightListener(CommonTokenStream tokens, HTMLColorScheme htmlColorScheme) {
        rewriter = new TokenStreamRewriter(tokens);
        this.htmlColorScheme = htmlColorScheme;
    }

    public TokenStreamRewriter getRewriter() {
        return rewriter;
    }

    @Override
    public void enterHtmlTagName(HTMLParser.HtmlTagNameContext ctx) {
        rewriter.insertBefore(ctx.start, htmlColorScheme.getTagNamePrefix());
        rewriter.insertAfter(ctx.start, htmlColorScheme.getTagNameSuffix());
    }
}
