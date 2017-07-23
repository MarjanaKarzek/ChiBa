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

	private boolean drawNormals = false;

	private boolean drawTextures = true;

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

    private boolean usesAvatar;

	// Light bulb 3d data
	private final Object3DData lightPoint = Object3DBuilder.buildPoint(new float[4]).setId("light").setPosition(lightPosition);

    /**
     * Parameterized constructor to set up a scene for a 3D object scenario.
     *
     * @param main          the parent fragment
     * @param assets        the current assets manager
     * @param usesAvatar    the state whether to use an avatar
     * @param animation     the current animation for the 3D object
     * */
    public SceneLoader(MainFragment main, AssetManager assets, boolean usesAvatar, String animation)
    {
        this.parent = main;
        this.usesAvatar = usesAvatar;

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

	/**
     * Method to return the light point of a 3D object scenario.
     *
     * @return the light point
     * */
	public Object3DData getLightBulb() {
		return lightPoint;
	}

	/**
     * Method called by the ModelRenderer onDrawFrame() to draw a scenario frame by frame.
     * Also enables to show animation of 3D objects.
     * */
	public void onDrawFrame(){
        if(usesAvatar) {
            if (loopable) {
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
            } else
                objects.get(0).setVisible(true);
        }
    }

    /**
     * Method to add an Object3DData object to the current scene.
     *
     * @param obj the 3d object data
     * */
	public synchronized void addObject(Object3DData obj) {
		List<Object3DData> newList = new ArrayList<Object3DData>(objects);
		newList.add(obj);
		this.objects = newList;
		requestRender();
	}

	/**
     * Method which request that the renderer render a frame.
     * */
	private void requestRender() {
		parent.getgLView().requestRender();
	}

	/**
     * Method to return the 3d objects used in the scene.
     *
     * @return the 3d objects used in the scene
     * */
	public synchronized List<Object3DData> getObjects() {
		return objects;
	}

	/**
     * Method to detect whether a wire frame should be drawn.
     *
     * @return whether a wire frame should be drawn
     * */
	public boolean isDrawWireframe() {
		return this.drawWireframe;
	}

    /**
     * Method to detect whether points should be drawn.
     *
     * @return whether points should be drawn
     * */
	public boolean isDrawPoints() {
		return this.drawingPoints;
	}

    /**
     * Method to detect whether normals should be drawn.
     *
     * @return whether normals should be drawn
     * */
	public boolean isDrawNormals() {
		return drawNormals;
	}

    /**
     * Method to detect whether texture should be drawn.
     *
     * @return whether normals texture be drawn
     * */
	public boolean isDrawTextures() {
		return drawTextures;
	}

    /**
     * Method to detect whether lighting should be drawn.
     *
     * @return whether lighting should be drawn
     * */
	public boolean isDrawLighting() {
		return drawLighting;
	}

	/**
     * Method which returns the selected 3d object.
     *
     * @return the selected 3d object
     * */
    public Object3DData getSelectedObject() {
        return selectedObject;
    }

    /**
     * Method to set an selected 3d object and the command to open the external Unity ChiBa Application.
     *
     * @param selectedObject the selected 3d object
     * */
    public void setSelectedObject(Object3DData selectedObject) {
        this.selectedObject = selectedObject;

        MainFragment.openApp(parent.getContext(), "com.HTWEMM.ChiBa");
    }
}
