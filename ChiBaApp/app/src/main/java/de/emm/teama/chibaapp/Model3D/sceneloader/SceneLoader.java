package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.content.res.AssetManager;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

public class SceneLoader
{
    private final static String TAG = SceneLoader.class.getName();

	private final MainFragment parent;                          // Parent component

	private List<Object3DData> objects = new ArrayList<Object3DData>();    // List of data objects containing info for building the opengl objects

	private boolean drawWireframe = false;

	private boolean drawingPoints = false;

	private boolean drawBoundingBox = false;

	private boolean drawNormals = false;

	private boolean drawTextures = true;

	private boolean rotatingLight = false;                      // Light toggle feature: we have 3 states: no light, light, light + rotation

	private boolean drawLighting = true;                        // Light toggle feature: whether to draw using lights

	private float[] lightPosition = new float[]{0f, 0f, 3, 1};  // Initial light position

    private Object3DData selectedObject = null;                 // Object selected by the user

    double lastTime = SystemClock.uptimeMillis();
    int runtimeCounter = 0;
    int objectcounter = 0;

	// Light bulb 3d data
	private final Object3DData lightPoint = Object3DBuilder.buildPoint(new float[4]).setId("light").setPosition(lightPosition);

    public SceneLoader(MainFragment main, AssetManager assets, boolean usesAvatar, String animation)
    {
        this.parent = main;

        animation = "sunny";

        if(usesAvatar) {
            switch (animation) {
                case "sunny":
                    try {
                        String[] assetsArray = assets.list("models/tailWagging");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "tailWagging_Frame" + counter + ".obj";
                                Object3DData rain = Object3DBuilder.loadObj(assets, "models/tailWagging", filename);
                                rain.centerAndScale(2.0f);
                                rain.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(rain);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object not found");
                    }
                    break;
                case "rain":
                    try {
                        String[] assetsArray = assets.list("models/umbrella");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "umbrella_Frame" + counter + ".obj";
                                Object3DData rain = Object3DBuilder.loadObj(assets, "models/umbrella", filename);
                                rain.centerAndScale(2.0f);
                                rain.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(rain);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object not found");
                    }
                    break;
                default:
                    Log.d(TAG, "SceneLoader: default scene selected");

                    try {
                        Object3DData chiba = Object3DBuilder.loadObj(assets, "models", "chiba.obj");
                        chiba.centerAndScale(2.0f);
                        chiba.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(chiba);
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in default scene not found");
                    }
                    break;
            }

        }
	}

	public Object3DData getLightBulb() {
		return lightPoint;
	}

	public void onDrawFrame(){
        double currentTime = SystemClock.uptimeMillis();
        if(currentTime - lastTime >= 115.0){
            for(Object3DData object: objects){
                if(objects.indexOf(object) == objectcounter)
                    object.setVisible(true);
                else
                    object.setVisible(false);
            }
            objectcounter++;
            if(objectcounter == objects.size()) {
                objectcounter = 0;
                runtimeCounter++;
                if(runtimeCounter == 5) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runtimeCounter=0;
                }
            }
            lastTime = currentTime;
            //animateLight();
        }
    }

	private void animateLight() {
		if (!rotatingLight) return;

        long time = SystemClock.uptimeMillis() % 5000L;
		float angleInDegrees = (360.0f / 5000.0f) * ((int) time);
        lightPoint.setRotationY(angleInDegrees);
	}

	public synchronized void addObject(Object3DData obj) {
		List<Object3DData> newList = new ArrayList<Object3DData>(objects);
		newList.add(obj);
		this.objects = newList;
		requestRender();
	}

	private void requestRender() {
		parent.getgLView().requestRender();
	}

	public synchronized List<Object3DData> getObjects() {
		return objects;
	}

	public boolean isDrawWireframe() {
		return this.drawWireframe;
	}

	public boolean isDrawPoints() {
		return this.drawingPoints;
	}

	public boolean isDrawNormals() {
		return drawNormals;
	}

	public boolean isDrawTextures() {
		return drawTextures;
	}

	public boolean isDrawLighting() {
		return drawLighting;
	}

    public Object3DData getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(Object3DData selectedObject) {
        this.selectedObject = selectedObject;

        MainFragment.openApp(parent.getContext(), "com.HTWEMM.ChiBa");
    }
}
