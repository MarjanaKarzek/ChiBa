package de.emm.teama.chibaapp.Main;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;
import de.emm.teama.chibaapp.Model3D.sceneloader.SceneLoader;
import de.emm.teama.chibaapp.Model3D.util.Utils;
import de.emm.teama.chibaapp.Model3D.view.ModelSurfaceView;


public class MainFragment extends Fragment
{
    private static final String TAG = "MainFragment";

    /* The file to load. Passed as input parameter */
    private String paramFilename;
    private String paramAssetDir;
    private String paramAssetFilename;

    /* Enter into Android Immersive mode so the renderer is full screen or not */
    private boolean immersiveMode = true;

    /* Background GL clear color. Default is white */
    private float[] backgroundColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private GLSurfaceView gLView;

    private SceneLoader scene;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        gLView = new ModelSurfaceView(this);

        // Create our 3D scenario
        scene = new SceneLoader(this);
        try {
            // 3D Axis
            Object3DData axis = Object3DBuilder.buildAxis().setId("axis");
            axis.setColor(new float[] { 1.0f, 0, 0, 1.0f });
            scene.addObject(axis);

            // 3D Object
            Object3DData android = Object3DBuilder.loadObj(this.getActivity().getAssets(), "models", "android.obj");
            android.setPosition(new float[] { 0f, 0f, 0f });
            android.setColor(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
            scene.addObject(android);
        } catch (Exception ex) {}


        // TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom for
        Utils.printTouchCapabilities(this.getActivity().getPackageManager());

        return gLView;
    }


    public File getParamFile() {
        return getParamFilename() != null ? new File(getParamFilename()) : null;
    }

    public String getParamAssetDir() {
        return paramAssetDir;
    }

    public String getParamAssetFilename() {
        return paramAssetFilename;
    }

    public String getParamFilename() {
        return paramFilename;
    }

    public float[] getBackgroundColor(){
        return backgroundColor;
    }

    public SceneLoader getScene() {
        return scene;
    }

    public GLSurfaceView getgLView() {
        return gLView;
    }
}
