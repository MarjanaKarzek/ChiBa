package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.content.res.AssetManager;
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

	/* Whether to draw using textures */
	private boolean drawTextures = true;

	/* Light toggle feature: we have 3 states: no light, light, light + rotation */
	private boolean rotatingLight = false;
    private boolean rotatingObject = true;

	private boolean drawLighting = true;                        // Light toggle feature: whether to draw using lights

	private float[] lightPosition = new float[]{0f, 0f, 3, 1};    // Initial light position

    private Object3DData selectedObject = null;                 // Object selected by the user
    boolean istLinks = false;
    boolean istOben = false;

	// Light bulb 3d data
	private final Object3DData lightPoint = Object3DBuilder.buildPoint(new float[4]).setId("light").setPosition(lightPosition);

    public SceneLoader(MainFragment main, AssetManager assets, boolean usesAvatar, String animation)
    {
        this.parent = main;
        if(usesAvatar) {
            switch (animation) {
                case "sunny":
                    try {
                        Object3DData tailWagging_frame1 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame1.obj");
                        tailWagging_frame1.centerAndScale(2.0f);
                        tailWagging_frame1.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame1);

                        Object3DData tailWagging_frame2 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame2.obj");
                        tailWagging_frame2.centerAndScale(2.0f);
                        tailWagging_frame2.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame2);

                        Object3DData tailWagging_frame3 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame3.obj");
                        tailWagging_frame3.centerAndScale(2.0f);
                        tailWagging_frame3.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame3);

                        Object3DData tailWagging_frame4 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame4.obj");
                        tailWagging_frame4.centerAndScale(2.0f);
                        tailWagging_frame4.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame4);

                        Object3DData tailWagging_frame5 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame5.obj");
                        tailWagging_frame5.centerAndScale(2.0f);
                        tailWagging_frame5.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame5);

                        Object3DData tailWagging_frame6 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame6.obj");
                        tailWagging_frame6.centerAndScale(2.0f);
                        tailWagging_frame6.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame6);

                        Object3DData tailWagging_frame7 = Object3DBuilder.loadObj(assets, "models/tailWagging", "tailWagging_Frame7.obj");
                        tailWagging_frame7.centerAndScale(2.0f);
                        tailWagging_frame7.setPosition(new float[]{-1.0f, -0.5f, 0f});
                        addObject(tailWagging_frame7);
                    } catch (Exception ex) {
                        Log.e(TAG, "SceneLoader: some object not found");
                    }
                    break;
                case "rain":
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

	int objectcounter = 0;
    double lastTime = SystemClock.uptimeMillis();
    int runtimeCounter = 0;

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

    private void bouncing(){
//        float gLViewWidth = parent.getgLView().getWidth();
//        float gLViewHeight = parent.getgLView().getHeight();
//        float radius = (selectedObject.getDimensions().getWidth() / 2 );

        float posX = selectedObject.getPositionX();
        float posY = selectedObject.getPositionY();
        float posZ = selectedObject.getPositionZ();

        if(posX < 3f && !istLinks) {
            // rechts
            posX += 0.02f;

        } else {
            // links
            posX = posX - 0.02f;
            istLinks = true;

            if(posX < -3f)
                istLinks = false;
        }

        if(posY < 3f && !istOben)
            // oben
            posY += 0.02f;
        else {
            // unten
            posY = posY - 0.02f;
            istOben = true;

            if(posY < -3f)
                istOben = false;

        }

        selectedObject.setPosition(new float[]{posX, posY, posZ});
    }

    private void rotateAroundY(){
        // Drehung um die Y-Achse
        long time = SystemClock.uptimeMillis() % 5000L;
        float angleInDegrees = (360.0f / 5000.0f) * ((int) time);
        selectedObject.setRotationY(angleInDegrees);
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
    }
}
