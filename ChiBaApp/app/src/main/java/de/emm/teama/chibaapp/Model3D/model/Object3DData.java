package de.emm.teama.chibaapp.Model3D.model;

import java.io.File;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import de.emm.teama.chibaapp.Model3D.entities.BoundingBox;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader.FaceMaterials;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader.Faces;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader.Materials;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader.Tuple3;

import android.opengl.GLES20;
import android.util.Log;

/**
 * <h1>Object3DData Class </h1>
 * This class represents the basic 3D data necessary to build the 3D object.
 *
 * @author Andres Oviedo, modified by Natalie Grasser
 * @version 1.3.1
 * @since 2017-04-23, modified 2017-06-28
 * Title: Android 3D Model Viewer
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */
public class Object3DData {

	// OpenGL version to use to draw this object
	private int version = 5;

	private File currentDir;

	private String assetsDir;
	private String id;
	private boolean drawUsingArrays = false;
	private boolean flipTextCoords = true;

	private float[] color;

	private int drawMode = GLES20.GL_POINTS;
	private int drawSize;

	// Model data
	private FloatBuffer vertexBuffer = null;
	private FloatBuffer vertexNormalsBuffer = null;
	private IntBuffer drawOrderBuffer = null;
	private ArrayList<Tuple3> texCoords;
	private Faces faces;
	private FaceMaterials faceMats;
	private Materials materials;

	// Processed arrays
	private FloatBuffer vertexArrayBuffer = null;
	private FloatBuffer vertexColorsArrayBuffer = null;
	private FloatBuffer vertexNormalsArrayBuffer = null;
	private FloatBuffer textureCoordsArrayBuffer = null;
	private List<int[]> drawModeList = null;
	private byte[] textureData = null;
	private List<InputStream> textureStreams = null;

	// derived data
	private BoundingBox boundingBox;

	// Transformation data
	protected float[] position = new float[] { 0f, 0f, 0f };
	protected float[] rotation = new float[] { 0f, 0f, 0f };
	protected float[] scale;

	// whether the object has changed
	private boolean changed;

	// Async Loader
	private WavefrontLoader.ModelDimensions modelDimensions;

	private boolean visible;

	/**
	 * Parameterized constructor of Object3DData object taking a vertex buffer as param.
	 *
	 * @param vertexArrayBuffer the vertex array buffer of the Object3DData object
	 */
	public Object3DData(FloatBuffer vertexArrayBuffer) {
		this.vertexArrayBuffer = vertexArrayBuffer;
		this.version = 1;
	}

    /**
     * Parameterized constructor of Object3DData object taking a vertex buffer as param.
     *
     * @param verts         the vertices of the Object3DData object
     * @param normals       the normals of the Object3DData object
     * @param texCoords     the texture coordinates of the Object3DData object
     * @param faces         the faces of the Object3DData object, could be null in case of async loading
     * @param faceMats      the faces materials of the Object3DData object
     * @param materials     the materials of the Object3DData object
     */
	public Object3DData(FloatBuffer verts, FloatBuffer normals, ArrayList<Tuple3> texCoords, Faces faces, FaceMaterials faceMats, Materials materials) {
		super();
		this.vertexBuffer = verts;
		this.vertexNormalsBuffer = normals;
		this.texCoords = texCoords;
		this.faces = faces;
		this.faceMats = faceMats;
		this.materials = materials;
        this.visible = false;
	}

	/**
     * Method to check whether an Object3DData object is visible.
     *
     * @return the state of visibilty
     * */
	public boolean isVisible(){
        return visible;
    }

    /**
     * Method to set whether an Object3DData object is visible.
     *
     * @param visibility    the state of visibilty
     * */
    public void setVisible(boolean visibility){
        visible = visibility;
    }

    /**
     * Method to set whether an Object3DData object is visible.
     *
     * @param modelDimensions    the model dimensions of the WavefrontLoader.ModelDimensions object
     * */
	public void setDimensions(WavefrontLoader.ModelDimensions modelDimensions) {
		this.modelDimensions = modelDimensions;
	}

