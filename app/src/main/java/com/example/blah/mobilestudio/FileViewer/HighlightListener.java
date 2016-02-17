package com.example.blah.mobilestudio.FileViewer;


import com.example.blah.mobilestudio.parser.JavaBaseListener;
import com.example.blah.mobilestudio.parser.JavaParser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by yehe on 17/02/2016.
 */
public class HighlightListener extends JavaBaseListener {
    public static final String STRING_FONT_SUFFIX = "</font>";
    public static final String STRING_FONT_PREFIX = "<font color='2e8b57'>";
    public static final String ANNOTATION_FONT_PREFIX = "<font color='#BBB529'><b>";
    public static final String ANNOTATION_FONT_SUFFIX = "</b></font>";
    public static final String KEYWORD_FONT_PREFIX = "<font color='#0000ff'><b>";
    public static final String KEYWORD_FONT_SUFFIX = "</b></font>";
    private static final String MODIFIER_FONT_PREFIX = "<font color='#CC7832'>";
    private static final String MODIFIER_FONT_SUFFIX = "</font>";
    private static final String METHOD_NAME_FONT_PREFIX = "<font color='#FFC66D'>";
    private static final String METHOD_NAME_FONT_SUFFIX = "</font>";
    private static final String VAR_DECL_ID_FONT_PREFIX = "<font color='#824892'>";;
    private static final String VAR_DECL_ID_FONT_SUFFIX = "</font>";;
    TokenStreamRewriter rewriter;

    public HighlightListener(TokenStream tokens) {
        rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (isKeyword(tokenType)) {
            rewriter.insertBefore(ctx.start, KEYWORD_FONT_PREFIX);
            rewriter.insertAfter(ctx.start, KEYWORD_FONT_SUFFIX);
        }
    }

    @Override
    public void enterLiteral(JavaParser.LiteralContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (tokenType == JavaParser.StringLiteral) {
            String text = ctx.start.getText();
            String escapedText = StringEscapeUtils.escapeHtml4(text);

            rewriter.replace(ctx.start, escapedText);
            rewriter.insertBefore(ctx.start, STRING_FONT_PREFIX);
            rewriter.insertAfter(ctx.start, STRING_FONT_SUFFIX);
        }
    }

    @Override
    public void enterAnnotation(JavaParser.AnnotationContext ctx) {
        rewriter.insertBefore(ctx.start, ANNOTATION_FONT_PREFIX);
        rewriter.insertAfter(ctx.stop, ANNOTATION_FONT_SUFFIX);
    }

    private boolean isKeyword(int tokenType) {
        return tokenType == JavaParser.IF || tokenType == JavaParser.FOR
                || tokenType == JavaParser.WHILE || tokenType == JavaParser.DO
                || tokenType == JavaParser.TRY || tokenType == JavaParser.SWITCH
                || tokenType == JavaParser.SYNCHRONIZED || tokenType == JavaParser.RETURN
                || tokenType == JavaParser.THROW || tokenType == JavaParser.BREAK
                || tokenType == JavaParser.CONTINUE;
    }

    @Override
    public void enterClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (tokenType != JavaParser.RULE_annotation) {
            rewriter.insertBefore(ctx.start, MODIFIER_FONT_PREFIX);
            rewriter.insertAfter(ctx.start, MODIFIER_FONT_SUFFIX);
        }
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        Token token = ctx.getToken(JavaParser.Identifier, 0).getSymbol();
        rewriter.insertBefore(token, METHOD_NAME_FONT_PREFIX);
        rewriter.insertAfter(token, METHOD_NAME_FONT_SUFFIX);
    }

    @Override
    public void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        rewriter.insertBefore(ctx.start, VAR_DECL_ID_FONT_PREFIX);
        rewriter.insertAfter(ctx.stop, VAR_DECL_ID_FONT_SUFFIX);
    }
}