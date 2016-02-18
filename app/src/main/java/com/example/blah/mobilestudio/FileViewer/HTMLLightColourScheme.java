package com.example.blah.mobilestudio.FileViewer;

/**
 * Created by Ye He on 18/02/2016.
 */
public class HTMLLightColourScheme extends HTMLColorScheme {
    private static final String TAG_NAME_FONT_SUFFIX = "</font>";
    private static final String TAG_NAME_FONT_PREFIX = "<font color='2e8b57'>";

    @Override
    public String getTagNamePrefix() {
        return TAG_NAME_FONT_PREFIX;
    }

    @Override
    public String getTagNameSuffix() {
        return TAG_NAME_FONT_SUFFIX;
    }
}
