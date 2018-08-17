package com.luiz0tavio.moonratar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main);

        ModelRenderable.builder()
            .setSource(this, R.raw.sceneform_default_light_probes)
            .build()
            .thenAccept(renderable -> modelRenderable = renderable)
            .exceptionally(throwable -> {
                Log.e(MainActivity.class.getSimpleName(), "Unable to load Renderable.", throwable);
                return null;
            });

       
    }
}
