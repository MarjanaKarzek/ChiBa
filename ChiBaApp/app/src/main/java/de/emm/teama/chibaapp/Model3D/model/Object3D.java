package de.emm.teama.chibaapp.Model3D.model;

/**
 * <h1>Object3D Interface</h1>
 * This interface specifies the functionality to build the 3D object.
 *
 * @author Andres Oviedo, modified by Natalie Grasser
 * @version 1.3.1
 * @since 2017-04-23, modified 2017-06-28
 * Title: Android 3D Model Viewer
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */

public interface Object3D {

	/**
     * Number of coordinates per vertex in this array.
     * */
	int COORDS_PER_VERTEX = 3;
	int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per

    /**
     * Default color of an Object3D object.
     * */
	float[] DEFAULT_COLOR = { 1.0f, 0.0f, 0, 1.0f };

    /**
     * Method to draw an Object3D object.
     *
     * @param obj                   the object 3D data
     * @param pMatrix               the projection matrix of the Object3D object
     * @param vMatrix               the vector matrix of the Object3D object
     * @param textureId             the texture identifier of the Object3D object
     * @param lightPosInEyeSpace    the light position of the Object3D object
     * */
	void draw(Object3DData obj, float[] pMatrix, float[] vMatrix, int textureId, float[] lightPosInEyeSpace);

    /**
     * Method to draw an Object3D object.
     *
     * @param obj                   the object 3D data
     * @param pMatrix               the projection matrix of the Object3D object
     * @param vMatrix               the vector matrix of the Object3D object
     * @param drawType              the draw type of the Object3D object
     * @param drawSize              the draw size of the Object3D object
     * @param textureId             the texture identifier of the Object3D object
     * @param lightPosInEyeSpace    the light position of the Object3D object
     * */
	void draw(Object3DData obj, float[] pMatrix, float[] vMatrix, int drawType, int drawSize, int textureId, float[] lightPosInEyeSpace);
}