	/**
     * This method returns the opengl version to use to draw this object.
     *
     * @return the opengl version
     * */
	public int getVersion() {
		return version;
	}

	/**
     * This method sets the opengl version to use to draw this object.
     *
     * @param version   the opengl This method returns the opengl version to use to draw this object
     * */
	public Object3DData setVersion(int version) {
		this.version = version;
		return this;
	}

    /**
     * Method to check whether an Object3DData object has changed.
     *
     * @return whether the object has changed
     * */
	public boolean isChanged() {
		return changed;
	}

	/**
     * Method to set the Object3DData objects identifier.
     *
     * @param id    identifier for the Object3DData object
     * */
	public Object3DData setId(String id) {
		this.id = id;
		return this;
	}

	/**
     * Method to get the Object3DData objects identifier.
     *
     * @return the identifier of the Object3DData object
     * */
	public String getId() {
		return id;
	}

	/**
     * Method to return the Object3DData objects color.
     *
     * @return the color of the Object3DData object as String
     * */
	public float[] getColor() {
		return color;
	}

	/**
     * Method to set the Object3DData objects color.
     *
     * @param color     the new color of the Object3DData object
     * */
	public Object3DData setColor(float[] color) {
		this.color = color;
		return this;
	}

    /**
     * Method to return the Object3DData objects draw mode.
     * <p>
     *     for instance: GLES20.GL_TRIANGLES
     *
     * @return the drawing mode as float array
     */
	public int getDrawMode() {
		return drawMode;
	}

	/**
     * Method to set the Object3DData objects draw mode.
     * <p>
     *     for instance: GLES20.GL_TRIANGLES
     *
     * @param drawMode  the Object3DData objects draw mode
     * */
	public Object3DData setDrawMode(int drawMode) {
		this.drawMode = drawMode;
		return this;
	}

	/**
     * Method to return the draw size of an Object3DData object.
     *
     * @return the draw size of an Object3DData object as int
     * */
	public int getDrawSize() {
		return drawSize;
	}

	/**
     * Method to return the texture data of an Object3DData object.
     *
     * @return the byte array of the texture data of an Object3DData object
     * */
	public byte[] getTextureData() {
		return textureData;
	}

    /**
     * Method to set the texture data of an Object3DData object.
     *
     * @param textureData   the byte array of the texture data of an Object3DData object
     * */
	public void setTextureData(byte[] textureData) {
		this.textureData = textureData;
	}

	/**
     * Method to set the position of an Object3DData object.
     *
     * @param position  the float array containing the x y z position of an Object3DData object
     * */
	public Object3DData setPosition(float[] position) {
		this.position = position;
		return this;
	}

	/**
     * Method to return the position of an Object3DData object as float array.
     *
     * @return the float array of the x y z position of an Object3DData object
     * */
	public float[] getPosition() {
		return position;
	}

	/**
     * Method to return the position of an Object3DData object on the x axis.
     *
     * @return the x position of an Object3DData object as float value
     * */
	public float getPositionX() {
		return position != null ? position[0] : 0;
	}

    /**
     * Method to return the position of an Object3DData object on the y axis.
     *
     * @return the y position of an Object3DData object as float value
     * */
	public float getPositionY() {
		return position != null ? position[1] : 0;
	}

    /**
     * Method to return the position of an Object3DData object on the z axis.
     *
     * @return the z position of an Object3DData object as float value
     * */
	public float getPositionZ() {
		return position != null ? position[2] : 0;
	}

    /**
     * Method to return the rotation of an Object3DData object.
     *
     * @return the rotation of an Object3DData object as float array
     * */
	public float[] getRotation() {
		return rotation;
	}

    /**
     * Method to return the rotation coordinate of an Object3DData object on the z axis.
     *
     * @return the z rotation coordinate of an Object3DData object as float value
     * */
	public float getRotationZ() {
		return rotation != null ? rotation[2] : 0;
	}

	/**
     * Method to set the scaling of an Object3DData object.
     *
     * @param scale scaling of an Object3DData object as float array
     * */
	public Object3DData setScale(float[] scale){
		this.scale = scale;
		return this;
	}

