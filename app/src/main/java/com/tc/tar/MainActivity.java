package com.tc.tar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;

import java.nio.ByteBuffer;


public class MainActivity extends AppCompatActivity implements LSDRenderer.RenderListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout  mLayout;
    private SurfaceView     mRajawaliSurface;
    private Renderer        mRenderer;
    private ImageView       mImageView;
    private int[]           mResolution;

    static {
        System.loadLibrary("g2o_core");
        System.loadLibrary("g2o_csparse_extension");
        System.loadLibrary("g2o_ext_csparse");
        System.loadLibrary("g2o_solver_csparse");
        System.loadLibrary("g2o_stuff");
        System.loadLibrary("g2o_types_sba");
        System.loadLibrary("g2o_types_sim3");
        System.loadLibrary("g2o_types_slam3d");
        System.loadLibrary("LSD");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TARNativeInterface.nativeInit();
        mRajawaliSurface = createSurfaceView();
        mRenderer = createRenderer();
        applyRenderer();

        mLayout = new RelativeLayout(this);
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayout.addView(mRajawaliSurface, childParams);

        mImageView = new ImageView(this);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(480, 320);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mLayout.addView(mImageView, imageParams);
        mResolution = TARNativeInterface.nativeGetResolution();

        setContentView(mLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TARNativeInterface.nativeDestroy();
            finish();
            System.exit(0);
        }
        return true;
    }

    protected SurfaceView createSurfaceView() {
        SurfaceView view = new SurfaceView(this);
        view.setFrameRate(60);
        view.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);
        return view;
    }

    protected Renderer createRenderer() {
        LSDRenderer renderer = new LSDRenderer(this);
        renderer.setRenderListener(this);
        return renderer;
    }

    protected void applyRenderer() {
        mRajawaliSurface.setSurfaceRenderer(mRenderer);
    }

    public View getView() {
        return mLayout;
    }

    @Override
    public void onRender() {
        if (mImageView == null)
            return;
        final byte[] rawData = TARNativeInterface.nativeGetCurrentImage(0);
        if (rawData == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = Bitmap.createBitmap(mResolution[0], mResolution[1], Bitmap.Config.ARGB_8888);
                bm.copyPixelsFromBuffer(ByteBuffer.wrap(rawData));
                mImageView.setImageBitmap(bm);
            }
        });
    }
}
