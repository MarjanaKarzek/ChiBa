package de.emm.teama.chibaapp.Model3D.sceneloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

/**
 * @author Andrew Davison
 * @since February 2007
 * Title: WavefrontLoader.java
 * Contact: ad@fivedots.coe.psu.ac.th
 * <p>
 *     Load the OBJ model from MODEL_DIR, centering and scaling it.
 *     The scale comes from the sz argument in the constructor, and
 *     is implemented by changing the vertices of the loaded model.
 *
 *     The model can have vertices, normals and tex coordinates, and
 *     refer to materials in a MTL file.
 *
 *     The OpenGL commands for rendering the model are stored in
 *     a display list (modelDispList), which is drawn by calls to
 *     draw().
 *
 *     Information about the model is printed to stdout.
 *
 *     Based on techniques used in the OBJ loading code in the
 *     JautOGL multiplayer racing game by Evangelos Pournaras
 *     (http://today.java.net/pub/a/today/2006/10/10/
 *     development-of-3d-multiplayer-racing-game.html
 *     and https://jautogl.dev.java.net/), and the
 *     Asteroids tutorial by Kevin Glass
 *     (http://www.cokeandcode.com/asteroidstutorial)
 *
 * */

public class WavefrontLoader {

	private static final float DUMMY_Z_TC = -5.0f;
	static final boolean INDEXES_START_AT_1 = true;
	private boolean hasTCs3D = false;

	private ArrayList<Tuple3> texCoords;

	private Faces faces;                    // model faces
	private FaceMaterials faceMats;         // materials used by faces
	private Materials materials;            // materials defined in MTL file
	private ModelDimensions modelDims;      // model dimensions

	private String modelNm;                 // without path or ".OBJ" extension
	private float maxSize;                  // for scaling the model

	// metadata
	int numVerts = 0;
	int numTextures = 0;
	int numNormals = 0;
	int numFaces = 0;
	int numVertsReferences = 0;

	// buffers
	private FloatBuffer vertsBuffer;
	private FloatBuffer normalsBuffer;
	private FloatBuffer textureCoordsBuffer;

	// flags
	private final int triangleMode = GLES20.GL_TRIANGLE_FAN;

	public WavefrontLoader(String nm) {
		modelNm = nm;
		maxSize = 1.0F;

		texCoords = new ArrayList<Tuple3>();


		faceMats = new FaceMaterials();
		modelDims = new ModelDimensions();
	}

	/**
     * Getter methods for vertex buffer, normal buffer, texture coordinates, face materials, materials and dimensions.*/
	public FloatBuffer getVerts() {
		return vertsBuffer;
	}

