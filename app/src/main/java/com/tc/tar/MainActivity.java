package com.tc.tar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

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
    }
}