    /**
     * Method to return the scaling of an Object3DData object.
     *
     * @return the scaling of an Object3DData object as float array
     * */
	public float[] getScale(){
		return scale;
	}

    /**
     * Method to return the scaling of the x coordinate of an Object3DData object.
     *
     * @return the scaling of the x coordinate an Object3DData object as float value
     * */
	public float getScaleX() {
		return getScale()[0];
	}

    /**
     * Method to return the scaling of the y coordinate of an Object3DData object.
     *
     * @return the scaling of the y coordinate an Object3DData object as float value
     * */
	public float getScaleY() {
		return getScale()[1];
	}

    /**
     * Method to return the scaling of the z coordinate of an Object3DData object.
     *
     * @return the scaling of the z coordinate an Object3DData object as float value
     * */
	public float getScaleZ() {
		return getScale()[2];
	}

	/**
     * Method to set the rotation of an Object3DData object.
     *
     * @param rotation the rotation coordinates as float array
     * */
	public Object3DData setRotation(float[] rotation) {
		this.rotation = rotation;
		return this;
	}

    /**
     * Method to set the rotation on the y axis of an Object3DData object.
     *
     * @param rotY the rotation coordinate on the y axis as float value
     * */
	public Object3DData setRotationY(float rotY) {
		this.rotation[1] = rotY;
		return this;
	}

	/**
     * Method to return the draw order of an Object3DData object as IntBuffer.
     *
     * @return the draw order as IntBuffer
     * */
	public IntBuffer getDrawOrder() {
		return drawOrderBuffer;
	}

    /**
     * Method to set the draw order of an Object3DData object as IntBuffer.
     *
     * @param drawBuffer    the draw order as IntBuffer
     * */
	public Object3DData setDrawOrder(IntBuffer drawBuffer) {
		this.drawOrderBuffer = drawBuffer;
		return this;
	}

	/**
     * Method to return the current directory of an Object3DData object as File.
     *
     * @return the current directory of an Object3DData object
     * */
	public File getCurrentDir() {
		return currentDir;
	}

	/**
     * Method to set the assets directory of an Object3DData object.
     *
     * @param assetsDir the current assets directory of an Object3DData object as String
     * */
	public void setAssetsDir(String assetsDir) {
		this.assetsDir = assetsDir;
	}

    /**
     * Method to return the assets directory of an Object3DData object.
     *
     * @return the current assets directory of an Object3DData object as String
     * */
	public String getAssetsDir() {
		return assetsDir;
	}

	/**
     * Method to detect whether to draw an Object3DData object using arrays.
     *
     * @return the boolean state whether to draw an Object3DData object using arrays
     * */
	public boolean isDrawUsingArrays() {
		return drawUsingArrays;
	}

    /**
     * Method to detect whether the texture coords of an Object3DData object are flipped.
     *
     * @return the boolean state whether the texture coords of an Object3DData object are flipped
     * */
	public boolean isFlipTextCoords() {
		return flipTextCoords;
	}

	/**
     * Method to set whether the Object3DData object uses arrays for drawing.
     *
     * @param drawUsingArrays the boolean state whether to draw an Object3DData object using arrays
     * */
	public Object3DData setDrawUsingArrays(boolean drawUsingArrays) {
		this.drawUsingArrays = drawUsingArrays;
		return this;
	}

	/**
     * Method to return the vertex buffer of an Object3DData object as FloatBuffer.
     *
     * @return the vertex buffer as FloatBuffer
     * */
	public FloatBuffer getVerts() {
		return vertexBuffer;
	}

    /**
     * Method to return the normals buffer of an Object3DData object as FloatBuffer.
     *
     * @return the normals buffer as FloatBuffer
     * */
	public FloatBuffer getNormals() {
		return vertexNormalsBuffer;
	}

    /**
     * Method to return the texture coordinates of an Object3DData object as an ArrayList using Tuple3.
     *
     * @return the texture coordinates as ArrayList
     * */
	public ArrayList<Tuple3> getTexCoords() {
		return texCoords;
	}

