package com.example.blah.mobilestudio.FileViewer;


import com.example.blah.mobilestudio.parser.JavaBaseListener;
import com.example.blah.mobilestudio.parser.JavaParser;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

/**
 * Created by yehe on 17/02/2016.
 */
public class HighlightListener extends JavaBaseListener {
    TokenStreamRewriter rewriter;

    public HighlightListener(TokenStream tokens) {
        rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (tokenType == JavaParser.IF) {
            rewriter.insertAfter(ctx.start, "</b></font>");
            rewriter.insertBefore(ctx.start, "<font color='#0000ff'><b>");
        }
//        else if (tokenType == JavaParser.COMMENT || tokenType == JavaParser.LINE_COMMENT) {
//            rewriter.insertAfter(ctx.start, "</i></font>");
//            rewriter.insertBefore(ctx.start, "<font color='2e8b57'><i>");
//        }
    }

}