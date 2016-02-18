package com.example.blah.mobilestudio.FileViewer;

/**
 * Created by Stephen on 18/02/2016.
 * A light colour scheme initially created by Yehe.
 */
public class LightColour extends JavaColorScheme {
    private static final String STRING_FONT_SUFFIX = "</font>";
    private static final String STRING_FONT_PREFIX = "<font color='2e8b57'>";
    private static final String ANNOTATION_FONT_PREFIX = "<font color='#BBB529'><b>";
    private static final String ANNOTATION_FONT_SUFFIX = "</b></font>";
    private static final String KEYWORD_FONT_PREFIX = "<font color='#0000ff'><b>";
    private static final String KEYWORD_FONT_SUFFIX = "</b></font>";
    private static final String MODIFIER_FONT_PREFIX = "<font color='#CC7832'>";
    private static final String MODIFIER_FONT_SUFFIX = "</font>";
    private static final String METHOD_NAME_FONT_PREFIX = "<font color='#FFC66D'>";
    private static final String METHOD_NAME_FONT_SUFFIX = "</font>";
    private static final String VAR_DECL_ID_FONT_PREFIX = "<font color='#824892'>";
    private static final String VAR_DECL_ID_FONT_SUFFIX = "</font>";

    @Override
    public String getStringPrefix() {
        return STRING_FONT_PREFIX;
    }

    @Override
    public String getStringSuffix() {
        return STRING_FONT_SUFFIX;
    }

    @Override
    public String getAnnotationPrefix() {
        return ANNOTATION_FONT_PREFIX;
    }

    @Override
    public String getAnnotationSuffix() {
        return ANNOTATION_FONT_SUFFIX;
    }

    @Override
    public String getKeywordPrefix() {
        return KEYWORD_FONT_PREFIX;
    }

    @Override
    public String getKeywordSuffix() {
        return KEYWORD_FONT_SUFFIX;
    }

    @Override
    public String getModifierPrefix() {
        return MODIFIER_FONT_PREFIX;
    }

    @Override
    public String getModifierSuffix() {
        return MODIFIER_FONT_SUFFIX;
    }

    @Override
    public String getMethodNamePrefix() {
        return METHOD_NAME_FONT_PREFIX;
    }

    @Override
    public String getMethodNameSuffix() {
        return METHOD_NAME_FONT_SUFFIX;
    }

    @Override
    public String getVardecidPrefix() {
        return VAR_DECL_ID_FONT_PREFIX;
    }

    @Override
    public String getVardecidSuffix() {
        return VAR_DECL_ID_FONT_SUFFIX;
    }
}