    /**
     * Method to return the faces of an Object3DData object as Faces.
     *
     * @return the faces as Faces
     * */
	public Faces getFaces() {
		return faces;
	}

    /**
     * Method to return the face materials of an Object3DData object as FaceMaterials.
     *
     * @return the face materials as FaceMaterials
     * */
	public FaceMaterials getFaceMats() {
		return faceMats;
	}

    /**
     * Method to return the materials of an Object3DData object as Materials.
     *
     * @return the materials as Materials
     * */
	public Materials getMaterials() {
		return materials;
	}

    /**
     * Method to return the vertex buffer of an Object3DData object as FloatBuffer.
     *
     * @return the vertex buffer as FloatBuffer
     * */
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

    /**
     * Method to set the vertex buffer of an Object3DData object as FloatBuffer.
     *
     * @param vertexBuffer  the vertex buffer of an Object3DData object
     * */
	public Object3DData setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
		return this;
	}

    /**
     * Method to return the vertex array buffer of an Object3DData object as FloatBuffer.
     *
     * @return the vertex array buffer as FloatBuffer
     * */
	public FloatBuffer getVertexArrayBuffer() {
		return vertexArrayBuffer;
	}

    /**
     * Method to set the vertex array buffer of an Object3DData object as FloatBuffer.
     *
     * @param vertexArrayBuffer  the vertex array buffer as FloatBuffer
     * */
	public Object3DData setVertexArrayBuffer(FloatBuffer vertexArrayBuffer) {
		this.vertexArrayBuffer = vertexArrayBuffer;
		return this;
	}

    /**
     * Method to return the vertex normals array buffer of an Object3DData object as FloatBuffer.
     *
     * @return the vertex normals array buffer as FloatBuffer
     * */
	public FloatBuffer getVertexNormalsArrayBuffer() {
		return vertexNormalsArrayBuffer;
	}

    /**
     * Method to set the vertex normals array buffer of an Object3DData object as FloatBuffer.
     *
     * @param vertexNormalsArrayBuffer  the vertex normals array buffer as FloatBuffer
     * */
	public Object3DData setVertexNormalsArrayBuffer(FloatBuffer vertexNormalsArrayBuffer) {
		this.vertexNormalsArrayBuffer = vertexNormalsArrayBuffer;
		return this;
	}

    /**
     * Method to return the texture coordinates array buffer of an Object3DData object as FloatBuffer.
     *
     * @return the texture coordinates array buffer as FloatBuffer
     * */
	public FloatBuffer getTextureCoordsArrayBuffer() {
		return textureCoordsArrayBuffer;
	}

    /**
     * Method to set the texture coordinates array buffer of an Object3DData object as FloatBuffer.
     *
     * @param textureCoordsArrayBuffer  the texture coordinates array buffer as FloatBuffer
     * */
	public Object3DData setTextureCoordsArrayBuffer(FloatBuffer textureCoordsArrayBuffer) {
		this.textureCoordsArrayBuffer = textureCoordsArrayBuffer;
		return this;
	}

	/**
     * Method to return the list of draw modes of an Object3DData object.
     * @return the list of draw modes
     * */
	public List<int[]> getDrawModeList() {
		return drawModeList;
	}

    /**
     * Method to return the vertex colors array buffer of an Object3DData object as FloatBuffer.
     *
     * @return the vertex colors array buffer as FloatBuffer
     * */
	public FloatBuffer getVertexColorsArrayBuffer() {
		return vertexColorsArrayBuffer;
	}

    /**
     * Method to set the vertex colors array buffer of an Object3DData object as FloatBuffer.
     *
     * @param vertexColorsArrayBuffer  the vertex colors array buffer as FloatBuffer
     * */
	public Object3DData setVertexColorsArrayBuffer(FloatBuffer vertexColorsArrayBuffer) {
		this.vertexColorsArrayBuffer = vertexColorsArrayBuffer;
		return this;
	}

    /**
     * Method to return the bounding box of a scene of an Object3DData object.
     *
     * @return the bounding box of a scene
     * */
	public BoundingBox getBoundingBox() {
		if (boundingBox == null && vertexBuffer != null) {
			float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE, yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE, zMin = Float.MAX_VALUE, zMax = Float.MIN_VALUE;
			FloatBuffer vertexBuffer = getVertexBuffer().asReadOnlyBuffer();
			vertexBuffer.position(0);
			while (vertexBuffer.hasRemaining()) {
				float vertexx = vertexBuffer.get();
				float vertexy = vertexBuffer.get();
				float vertexz = vertexBuffer.get();
				if (vertexx < xMin) {
					xMin = vertexx;
				}
				if (vertexx > xMax) {
					xMax = vertexx;
				}
				if (vertexy < yMin) {
					yMin = vertexy;
				}
				if (vertexy > yMax) {
					yMax = vertexy;
				}
				if (vertexz < zMin) {
					zMin = vertexz;
				}
				if (vertexz > zMax) {
					zMax = vertexz;
				}
			}
			boundingBox = new BoundingBox(  getId(),
                                            xMin+getPositionX(), xMax+getPositionX(),
                                            yMin+getPositionY(), yMax+getPositionY(),
                                            zMin+getPositionZ(), zMax+getPositionZ());
		}
		return boundingBox;
	}

    /**
     * Method to calculate the center and the scaling an Object3DData object.
     *
     * @param maxSize the maximum size of an Object3DData object
     * @return the new centered and scaled Object3DData object
     * */
    public Object3DData centerAndScale(float maxSize) {
        float leftPt = Float.MAX_VALUE, rightPt = Float.MIN_VALUE; // on x-axis
        float topPt = Float.MIN_VALUE, bottomPt = Float.MAX_VALUE; // on y-axis
        float farPt = Float.MAX_VALUE, nearPt = Float.MIN_VALUE; // on z-axis

        FloatBuffer vertexBuffer = getVertexArrayBuffer() != null ? getVertexArrayBuffer() : getVertexBuffer();
        if (vertexBuffer == null) {
            Log.v("Object3DData", "Scaling for '" + getId() + "No vertex data found.");
            return this;
        }

        for (int i = 0; i < vertexBuffer.capacity(); i += 3) {
            if (vertexBuffer.get(i) > rightPt)
                rightPt = vertexBuffer.get(i);
            else if (vertexBuffer.get(i) < leftPt)
                leftPt = vertexBuffer.get(i);
            if (vertexBuffer.get(i + 1) > topPt)
                topPt = vertexBuffer.get(i + 1);
            else if (vertexBuffer.get(i + 1) < bottomPt)
                bottomPt = vertexBuffer.get(i + 1);
            if (vertexBuffer.get(i + 2) > nearPt)
                nearPt = vertexBuffer.get(i + 2);
            else if (vertexBuffer.get(i + 2) < farPt)
                farPt = vertexBuffer.get(i + 2);
        }

        // calculate center of 3D object
        float xc = (rightPt + leftPt) / 2.0f;
        float yc = (topPt + bottomPt) / 2.0f;
        float zc = (nearPt + farPt) / 2.0f;

        // calculate largest dimension
        float height = topPt - bottomPt;
        float depth = nearPt - farPt;
        float largest = rightPt - leftPt;
        if (height > largest)
            largest = height;
        if (depth > largest)
            largest = depth;

        // calculate a scale factor
        float scaleFactor = 1.0f;
        if (largest != 0.0f)
            scaleFactor = (maxSize / largest);


        // modify the model's vertices
        for (int i = 0; i < vertexBuffer.capacity(); i += 3) {
            float x = vertexBuffer.get(i);
            float y = vertexBuffer.get(i + 1);
            float z = vertexBuffer.get(i + 2);
            x = (x - xc) * scaleFactor;
            y = (y - yc) * scaleFactor;
            z = (z - zc) * scaleFactor;
            vertexBuffer.put(i, x);
            vertexBuffer.put(i + 1, y);
            vertexBuffer.put(i + 2, z);
        }

        return this;
    }
}
