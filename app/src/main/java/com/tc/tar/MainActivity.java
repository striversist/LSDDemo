package com.tc.tar;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    public static final String TAG = MainActivity.class.getSimpleName();
    private GLSurfaceView mGLSurfaceView;

    static {
        System.loadLibrary("g2o_core");
        System.loadLibrary("g2o_csparse_extension");
        System.loadLibrary("g2o_ext_csparse");
        System.loadLibrary("g2o_solver_csparse");
        System.loadLibrary("g2o_stuff");
        System.loadLibrary("g2o_types_sba");
        System.loadLibrary("g2o_types_sim3");
        System.loadLibrary("g2o_types_slam3d");
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("LSD");
        System.loadLibrary("pangolin");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        TARNativeInterface.nativeInit();
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLSurfaceView.setPreserveEGLContextOnPause(true);
        mGLSurfaceView.setRenderer(this);
//        mGLSurfaceView.setOnTouchListener(this);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(mGLSurfaceView);

        registerForContextMenu(mGLSurfaceView);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        TARNativeInterface.nativeInitGL();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        TARNativeInterface.nativeResize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);
        TARNativeInterface.nativeRender();
        mGLSurfaceView.requestRender();
    }
}
