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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable booRenderable;
    private ModelRenderable spiderRenderable;

    private int modelCount = 0;

    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main);

        ModelRenderable.builder()
            .setSource(this, R.raw.boo)
            .build()
            .thenAccept(renderable -> booRenderable = renderable)
            .exceptionally(throwable -> {
                Log.e(MainActivity.class.getSimpleName(), "Unable to load Renderable.", throwable);
                return null;
            });

        ModelRenderable.builder()
                .setSource(this, R.raw.spider)
                .build()
                .thenAccept(renderable -> spiderRenderable = renderable)
                .exceptionally(throwable -> {
                    Log.e(MainActivity.class.getSimpleName(), "Unable to load Renderable.", throwable);
                    return null;
                });


        arFragment.setOnTapArPlaneListener(
            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                if (booRenderable == null || spiderRenderable == null) return;

                // Create the Anchor.
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                modelCount++;

                // Create the transformable andy and add it to the anchor.
                TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
                model.setParent(anchorNode);
                model.setRenderable(modelCount % 2 == 0 ? booRenderable : spiderRenderable);
                model.select();
            });
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {

        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(activity.getLocalClassName(), "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }

        String openGlVersionString =
            ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo()
                .getGlEsVersion();

        if (Double.parseDouble(openGlVersionString) < 3.0) {
            Log.e(activity.getLocalClassName(), "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }

        return true;
    }


}
