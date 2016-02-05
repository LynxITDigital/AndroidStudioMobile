package com.example.blah.mobilestudio.Resizer;

/**
 * Created by mattgale on 5/02/2016.
 */
public class SizableRegion {
    float minX, minY, maxX, maxY;

    public SizableRegion(float minX, float maxX, float minY, float maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
    }
}
