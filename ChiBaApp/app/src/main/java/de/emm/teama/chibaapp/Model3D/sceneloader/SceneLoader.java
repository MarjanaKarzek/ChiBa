package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder.Callback;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * This class loads a 3D scene as an example of what can be done with the app
 */
public class SceneLoader
{
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
	private boolean rotatingLight = true;

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

	public void init() {

		// Load object
		if (parent.getParamFile() != null || parent.getParamAssetDir() != null) {

			// Create asset url
			final URL url;
			try {
				if (parent.getParamFile() != null) {
					url = parent.getParamFile().toURI().toURL();
				} else {
					// TODO find the right path to assets
					url = new URL("android://org.andresoviedo.dddmodel2/assets/" + parent.getParamAssetDir() + File.separator + parent.getParamAssetFilename());
				}
			} catch (MalformedURLException e) {
				Log.e("SceneLoader", e.getMessage(), e);
				throw new RuntimeException(e);
			}

		}
	}

	public Object3DData getLightBulb() {
		return lightPoint;
	}

	/* Hook for animating the objects before the rendering; rotate the light source around object */
	public void onDrawFrame(){
//		animateLight();
	}

	private void animateLight() {
		if (!rotatingLight) return;

		// animate light - Do a complete rotation every 5 seconds.
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

	public boolean isDrawBoundingBox() {
		return drawBoundingBox;
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
