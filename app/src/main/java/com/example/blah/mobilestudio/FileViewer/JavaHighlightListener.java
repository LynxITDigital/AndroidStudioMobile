package com.example.blah.mobilestudio.FileViewer;


import com.example.blah.mobilestudio.parser.java.JavaBaseListener;
import com.example.blah.mobilestudio.parser.java.JavaParser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Ye He on 17/02/2016.
 */
public class JavaHighlightListener extends JavaBaseListener {
    private TokenStreamRewriter rewriter;
    private JavaColorScheme javaColorScheme;

    public JavaHighlightListener(TokenStream tokens, JavaColorScheme color) {
        rewriter = new TokenStreamRewriter(tokens);
        javaColorScheme = color;
    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (isKeyword(tokenType)) {
            rewriter.insertBefore(ctx.start, javaColorScheme.getKeywordPrefix());
            rewriter.insertAfter(ctx.start, javaColorScheme.getKeywordSuffix());
        }
    }

    @Override
    public void enterLiteral(JavaParser.LiteralContext ctx) {
        int tokenType = ctx.getStart().getType();
        if (tokenType == JavaParser.StringLiteral) {
            String text = ctx.start.getText();
            String escapedText = StringEscapeUtils.escapeHtml4(text);

            rewriter.replace(ctx.start, escapedText);
            rewriter.insertBefore(ctx.start, javaColorScheme.getStringPrefix());
            rewriter.insertAfter(ctx.start, javaColorScheme.getStringSuffix());
        }
    }

    @Override
    public void enterAnnotation(JavaParser.AnnotationContext ctx) {
        rewriter.insertBefore(ctx.start, javaColorScheme.getAnnotationPrefix());
        rewriter.insertAfter(ctx.stop, javaColorScheme.getAnnotationSuffix());
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
            rewriter.insertBefore(ctx.start, javaColorScheme.getModifierPrefix());
            rewriter.insertAfter(ctx.start, javaColorScheme.getModifierSuffix());
        }
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        Token token = ctx.getToken(JavaParser.Identifier, 0).getSymbol();
        rewriter.insertBefore(token, javaColorScheme.getMethodNamePrefix());
        rewriter.insertAfter(token, javaColorScheme.getMethodNameSuffix());
    }

    @Override
    public void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {
        rewriter.insertBefore(ctx.start, javaColorScheme.getVardecidPrefix());
        rewriter.insertAfter(ctx.stop, javaColorScheme.getVardecidSuffix());
    }

    public TokenStreamRewriter getRewriter() {
        return rewriter;
    }
}