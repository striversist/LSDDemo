package com.tc.tar;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.debug.DebugVisualizer;
import org.rajawali3d.renderer.Renderer;

/**
 * Created by aarontang on 2017/4/6.
 */

public class LSDRenderer extends Renderer {

    public LSDRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        drawGrid();
        ArcballCamera arcball = new ArcballCamera(mContext, ((MainActivity)mContext).getView());
        arcball.setPosition(4, 4, 4);
        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
    }

    private void drawGrid() {
        DebugVisualizer debugViz = new DebugVisualizer(this);
        debugViz.addChild(new LSDGridFloor());
        getCurrentScene().addChild(debugViz);
    }

    private void drawFrustum() {

    }
}
