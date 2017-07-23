package de.emm.teama.chibaapp.Model3D.controller;

import de.emm.teama.chibaapp.Model3D.model.Object3DData;
import de.emm.teama.chibaapp.Model3D.sceneloader.SceneLoader;
import de.emm.teama.chibaapp.Model3D.view.ModelRenderer;
import de.emm.teama.chibaapp.Model3D.view.ModelSurfaceView;
import de.emm.teama.chibaapp.Model3D.util.Math3DUtils;

import android.graphics.PointF;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * <h1>TouchController Class</h1>
 * TouchController class to handle touch interactions with an OpenGL object.
 * <p>
 * @author Andres Oviedo
 * @version 1.3.1
 * @since 2017-04-23
 * Title: Android 3D Model Viewer
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */

public class TouchController {

	private static final String TAG = TouchController.class.getName();

	private static final int TOUCH_STATUS_ZOOMING_CAMERA = 1;
	private static final int TOUCH_STATUS_ROTATING_CAMERA = 4;
	private static final int TOUCH_STATUS_MOVING_WORLD = 5;

	private final ModelSurfaceView view;
	private final ModelRenderer mRenderer;

	int pointerCount = 0;
	float x1 = Float.MIN_VALUE;
	float y1 = Float.MIN_VALUE;
	float x2 = Float.MIN_VALUE;
	float y2 = Float.MIN_VALUE;
	float dx1 = Float.MIN_VALUE;
	float dy1 = Float.MIN_VALUE;
	float dx2 = Float.MIN_VALUE;
	float dy2 = Float.MIN_VALUE;

	float length = Float.MIN_VALUE;
	float previousLength = Float.MIN_VALUE;
	float currentPress1 = Float.MIN_VALUE;
	float currentPress2 = Float.MIN_VALUE;

	float rotation = 0;
	int currentSquare = Integer.MIN_VALUE;

	boolean isOneFixedAndOneMoving = false;
	boolean fingersAreClosing = false;
	boolean isRotating = false;

	boolean gestureChanged = false;
	private boolean moving = false;
	private boolean simpleTouch = false;
	private long lastActionTime;
	private int touchDelay = -2;
	private int touchStatus = -1;

	private float previousX1;
	private float previousY1;
	private float previousX2;
	private float previousY2;
	float[] previousVector = new float[4];
	float[] vector = new float[4];
	float[] rotationVector = new float[4];
	private float previousRotationSquare;


	/**
     * Parameterized constructor of the TouchController class.
     *
     * @param view      the current ModelSurfaceView
     * @param renderer  the current ModelRenderer
     * */
	public TouchController(ModelSurfaceView view, ModelRenderer renderer) {
		super();
		this.view = view;
		this.mRenderer = renderer;
	}

	/**
     *  MotionEvent reports input details from the touch screen and other input controls.
     *  In this case, you are only interested in events where the touch position changed.
     *
     *  @param motionEvent  the motion event
     *  @return the onTouchEvent state as boolean value
     * */
	public synchronized boolean onTouchEvent(MotionEvent motionEvent) {

		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_HOVER_EXIT:
		case MotionEvent.ACTION_OUTSIDE:
			// this to handle "1 simple touch"
			if (lastActionTime > SystemClock.uptimeMillis() - 250) {
				simpleTouch = true;
			} else {
				gestureChanged = true;
				touchDelay = 0;
				lastActionTime = SystemClock.uptimeMillis();
				simpleTouch = false;
			}
			moving = false;
			break;
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_HOVER_ENTER:
			gestureChanged = true;
			touchDelay = 0;
			lastActionTime = SystemClock.uptimeMillis();
			simpleTouch = false;
			break;
		case MotionEvent.ACTION_MOVE:
			moving = true;
			simpleTouch = false;
			touchDelay++;
			break;
		default:
			Log.w(TAG, "Unknown state: " + motionEvent.getAction());
			gestureChanged = true;
		}

		pointerCount = motionEvent.getPointerCount();

