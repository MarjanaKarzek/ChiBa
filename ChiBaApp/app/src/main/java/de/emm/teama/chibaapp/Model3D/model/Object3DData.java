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

	// opengl version to use to draw this object
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

	public Object3DData(FloatBuffer vertexArrayBuffer) {
		this.vertexArrayBuffer = vertexArrayBuffer;
		this.version = 1;
	}

	public Object3DData(FloatBuffer verts, FloatBuffer normals, ArrayList<Tuple3> texCoords, Faces faces,
			FaceMaterials faceMats, Materials materials) {
		super();
		this.vertexBuffer = verts;
		this.vertexNormalsBuffer = normals;
		this.texCoords = texCoords;
		this.faces = faces;  // parameter "faces" could be null in case of async loading
		this.faceMats = faceMats;
		this.materials = materials;
        this.visible = false;
	}

	public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visibility){
        visible = visibility;
    }

	public void setDimensions(WavefrontLoader.ModelDimensions modelDimensions) {
		this.modelDimensions = modelDimensions;
	}

	public WavefrontLoader.ModelDimensions getDimensions() {
		return modelDimensions;
	}

	public int getVersion() {
		return version;
	}

	public Object3DData setVersion(int version) {
		this.version = version;
		return this;
	}

	public boolean isChanged() {
		return changed;
	}

	public Object3DData setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return id;
	}

	public float[] getColor() {
		return color;
	}

	public Object3DData setColor(float[] color) {
		this.color = color;
		return this;
	}

	public int getDrawMode() {
		return drawMode;
	}

	public Object3DData setDrawMode(int drawMode) {
		this.drawMode = drawMode;
		return this;
	}

	public int getDrawSize() {
		return drawSize;
	}

	public byte[] getTextureData() {
		return textureData;
	}

	public void setTextureData(byte[] textureData) {
		this.textureData = textureData;
	}

	public Object3DData setPosition(float[] position) {
		this.position = position;
		return this;
	}

	public float[] getPosition() {
		return position;
	}

	public float getPositionX() {
		return position != null ? position[0] : 0;
	}

	public float getPositionY() {
		return position != null ? position[1] : 0;
	}

	public float getPositionZ() {
		return position != null ? position[2] : 0;
	}

	public float[] getRotation() {
		return rotation;
	}

	public float getRotationZ() {
		return rotation != null ? rotation[2] : 0;
	}

	public Object3DData setScale(float[] scale){
		this.scale = scale;
		return this;
	}

	public float[] getScale(){
		return scale;
	}

	public float getScaleX() {
		return getScale()[0];
	}

	public float getScaleY() {
		return getScale()[1];
	}

	public float getScaleZ() {
		return getScale()[2];
	}

	public Object3DData setRotation(float[] rotation) {
		this.rotation = rotation;
		return this;
	}

	public Object3DData setRotationY(float rotY) {
		this.rotation[1] = rotY;
		return this;
	}

	public IntBuffer getDrawOrder() {
		return drawOrderBuffer;
	}

	public Object3DData setDrawOrder(IntBuffer drawBuffer) {
		this.drawOrderBuffer = drawBuffer;
		return this;
	}

	public File getCurrentDir() {
		return currentDir;
	}

	public void setAssetsDir(String assetsDir) {
		this.assetsDir = assetsDir;
	}

	public String getAssetsDir() {
		return assetsDir;
	}

	public boolean isDrawUsingArrays() {
		return drawUsingArrays;
	}

	public boolean isFlipTextCoords() {
		return flipTextCoords;
	}

	public Object3DData setDrawUsingArrays(boolean drawUsingArrays) {
		this.drawUsingArrays = drawUsingArrays;
		return this;
	}

	public FloatBuffer getVerts() {
		return vertexBuffer;
	}

	public FloatBuffer getNormals() {
		return vertexNormalsBuffer;
	}

	public ArrayList<Tuple3> getTexCoords() {
		return texCoords;
	}

	public Faces getFaces() {
		return faces;
	}

	public FaceMaterials getFaceMats() {
		return faceMats;
	}

	public Materials getMaterials() {
		return materials;
	}

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public Object3DData setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
		return this;
	}

	public FloatBuffer getVertexArrayBuffer() {
		return vertexArrayBuffer;
	}

	public Object3DData setVertexArrayBuffer(FloatBuffer vertexArrayBuffer) {
		this.vertexArrayBuffer = vertexArrayBuffer;
		return this;
	}

	public FloatBuffer getVertexNormalsArrayBuffer() {
		return vertexNormalsArrayBuffer;
	}

	public Object3DData setVertexNormalsArrayBuffer(FloatBuffer vertexNormalsArrayBuffer) {
		this.vertexNormalsArrayBuffer = vertexNormalsArrayBuffer;
		return this;
	}

	public FloatBuffer getTextureCoordsArrayBuffer() {
		return textureCoordsArrayBuffer;
	}

	public Object3DData setTextureCoordsArrayBuffer(FloatBuffer textureCoordsArrayBuffer) {
		this.textureCoordsArrayBuffer = textureCoordsArrayBuffer;
		return this;
	}

	public List<int[]> getDrawModeList() {
		return drawModeList;
	}

	public Object3DData setDrawModeList(List<int[]> drawModeList) {
		this.drawModeList = drawModeList;
		return this;
	}

	public FloatBuffer getVertexColorsArrayBuffer() {
		return vertexColorsArrayBuffer;
	}

	public Object3DData setVertexColorsArrayBuffer(FloatBuffer vertexColorsArrayBuffer) {
		this.vertexColorsArrayBuffer = vertexColorsArrayBuffer;
		return this;
	}

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

    public Object3DData centerAndScale(float maxSize) {
        float leftPt = Float.MAX_VALUE, rightPt = Float.MIN_VALUE; // on x-axis
        float topPt = Float.MIN_VALUE, bottomPt = Float.MAX_VALUE; // on y-axis
        float farPt = Float.MAX_VALUE, nearPt = Float.MIN_VALUE; // on z-axis

        FloatBuffer vertexBuffer = getVertexArrayBuffer() != null ? getVertexArrayBuffer() : getVertexBuffer();
        if (vertexBuffer == null) {
            Log.v("Object3DData", "Scaling for '" + getId() + "' I found that there is no vertex data");
            return this;
        }

//        Log.i("Object3DData", "Calculating dimensions for '" + getId() + "...");
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

        // this.setOriginalPosition(new float[]{-xc,-yc,-zc});

        // calculate largest dimension
        float height = topPt - bottomPt;
        float depth = nearPt - farPt;
        float largest = rightPt - leftPt;
        if (height > largest)
            largest = height;
        if (depth > largest)
            largest = depth;
//        Log.i("Object3DData", "Largest dimension ["+largest+"]");

        // scale object

        // calculate a scale factor
        float scaleFactor = 1.0f;
        // System.out.println("Largest dimension: " + largest);
        if (largest != 0.0f)
            scaleFactor = (maxSize / largest);
//        Log.i("Object3DData", "Centering & scaling '" + getId() + "' to (" + xc + "," + yc + "," + zc + ") scale: '" + scaleFactor + "'");

        // this.setOriginalScale(new float[]{scaleFactor,scaleFactor,scaleFactor});

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
