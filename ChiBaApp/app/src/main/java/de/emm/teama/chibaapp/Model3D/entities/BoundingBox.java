package de.emm.teama.chibaapp.Model3D.entities;

/**
 * <h1>BoundingBox Class</h1>
 * This class implements the scene for the OpenGL implementation.
 * <p>
 *
 * @author Andres Oviedo, modified by Natalie Grasser
 * @version 1.3.1
 * @since 2017-04-23, modified 2017-06-28
 * Title: Android 3D Model Viewer
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */

public final class BoundingBox {
	private final String id;
	private float xMin = Float.MAX_VALUE;
	private float xMax = Float.MIN_VALUE;
	private float yMin = Float.MAX_VALUE;
	private float yMax = Float.MIN_VALUE;
	private float zMin = Float.MAX_VALUE;
	private float zMax = Float.MIN_VALUE;

    /**
     * Parameterized constructor to set up a bounding box.
     *
     * @param id    identifier for bounding box
     * @param xMin  minumum on x axis
     * @param xMax  maximum on x axis
     * @param yMin  minumum on y axis
     * @param yMax  maximum on y axis
     * @param zMin  minumum on z axis
     * @param zMax  maximum on z axis
     * */
	public BoundingBox(String id, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		this.id = id;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
	}

	/**
	 * Getter method for bounding box.
     *
     * @return the minumum on x axis
	 * */
	public float getxMin() {
		return xMin;
	}

    /**
     * Getter method for bounding box.
     *
     * @return the maximum on x axis
     * */
	public float getxMax() {
		return xMax;
	}

    /**
     * Getter method for bounding box.
     *
     * @return minumum on y axis
     * */
	public float getyMin() {
		return yMin;
	}

    /**
     * Getter method for bounding box.
     *
     * @return maximum on y axis
     * */
	public float getyMax() {
		return yMax;
	}

    /**
     * Getter method for bounding box.
     *
     * @return minimum on z axis
     * */
	public float getzMin() {
		return zMin;
	}

    /**
     * Getter method for bounding box.
     *
     * @return maximum on z axis
     * */
	public float getzMax() {
		return zMax;
	}

	/**
     * Method to check whether a point is inside of a bounding box.
     *
     * @param x coordinate of point on x axis
     * @param y coordinate of point on y axis
     * @param z coordinate of point on z axis
     * @return whether the point is inside the bounding box
     * */
	public boolean insideBounds(float x, float y, float z){
		return !outOfBound(x,y,z);
	}

    /**
     * Method to check whether a point is outside of a bounding box.
     *
     * @param x coordinate of point on x axis
     * @param y coordinate of point on y axis
     * @param z coordinate of point on z axis
     * @return whether the point is outside the bounding box
     * */
	public boolean outOfBound(float x, float y, float z) {
		if (x > getxMax()) {
			return true;
		}
		if (x < getxMin()) {
			return true;
		}
		if (y < getyMin()) {
			return true;
		}
		if (y > getyMax()){
			return true;
		}
		if (z < getzMin()){
			return true;
		}
		if (z > getzMax()){
			return true;
		}
		return false;
	}

	/**
     * A method to get information about a bounding box.
     *
     * @return the boundaries of a bounding box as STRING.
     * */
	@Override
	public String toString() {
		return "BoundingBox{" +
				"id='" + id + '\'' +
				", xMin=" + xMin +
				", xMax=" + xMax +
				", yMin=" + yMin +
				", yMax=" + yMax +
				", zMin=" + zMin +
				", zMax=" + zMax +
				'}';
	}
}