	public FloatBuffer getNormals() {
		return normalsBuffer;
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

	public ModelDimensions getDimensions() {
		return modelDims;
	}

	/**
     *  Count verts, normals, faces etc and reserve buffers to save the data. */
	public void analyzeModel(InputStream is) {
		int lineNum = 0;
		String line;


		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				lineNum++;
				line = line.trim();
				if (line.length() > 0) {

					if (line.startsWith("v ")) {            // vertex
						numVerts++;
					} else if (line.startsWith("vt")) {     // tex coord
						numTextures++;
					} else if (line.startsWith("vn")) {     // normal
						numNormals++;
					} else if (line.startsWith("f ")) {     // face
						final int faceSize;
						if (line.contains("  ")) {
							faceSize = line.split(" +").length - 1;
						} else {
							faceSize = line.split(" ").length - 1;
						}
						numFaces += (faceSize - 2);
						// (faceSize-2)x3 = converting polygon to triangles
						numVertsReferences += (faceSize - 2) * 3;
					} else if (line.startsWith("mtllib "))  // build material
					{
						materials = new Materials(line.substring(7));

					} else if (line.startsWith("usemtl ")) {    // use material
					} else if (line.charAt(0) == 'g') {         // group name
						// not implemented
					} else if (line.charAt(0) == 's') {         // smoothing group
						// not implemented
					} else if (line.charAt(0) == '#')           // comment line
						continue;
					else if (line.charAt(0) == 'o')             // object group
						continue;
					else
						System.out.println("Ignoring line " + lineNum + " : " + line);
				}
			}
		} catch (IOException e) {
			Log.e("WavefrontLoader", "Problem reading line '" + (++lineNum) + "'");
			Log.e("WavefrontLoader", e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e("WavefrontLoader", e.getMessage(), e);
				}
			}
		}
	}

	/**
     * Allocate buffers for pushing the model data */
	public void allocateBuffers() {
		// size = 3 (x,y,z) * 4 (bytes per float)
		vertsBuffer = createNativeByteBuffer(numVerts*3*4).asFloatBuffer();
		normalsBuffer = createNativeByteBuffer(numNormals*3*4).asFloatBuffer();
		textureCoordsBuffer = createNativeByteBuffer(numTextures*3*4).asFloatBuffer();
		IntBuffer buffer = createNativeByteBuffer(numFaces*3*4).asIntBuffer();
		faces = new Faces(numFaces, buffer, vertsBuffer, normalsBuffer, texCoords);
	}

	/**
     * Method to load the model.*/
	public void loadModel(InputStream is) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			readModel(br);
		} finally{
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
     * Method to initialize vertex byte buffer for shape coordinates and use the device hardware's native byte order.
     * */
	private static ByteBuffer createNativeByteBuffer(int length) {
		ByteBuffer bb = ByteBuffer.allocateDirect(length);
		bb.order(ByteOrder.nativeOrder());
		return bb;
	}

	/* Method to parse the OBJ file line-by-line */
	private void readModel(BufferedReader br) {
		boolean isLoaded = true;

		int lineNum = 0;
		String line;
		boolean isFirstCoord = true;
		boolean isFirstTC = true;
		int numFaces = 0;

		int vertNumber = 0;
		int normalNumber = 0;


		try {
			while (((line = br.readLine()) != null)) {
				lineNum++;
				line = line.trim();
				if (line.length() > 0) {

					if (line.startsWith("v ")) {                    // vertex
						isLoaded = addVert(vertsBuffer, vertNumber++ * 3, line, isFirstCoord, modelDims) && isLoaded;
						if (isFirstCoord)
							isFirstCoord = false;
					} else if (line.startsWith("vt")) {             // tex coord
						isLoaded = addTexCoord(line, isFirstTC) && isLoaded;
						if (isFirstTC)
							isFirstTC = false;
					} else if (line.startsWith("vn"))               // normal
						isLoaded = addVert(normalsBuffer, normalNumber++ * 3,line, isFirstCoord, null) && isLoaded;
					else if (line.startsWith("f ")) {               // face
						isLoaded = faces.addFace(line) && isLoaded;
						numFaces++;
					} else if (line.startsWith("mtllib ")){         // build material

                    } else if (line.startsWith("usemtl "))          // use material
						faceMats.addUse(numFaces, line.substring(7));
					else if (line.charAt(0) == 'g') {               // group name
						// not implemented
					} else if (line.charAt(0) == 's') {             // smoothing group
						// not implemented
					} else if (line.charAt(0) == '#')               // comment line
						continue;
					else if (line.charAt(0) == 'o')                 // object group
						continue;
					else
						System.out.println("Ignoring line " + lineNum + " : " + line);
				}
			}
		} catch (IOException e) {
			Log.e("WavefrontLoader",e.getMessage(),e);
			throw new RuntimeException(e);
		}

		if (!isLoaded) {
			Log.e("WavefrontLoader","Error loading model");
		}
	}

	/**
	 * Parse the vertex and add it to the buffer. If the vertex cannot be parsed,
	 * then a default (0,0,0) vertex is added instead.
	 *
	 * @param buffer the buffer where the vertex is to be added
	 * @param offset the offset of the buffer
	 * @param line the vertex to parse
	 * @param isFirstCoord if this is the first vertex to be parsed
	 * @param dimensions the model dimesions so they are updated
	 * @return <code>true</code> if the vertex could be parsed, <code>false</code> otherwise
	 */

	/* Add vertex from line "v x y z" to vert ArrayList, and update the model dimension's info. */
    private boolean addVert(FloatBuffer buffer, int offset, String line, boolean isFirstCoord, ModelDimensions dimensions) {
		float x=0,y=0,z=0;
		try{
			String[] tokens = null;
			if (line.contains("  ")){
				tokens = line.split(" +");
			}
			else{
				tokens = line.split(" ");
			}
			x = Float.parseFloat(tokens[1]);
			y = Float.parseFloat(tokens[2]);
			z = Float.parseFloat(tokens[3]);

			if (dimensions != null) {
				if (isFirstCoord)
					modelDims.set(x, y, z);
				else
					modelDims.update(x, y, z);
			}

			return true;

		}catch(NumberFormatException ex){
			Log.e("WavefrontLoader",ex.getMessage());
		} finally{
			// try to build even with errors
			buffer.put(offset, x).put(offset+1, y).put(offset+2, z);
		}

		return false;
	}

    /* Add the texture coordinate from the line "vt u v w" to the texCoords ArrayList. There may only be two tex coords on the line, which is determined by looking at the first tex coord line. */
    private boolean addTexCoord(String line, boolean isFirstTC) {
		if (isFirstTC) {
			hasTCs3D = checkTC3D(line);
		}

		Tuple3 texCoord = readTCTuple(line);
		if (texCoord != null) {
			texCoords.add(texCoord);
			return true;
		}

		return false;
	}

    /* Check if the line has 4 tokens, which will be the "vt" token and 3 tex coords in this case. */
	private boolean checkTC3D(String line) {
		String[] tokens = line.split("\\s+");
		return (tokens.length == 4);
	}

	/* The line starts with a "vt" OBJ word and two or three floats (x, y, z) for the tex coords separated by spaces. If there are only two coords, then the z-value is assigned a dummy value, DUMMY_Z_TC. */
	private Tuple3 readTCTuple(String line) {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		tokens.nextToken(); // skip "vt" OBJ word

		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());

			float z = DUMMY_Z_TC;
			if (hasTCs3D)
				z = Float.parseFloat(tokens.nextToken());
			return new Tuple3(x, y, z);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	/**
     * Helper Class to provide tuples. */
	public static class Tuple3 {
		private float x, y, z;

		public Tuple3(float xc, float yc, float zc) {
			x = xc;
			y = yc;
			z = zc;
		}

		public String toString() {
			return "( " + x + ", " + y + ", " + z + " )";
		}

		public void setX(float xc) {
			x = xc;
		}

		public float getX() {
			return x;
		}

		public void setY(float yc) {
			y = yc;
		}

		public float getY() {
			return y;
		}

		public void setZ(float zc) {
			z = zc;
		}

		public float getZ() {
			return z;
		}

	}

	/**
     * Helper Class to set the correct model dimensions.*/
	public static class ModelDimensions {
		private float leftPt, rightPt;  // on x-axis
		private float topPt, bottomPt;  // on y-axis
		private float farPt, nearPt;    // on z-axis

		private DecimalFormat df = new DecimalFormat("0.##");

		public ModelDimensions() {
			leftPt = 0.0f;
			rightPt = 0.0f;
			topPt = 0.0f;
			bottomPt = 0.0f;
			farPt = 0.0f;
			nearPt = 0.0f;
		}

        /* Initialization for the model's edge coordinates */
		public void set(float x, float y, float z) {
			rightPt = x;
			leftPt = x;

			topPt = y;
			bottomPt = y;

			nearPt = z;
			farPt = z;
		}

        /* Method for updating the edge coordinates using vert.*/
        public void update(float x, float y, float z)
		{
			if (x > rightPt)
				rightPt = x;
			if (x < leftPt)
				leftPt = x;

			if (y > topPt)
				topPt = y;
			if (y < bottomPt)
				bottomPt = y;

			if (z > nearPt)
				nearPt = z;
			if (z < farPt)
				farPt = z;
		}

		public float getWidth() {
			return (rightPt - leftPt);
		}

		public float getHeight() {
			return (topPt - bottomPt);
		}

		public float getDepth() {
			return (nearPt - farPt);
		}

		public float getLargest() {
			float height = getHeight();
			float depth = getDepth();

			float largest = getWidth();
			if (height > largest)
				largest = height;
			if (depth > largest)
				largest = depth;

			return largest;
		}

		public Tuple3 getCenter() {
			float xc = (rightPt + leftPt) / 2.0f;
			float yc = (topPt + bottomPt) / 2.0f;
			float zc = (nearPt + farPt) / 2.0f;
			return new Tuple3(xc, yc, zc);
		}

	}

	public static class Materials {

		public Map<String, Material> materials;     // stores the Material objects built from the MTL file data
		private String mfnm;                        // private File file;

		public Materials(String mtlFnm) {
			materials = new LinkedHashMap<String, Material>();
			this.mfnm = mtlFnm;
		}

		public void readMaterials(File currentDir, String assetsDir, AssetManager am) {
			try {
				InputStream is;
				if (currentDir != null) {
					File file = new File(currentDir, mfnm);
					System.out.println("Loading material from " + file);
					is = new FileInputStream(file);
				} else {
					System.out.println("Loading material from " + mfnm);
					is = am.open(assetsDir + "/" + mfnm);
				}
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				readMaterials(br);
				br.close();
			} catch (FileNotFoundException ex){
				Log.w("WavefrontLoader", ex.getMessage());
			} catch (IOException e) {
				Log.e("WavefrontLoader", e.getMessage(), e);
			}

		}

		/*
		 * The functionality to parse the MTL file line-by-line, building Material objects which are collected in the materials ArrayList
		 * is the main task of this method.
		 * */
		private void readMaterials(BufferedReader br) {
			try {
				String line;
				Material currMaterial = null;               // current material

				while (((line = br.readLine()) != null)) {
					line = line.trim();
					if (line.length() == 0)
						continue;

					if (line.startsWith("newmtl ")) {       // new material
						if (currMaterial != null)           // save previous material
							materials.put(currMaterial.getName(), currMaterial);

						// start collecting info for new material
						String name = line.substring(7);
						currMaterial = new Material(name);
					} else if (line.startsWith("map_Kd ")) {        // texture filename
						String textureFilename = line.substring(7);
						Log.d("Loader", "New texture found: " + textureFilename);
						currMaterial.setTexture(textureFilename);
					} else if (line.startsWith("Ka "))              // ambient colour
						currMaterial.setKa(readTuple3(line));
					else if (line.startsWith("Kd "))                // diffuse colour
						currMaterial.setKd(readTuple3(line));
					else if (line.startsWith("Ks "))                // specular colour
						currMaterial.setKs(readTuple3(line));
					else if (line.startsWith("Ns ")) {              // shininess
						float val = Float.valueOf(line.substring(3)).floatValue();
						currMaterial.setNs(val);
					} else if (line.charAt(0) == 'd') {             // alpha
						float val = Float.valueOf(line.substring(2)).floatValue();
						currMaterial.setD(val);
					} else if (line.startsWith("Tr ")) {            // Transparency (inverted)
						float val = Float.valueOf(line.substring(3)).floatValue();
						currMaterial.setD(1 - val);
					} else if (line.startsWith("illum ")) {         // illumination model
						// not implemented
					} else if (line.charAt(0) == '#')               // comment line
						continue;
					else
						System.out.println("Ignoring MTL line: " + line);

				}
				if (currMaterial != null) {
					materials.put(currMaterial.getName(), currMaterial);
				}
			} catch (Exception e) {
				Log.e("materials", e.getMessage(), e);
			}
		}

        /*
         * The line starts with an MTL word such as Ka, Kd, Ks, and the three floats (x, y, z) separated by spaces
         */
		private Tuple3 readTuple3(String line) {
			StringTokenizer tokens = new StringTokenizer(line, " ");
			tokens.nextToken();

			try {
				float x = Float.parseFloat(tokens.nextToken());
				float y = Float.parseFloat(tokens.nextToken());
				float z = Float.parseFloat(tokens.nextToken());

				return new Tuple3(x, y, z);
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}

			return null;
		}

		public Material getMaterial(String name) {
			return materials.get(name);
		}

	}

	public static class Material {
		private String name;

        private Tuple3 ka, kd, ks;  // ambient, diffuse, specular colours
		private float ns;           // shininess
		private float d;            // alpha

		private String texFnm;
		private String texture;

		public Material(String nm) {
            name = nm;

            d = 1.0f;
            ns = 0.0f;
            ka = null;
            kd = null;
            ks = null;

            texFnm = null;
            texture = null;
        }

		public void setD(float val) {
			d = val;
		}

		public float getD() {
			return d;
		}

		public void setNs(float val) {
			ns = val;
		}

		public float getNs() {
			return ns;
		}

		public void setKa(Tuple3 t) {
			ka = t;
		}

		public Tuple3 getKa() {
			return ka;
		}

		public void setKd(Tuple3 t) {
			kd = t;
		}

		public Tuple3 getKd() {
			return kd;
		}

		public float[] getKdColor() {
			if (kd == null) {
				return null;
			}
			return new float[] { kd.getX(), kd.getY(), kd.getZ(), getD() };
		}

		public void setKs(Tuple3 t) {
			ks = t;
		}

		public Tuple3 getKs() {
			return ks;
		}

		public void setTexture(String t) {
			texture = t;
		}

		public String getTexture() {
			return texture;
		}

		String getName() {
			return name;
		}

	}

	public class Faces {
		private static final float DUMMY_Z_TC = -5.0f;

		public final int totalFaces;
		public IntBuffer facesVertIdxs;         // indices for verticesused by each face
		public ArrayList<int[]> facesTexIdxs;   // indices for tex coords used by each face
		public ArrayList<int[]> facesNormIdxs;  // indices for normals used by each face

		private FloatBuffer normals;
		private ArrayList<Tuple3> texCoords;

		private int facesLoadCounter;
		private int faceVertexLoadCounter = 0;
		private int verticesReferencesCount;

		Faces(int totalFaces, IntBuffer buffer, FloatBuffer vs, FloatBuffer ns, ArrayList<Tuple3> ts) {
			this.totalFaces = totalFaces;
			normals = ns;
			texCoords = ts;

			facesVertIdxs = buffer;
			facesTexIdxs = new ArrayList<int[]>();
			facesNormIdxs = new ArrayList<int[]>();
		}

		public int getSize(){
			return totalFaces;
		}

		/* This method returns true if all faces are loaded. */
		public boolean loaded(){
			return facesLoadCounter == totalFaces;
		}

		/* Thes method gets the face's indicies from line "f v/vt/vn ..." with vt or vn index values perhaps being absent. */
		public boolean addFace(String line) {
			try {
				line = line.substring(2);
				String[] tokens = null;
				if (line.contains("  ")){
					tokens = line.split(" +");
				}
				else{
					tokens = line.split(" ");
				}
				int numTokens = tokens.length;

				int vt[] = null;
				int vn[] = null;


				for (int i = 0, faceIndex = 0; i < numTokens; i++, faceIndex++) {
                    if (faceIndex > 2){
						faceIndex = 0;

						facesLoadCounter++;
						verticesReferencesCount += 3;
						if (vt != null)  facesTexIdxs.add(vt);
						if (vn != null) facesNormIdxs.add(vn);

						vt = null;
						vn = null;

						i -= 2;
					}
                    String faceToken = null;
					if (WavefrontLoader.this.triangleMode == GLES20.GL_TRIANGLE_FAN) {
						if (faceIndex == 0){
							faceToken = tokens[0];
						}else{
							faceToken = tokens[i];
						}
					}
					else {
						faceToken = tokens[i];
					}

					String[] faceTokens = faceToken.split("/");
					int numSeps = faceTokens.length;

					int vertIdx = Integer.parseInt(faceTokens[0]);

					if (numSeps > 1){
						if (vt == null)	vt = new int[3];
						try{
							vt[faceIndex] = Integer.parseInt(faceTokens[1]);
						}catch(NumberFormatException ex){
							vt[faceIndex] = 0;
						}
					}
					if (numSeps > 2){
						if (vn == null)	vn = new int[3];
						try{
							vn[faceIndex] = Integer.parseInt(faceTokens[2]);
						}catch(NumberFormatException ex){
							vn[faceIndex] = 0;
						}
					}

					if (WavefrontLoader.INDEXES_START_AT_1) {
						vertIdx--;
						if (vt != null)	vt[faceIndex] = vt[faceIndex] - 1;
						if (vn != null) vn[faceIndex] = vn[faceIndex] - 1;
					}
					facesVertIdxs.put(faceVertexLoadCounter++,vertIdx);
				}
				if (vt != null)  facesTexIdxs.add(vt);
				if (vn != null) facesNormIdxs.add(vn);

				facesLoadCounter++;
				verticesReferencesCount += 3;

			} catch (NumberFormatException e) {
				Log.e("WavefrontLoader",e.getMessage(),e);
				return false;
			}
			return true;
		}


		public int getVerticesReferencesCount() {
			return getSize()*3;
		}

		public IntBuffer getIndexBuffer(){return facesVertIdxs;}

	}

	public static class FaceMaterials {
		private HashMap<Integer, String> faceMats;
        private HashMap<String, Integer> matCount;

		public FaceMaterials() {
			faceMats = new HashMap<Integer, String>();
			matCount = new HashMap<String, Integer>();
		}

		public void addUse(int faceIdx, String matName) {
			if (faceMats.containsKey(faceIdx))                  // face index already present
				System.out.println("Face index " + faceIdx + " changed to use material " + matName);
			faceMats.put(faceIdx, matName);

			if (matCount.containsKey(matName)) {
				int i = (Integer) matCount.get(matName) + 1;
				matCount.put(matName, i);
			} else
				matCount.put(matName, 1);
		}

		public String findMaterial(int faceIdx) {
			return (String) faceMats.get(faceIdx);
		}

		public boolean isEmpty() {
			return faceMats.isEmpty() || this.matCount.isEmpty();
		}

	}

}