		if (pointerCount == 1) {
			x1 = motionEvent.getX();
			y1 = motionEvent.getY();
			if (gestureChanged) {
				previousX1 = x1;
				previousY1 = y1;
			}
			dx1 = x1 - previousX1;
			dy1 = y1 - previousY1;
		} else if (pointerCount == 2) {
			x1 = motionEvent.getX(0);
			y1 = motionEvent.getY(0);
			x2 = motionEvent.getX(1);
			y2 = motionEvent.getY(1);
			vector[0] = x2 - x1;
			vector[1] = y2 - y1;
			vector[2] = 0;
			vector[3] = 1;
			float len = Matrix.length(vector[0], vector[1], vector[2]);
			vector[0] /= len;
			vector[1] /= len;

			if (gestureChanged) {
				previousX1 = x1;
				previousY1 = y1;
				previousX2 = x2;
				previousY2 = y2;
				System.arraycopy(vector, 0, previousVector, 0, vector.length);
			}
			dx1 = x1 - previousX1;
			dy1 = y1 - previousY1;
			dx2 = x2 - previousX2;
			dy2 = y2 - previousY2;

			rotationVector[0] = (previousVector[1] * vector[2]) - (previousVector[2] * vector[1]);
			rotationVector[1] = (previousVector[2] * vector[0]) - (previousVector[0] * vector[2]);
			rotationVector[2] = (previousVector[0] * vector[1]) - (previousVector[1] * vector[0]);
			len = Matrix.length(rotationVector[0], rotationVector[1], rotationVector[2]);
			rotationVector[0] /= len;
			rotationVector[1] /= len;
			rotationVector[2] /= len;

			previousLength = (float) Math
					.sqrt(Math.pow(previousX2 - previousX1, 2) + Math.pow(previousY2 - previousY1, 2));
			length = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

			currentPress1 = motionEvent.getPressure(0);
			currentPress2 = motionEvent.getPressure(1);
			rotation = 0;
			if (currentSquare == 1 && previousRotationSquare == 4) {
				rotation = 0;
			} else if (currentSquare == 4 && previousRotationSquare == 1) {
				rotation = 360;
			}

			// gesture detection
			isOneFixedAndOneMoving = ((dx1 + dy1) == 0) != (((dx2 + dy2) == 0));
			fingersAreClosing = !isOneFixedAndOneMoving && (Math.abs(dx1 + dx2) < 10 && Math.abs(dy1 + dy2) < 10);
			isRotating = !isOneFixedAndOneMoving && (dx1 != 0 && dy1 != 0 && dx2 != 0 && dy2 != 0)
					&& rotationVector[2] != 0;
		}

		if (pointerCount == 1 && simpleTouch) {
			// calculate the world coordinates where the user is clicking (near plane and far plane)
			float[] hit1 = unproject(x1, y1, 0);
			float[] hit2 = unproject(x1, y1, 1);
			// check if the ray intersect any of our objects and select the nearer
			selectObjectImpl(hit1, hit2);
		}


		int max = Math.max(mRenderer.getWidth(), mRenderer.getHeight());
		if (touchDelay > 1) {
			// INFO: Procesar gesto
			if (pointerCount == 1 && currentPress1 > 4.0f) {
			} else if (pointerCount == 1) {
				touchStatus = TOUCH_STATUS_MOVING_WORLD;
				// Log.d("TouchController", "Translating camera (dx,dy) '" + dx1 + "','" + dy1 + "'...");
				dx1 = (float)(dx1 / max * Math.PI * 2);
				dy1 = (float)(dy1 / max * Math.PI * 2);
				mRenderer.getCamera().translateCamera(dx1,dy1);
			} else if (pointerCount == 2) {
				if (fingersAreClosing) {
					touchStatus = TOUCH_STATUS_ZOOMING_CAMERA;
					float zoomFactor = (length - previousLength) / max * mRenderer.getFar();
					mRenderer.getCamera().MoveCameraZ(zoomFactor);
				}
				if (isRotating) {
					touchStatus = TOUCH_STATUS_ROTATING_CAMERA;
					mRenderer.getCamera().Rotate((float) (Math.signum(rotationVector[2]) / Math.PI) / 4);
				}
			}

		}

		previousX1 = x1;
		previousY1 = y1;
		previousX2 = x2;
		previousY2 = y2;

		previousRotationSquare = currentSquare;

		System.arraycopy(vector, 0, previousVector, 0, vector.length);

		if (gestureChanged && touchDelay > 1) {
			gestureChanged = false;
			Log.v(TAG, "Fin");
		}

		view.requestRender();

		return true;

	}

	/**
	 * Get the nearest object intersecting the specified ray and selects it
	 * 
	 * @param nearPoint     the near point in world coordinates
	 * @param farPoint      the far point in world coordinates
	 */
	private void selectObjectImpl(float[] nearPoint, float[] farPoint) {
		SceneLoader scene = view.getMainFragment().getScene();
		if (scene == null) {
			return;
		}
		Object3DData objectToSelect = null;
		float objectToSelectDistance = Integer.MAX_VALUE;
		for (Object3DData obj : scene.getObjects()) {
			float distance = Math3DUtils.calculateDistanceOfIntersection(nearPoint, farPoint, obj.getPosition(), 1f);
			if (distance != -1) {
				if (distance < objectToSelectDistance) {
					objectToSelectDistance = distance;
					objectToSelect = obj;
				}
			}
		}
		if (objectToSelect != null) {
			if (scene.getSelectedObject() == objectToSelect) {
				scene.setSelectedObject(null);
            } else {
				scene.setSelectedObject(objectToSelect);
            }
		}
	}

	/**
     * This method calculates coordinates of a touch on the screen.
     *
     * @param rx    the renderer x position
     * @param ry    the renderer y position
     * @param rz    the renderer z position
     * @return the coordinates as float array
     * */
	public float[] unproject(float rx, float ry, float rz) {
		float[] xyzw = { 0, 0, 0, 0 };

		ry = (float) mRenderer.getHeight() - ry;

		int[] viewport = { 0, 0, mRenderer.getWidth(), mRenderer.getHeight() };

		GLU.gluUnProject(rx, ry, rz, mRenderer.getModelViewMatrix(), 0, mRenderer.getModelProjectionMatrix(), 0,
				viewport, 0, xyzw, 0);

		xyzw[0] /= xyzw[3];
		xyzw[1] /= xyzw[3];
		xyzw[2] /= xyzw[3];
		xyzw[3] = 1;
		return xyzw;
	}
}