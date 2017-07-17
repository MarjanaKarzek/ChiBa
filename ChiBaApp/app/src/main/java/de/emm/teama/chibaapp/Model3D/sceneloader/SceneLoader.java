package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.content.res.AssetManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * <h1>SceneLoader Class</h1>
 * This class implements the scene for the OpenGL implementation.
 * <p>
 *
 * @author Natalie Grasser
 * @version 1.0
 * @since 2017-06-28
 *
 * <p>
 * Based on
 * Authos: Andres Oviedo
 * Date: 2017-04-23
 * Title: Android 3D Model Viewer
 * Version: 1.3.1
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */

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

    private double lastTime = SystemClock.uptimeMillis();
    private int runtimeCounter = 0;
    private int objectcounter = 0;
    private int previousobject = 1;
    private int loopdirection = 1;
    private double speed;
    private boolean rewindable;
    private boolean loopable;


	// Light bulb 3d data
	private final Object3DData lightPoint = Object3DBuilder.buildPoint(new float[4]).setId("light").setPosition(lightPosition);

    public SceneLoader(MainFragment main, AssetManager assets, boolean usesAvatar, String animation)
    {
        this.parent = main;
        // Tests with Ballsport, Lernen, Laptop, Arbeit, Einkaufen, Restaurant, Geburtstag, Sonne, Regen, ""
        // animation = "Ballsport";

        if(usesAvatar) {
            switch (animation) {
                case "Ballsport":
                case "Fitness":
                case "Schwimmen":
                    loopable = true;
                    rewindable = false;
                    speed = 115.0;
                    try {
                        String[] assetsArray = assets.list("models/ball");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "ball_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/ball", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in ball scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Lernen":
                case "Klausur":
                case "Prüfungsanmeldung":
                case "Kursbelegung":
                case "Unterlagen":
                    loopable = true;
                    rewindable = true;
                    speed = 115.0;
                    try {
                        String[] assetsArray = assets.list("models/book");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "book_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/book", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in book scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Laptop":
                case "Lerngruppe":
                    loopable = true;
                    rewindable = true;
                    speed = 115.0;
                    try {
                        String[] assetsArray = assets.list("models/computerWork");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "computerWork_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/computerWork", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in computerWork scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Arbeit":
                case "Hausaufgaben":
                    loopable = false;
                    rewindable = false;
                    try {
                        String[] assetsArray = assets.list("models/computerWorkGlasses");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "computerWorkGlasses_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/computerWorkGlasses", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in default computerWorkGlasses not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Einkaufen":
                    loopable = true;
                    rewindable = true;
                    speed = 115.0;
                    try {
                        String[] assetsArray = assets.list("models/emptyBowl");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "emptyBowl_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/emptyBowl", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in emptyBowl scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Restaurant":
                case "Brunch":
                case "Business Lunch":
                case "Mahlzeit":
                    loopable = true;
                    rewindable = true;
                    speed = 80.0;
                    try {
                        String[] assetsArray = assets.list("models/fullBowl");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "fullBowl_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/fullBowl", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in fullBowl scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Geburtstag":
                case "Jahrestag":
                case "Muttertag":
                case "Vatertag":
                case "Valentinstag":
                case "Weihnachten":
                case "Halloween":
                case "Silvester":
                case "Chanukka":
                case "Chinesisches Neujahr":
                case "Ostern":
                case "Sommersonnenwende":
                case "Party":
                    loopable = true;
                    rewindable = true;
                    speed = 80.0;
                    try {
                        String[] assetsArray = assets.list("models/tailWag");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "tailWagging_frame" + counter + ".obj";
                                Object3DData object = Object3DBuilder.loadObj(assets, "models/tailWag", filename);
                                object.centerAndScale(2.0f);
                                object.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(object);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in tailWagging scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Sonne":
                    loopable = false;
                    rewindable = false;
                    try {
                        String[] assetsArray = assets.list("models/sunglasses");
                        int counter = 1;
                        for (String asset: assetsArray) {
                            if(asset.endsWith(".obj")){
                                String filename = "sunglasses_frame" + counter + ".obj";
                                Object3DData rain = Object3DBuilder.loadObj(assets, "models/sunglasses", filename);
                                rain.centerAndScale(2.0f);
                                rain.setPosition(new float[]{-1.0f, -0.5f, 0f});
                                addObject(rain);
                                counter ++;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in sunglasses scene not found");
                        ex.printStackTrace();
                    }
                    break;
                case "Regen":
                    loopable = true;
                    rewindable = true;
                    speed = 115.0;
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
                        Log.e(TAG, "SceneLoader: some object in umbrella scene not found");
                        ex.printStackTrace();
                    }
                    break;
                default:

                    loopable = false;
                    rewindable = false;
                    try {
                        Object3DData chiba = Object3DBuilder.loadObj(assets, "models/chiba", "chiba.obj");
                        chiba.centerAndScale(2.0f);
                        chiba.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(chiba);
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object in default scene not found");
                        ex.printStackTrace();
                    }
            }

        }
	}

	public Object3DData getLightBulb() {
		return lightPoint;
	}

	public void onDrawFrame(){
        if(loopable) {
            double currentTime = SystemClock.uptimeMillis();
            if (currentTime - lastTime >= speed) {
                objects.get(objectcounter).setVisible(true);
                objects.get(previousobject).setVisible(false);
                previousobject = objectcounter;
                objectcounter += loopdirection;
                if (objectcounter == objects.size() - 1 || objectcounter == 0) {
                    if (rewindable)
                        loopdirection *= -1;
                    else
                        objectcounter = 0;
                    runtimeCounter++;
                    if (runtimeCounter == 5) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runtimeCounter = 0;
                    }
                }
                lastTime = currentTime;
            }
        }
        else
            objects.get(0).setVisible(true);
    }

    private float[] rotateAroundY(Object3DData obj, float angle) {
        float[] rotation = obj.getRotation();

        float x = (float) (rotation[2] * Math.sin(angle) + rotation[0] * Math.cos(angle));
        float y = rotation[1];
        float z = (float) (rotation[2] * Math.cos(angle) - rotation[0] * Math.sin(angle));

        return new float[]{x, y, z};
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
