package com.tc.tar;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.debug.DebugVisualizer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

/**
 * Created by aarontang on 2017/4/6.
 */

public class LSDRenderer extends Renderer {
    public static final String TAG = LSDRenderer.class.getSimpleName();
    private float intrinsics[];
    private int resolution[];
    private boolean mHasSleep = false;
    private Object3D mCameraFrame;
    private ArrayList<Object3D> mAllCameraFrames = new ArrayList<>();

    public LSDRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        TARNativeInterface.nativeInitGL();
        intrinsics = TARNativeInterface.nativeGetIntrinsics();
        resolution = TARNativeInterface.nativeGetResolution();

        drawGrid();
        ArcballCamera arcball = new ArcballCamera(mContext, ((MainActivity)mContext).getView());
        arcball.setPosition(0, 0, 4);
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
        if (!mHasSleep) {
            try {
                Thread.sleep(1000);
                mHasSleep = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drawKeyframes();
        drawFrustum();
    }

    private void drawGrid() {
        DebugVisualizer debugViz = new DebugVisualizer(this);
        debugViz.addChild(new LSDGridFloor());
        getCurrentScene().addChild(debugViz);
    }

    private void drawFrustum() {
        float pose[] = TARNativeInterface.nativeGetPose();
        Matrix4 poseMatrix = new Matrix4();
        poseMatrix.setAll(pose);

        if (mCameraFrame == null) {
            Stack<Vector3> points = new Stack<>();
            points.add(new Vector3(0, 0, 0));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0, 0, 0));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0, 0, 0));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0, 0, 0));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (resolution[1] - 1 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (0 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));
            points.add(new Vector3(0.05 * (resolution[0] - 1 - intrinsics[0]) / intrinsics[2], 0.05 * (0 - intrinsics[1]) / intrinsics[3], 0.05));

            Line3D line = new Line3D(points, 1);
            line.setColor(0xff0000);
            line.setMaterial(new Material());
            line.setDrawingMode(GLES20.GL_LINES);
            mCameraFrame = line;
            getCurrentScene().addChild(mCameraFrame);
        }
        mCameraFrame.setPosition(poseMatrix.getTranslation());
        mCameraFrame.setOrientation(new Quaternion().fromMatrix(poseMatrix));
    }
    private void drawKeyframes() {
        drawPoints();
        drawCamera();
    }

    private void drawPoints() {

    }

    private void drawCamera() {
        float allPose[] = TARNativeInterface.nativeGetAllKeyFramePose();
        int num = allPose.length / 16;
        if (num > mAllCameraFrames.size()) {
            for (Object3D obj : mAllCameraFrames) {
                getCurrentScene().removeChild(obj);
            }
            mAllCameraFrames.clear();

            float cx = intrinsics[0];
            float cy = intrinsics[1];
            float fx = intrinsics[2];
            float fy = intrinsics[3];
            int width = resolution[0];
            int height = resolution[1];
            for (int i = 0; i < num; ++i) {
                float pose[] = Arrays.copyOfRange(allPose, i * 16, i * 16 + 16);
                Matrix4 poseMatrix = new Matrix4();
                poseMatrix.setAll(pose);

                Stack<Vector3> points = new Stack<>();
                points.add(new Vector3(0, 0, 0));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));
                points.add(new Vector3(0, 0, 0));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0, 0, 0));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0, 0, 0));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (height - 1 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (0 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));
                points.add(new Vector3(0.05 * (width - 1 - cx) / fx, 0.05 * (0 - cy) / fy, 0.05));

                Line3D line = new Line3D(points, 1);
                line.setColor(0xff0000);
                line.setMaterial(new Material());
                line.setDrawingMode(GLES20.GL_LINES);
                line.setPosition(poseMatrix.getTranslation());
                line.setOrientation(new Quaternion().fromMatrix(poseMatrix));
                mAllCameraFrames.add(line);
            }
            getCurrentScene().addChildren(mAllCameraFrames);
        }
    }
}
