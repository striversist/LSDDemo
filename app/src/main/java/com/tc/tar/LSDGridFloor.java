package com.tc.tar;

import android.graphics.Color;
import android.opengl.GLES20;

import org.rajawali3d.debug.DebugObject3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;

import java.util.Stack;

/**
 * Created by aarontang on 2017/4/6.
 */

public class LSDGridFloor extends DebugObject3D {
    private float mSize;
    private int mNumLines;

    public LSDGridFloor() {
        this(10);
    }

    public LSDGridFloor(float size) {
        this(size, Color.WHITE, 1, 20);
    }

    public LSDGridFloor(float size, int color, int lineThickness, int numLines) {
        super(color, lineThickness);
        mSize = size;
        mNumLines = numLines;
        createGridFloor();
    }

    private void createGridFloor() {
        mPoints = new Stack<>();
        double dGridInterval = 0.1;

        double dMin = -100.0 * dGridInterval;
        double dMax = 100.0 * dGridInterval;

        double height = -4;

        int color = 0;

        for(int x = -10; x <= 10; x += 1)
        {
            if(x == 0)
                color = 0xffffff;
            else
                color = 0x4c4c4c;
            mPoints.add(new Vector3((double) x * 10 * dGridInterval, dMin, height));
            mPoints.add(new Vector3((double) x * 10 * dGridInterval, dMax, height));
        }

        for(int y = -10; y <= 10; y += 1)
        {
            if(y == 0)
                color = 0xffffff;
            else
                color = 0x4c4c4c;
            mPoints.add(new Vector3(dMin, (double) y * 10 * dGridInterval, height));
            mPoints.add(new Vector3(dMax, (double) y * 10 * dGridInterval, height));
        }

        dMin = -10.0 * dGridInterval;
        dMax = 10.0 * dGridInterval;

        for(int x = -10; x <= 10; x++)
        {
            if(x == 0)
                color = 0xffffff;
            else
                color = 0x808080;
            mPoints.add(new Vector3((double) x * dGridInterval, dMin, height));
            mPoints.add(new Vector3((double) x * dGridInterval, dMax, height));
        }

        for(int y = -10; y <= 10; y++)
        {
            if(y == 0)
                color = 0xffffff;
            else
                color = 0x808080;
            mPoints.add(new Vector3(dMin, (double) y * dGridInterval, height));
            mPoints.add(new Vector3(dMax, (double) y * dGridInterval, height));
        }

        mPoints.add(new Vector3(0, 0, height));
        mPoints.add(new Vector3(1, 0, height));

        mPoints.add(new Vector3(0, 0, height));
        mPoints.add(new Vector3(0, 1, height));

        mPoints.add(new Vector3(0, 0, height));
        mPoints.add(new Vector3(0, 0, height + 1));

        setMaterial(new Material());
        init(true);
        setDrawingMode(GLES20.GL_LINES);
    }
}
