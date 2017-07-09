package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.os.SystemClock;
import android.transition.Scene;
import android.util.Log;

public class SceneLoader
{
    private final static String TAG = SceneLoader.class.getName();
	/* Parent component */
	private final MainFragment parent;

	/* List of data objects containing info for building the opengl objects */
	private List<Object3DData> objects = new ArrayList<Object3DData>();

	/* Whether to draw objects as wireframes */
	private boolean drawWireframe = false;

	/* Whether to draw using points */
	private boolean drawingPoints = false;

	/* Whether to draw bounding boxes around objects */
	private boolean drawBoundingBox = false;

	/* Whether to draw face normals. Normally used to debug models */
	private boolean drawNormals = false;

	/* Whether to draw using textures */
	private boolean drawTextures = true;

	/* Light toggle feature: we have 3 states: no light, light, light + rotation */
	private boolean rotatingLight = false;
    private boolean rotatingObject = true;

	/* Light toggle feature: whether to draw using lights */
	private boolean drawLighting = true;

	/* Initial light position */
	private float[] lightPosition = new float[]{0, 0, 3, 1};

    /* Object selected by the user */
    private Object3DData selectedObject = null;

	/* Light bulb 3d data */
	private final Object3DData lightPoint = Object3DBuilder.buildPoint(new float[4]).setId("light").setPosition(lightPosition);

    public SceneLoader(MainFragment main)
    {
		this.parent = main;
	}

	public Object3DData getLightBulb() {
		return lightPoint;
	}

	/* Hook for animating the objects before the rendering; rotate the light source around object */
	public void onDrawFrame(){
		animateLight();
        animateObject();
	}

	private void animateLight() {
		if (!rotatingLight) return;

		// animate light - Do a complete rotation every 5 seconds.
		long time = SystemClock.uptimeMillis() % 5000L;
		float angleInDegrees = (360.0f / 5000.0f) * ((int) time);
        lightPoint.setRotationY(angleInDegrees);
	}

    private void animateObject() {
        if (!rotatingObject || selectedObject == null) return;

        if (selectedObject.getId().equals("BallAnimiert.obj")) {
            float posX = selectedObject.getPositionX();
            float posY = selectedObject.getPositionY();
            float posZ = selectedObject.getPositionZ();

            if (posX < 0 && posY < 0 || posX > 10 && posY > 10) {
                posX = 0.0f;
                posY = 0.0f;
            } else {
                posX = posX + 0.05f;
                posY = posY + 0.05f;
            }

            selectedObject.setPosition(new float[]{posX, posY, posZ});
        }
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
