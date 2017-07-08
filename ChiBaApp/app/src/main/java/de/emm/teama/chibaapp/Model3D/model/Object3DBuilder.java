package de.emm.teama.chibaapp.Model3D.model;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader;
import de.emm.teama.chibaapp.Model3D.sceneloader.WavefrontLoader.*;
import de.emm.teama.chibaapp.Model3D.util.Math3DUtils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public final class Object3DBuilder {

	public interface Callback {
		void onLoadError(Exception ex);

		void onLoadComplete(Object3DData data);

		void onBuildComplete(Object3DData data);
	}

	private static final int COORDS_PER_VERTEX = 3;

	private static float[] DEFAULT_COLOR = {1.0f, 1.0f, 0, 1.0f};

	private Object3DV0 object3dv0;
	private Object3DV1 object3dv1;
    private Object3DV6 object3dv6;

	public static Object3DData buildPoint(float[] point) {
		return new Object3DData(createNativeByteBuffer(point.length * 4).asFloatBuffer().put(point))
				.setDrawMode(GLES20.GL_POINTS);
	}

	public static Object3DData loadObj(AssetManager assets, String assetDir, String assetFilename) {
		try {
			final String modelId = assetDir + "/" + assetFilename;

			InputStream is = assets.open(modelId);
			WavefrontLoader wfl = new WavefrontLoader(assetFilename);
			wfl.analyzeModel(is);
			is.close();

			wfl.allocateBuffers();

			is = assets.open(modelId);
			wfl.loadModel(is);
			is.close();

			Object3DData data3D = new Object3DData(wfl.getVerts(), wfl.getNormals(), wfl.getTexCoords(), wfl.getFaces(), wfl.getFaceMats(), wfl.getMaterials());
			data3D.setId(assetFilename);
			data3D.setAssetsDir(assetDir);
			data3D.setDimensions(wfl.getDimensions());

			data3D.setDrawMode(GLES20.GL_TRIANGLES);
			generateArrays(assets, data3D);

			return data3D;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object3D getDrawer(Object3DData obj, boolean usingTextures, boolean usingLights) throws IOException {

		if (object3dv1 == null) {
			object3dv1 = new Object3DV1();
            object3dv6 = new Object3DV6();
		}

        if (usingTextures && usingLights
                && obj.getVertexColorsArrayBuffer() != null
                && obj.getTextureData() != null
                && obj.getTextureCoordsArrayBuffer() != null
                && obj.getVertexNormalsArrayBuffer() != null
                && obj.getVertexNormalsArrayBuffer() != null) {
            return object3dv6;
		} else {
			return object3dv1;
		}
	}

	public static Object3DData generateArrays(AssetManager assets, Object3DData obj) throws IOException {

		Faces faces = obj.getFaces(); // model faces
		FaceMaterials faceMats = obj.getFaceMats();
		Materials materials = obj.getMaterials();

		final FloatBuffer vertexArrayBuffer = createNativeByteBuffer(faces.getVerticesReferencesCount() * 3 * 4).asFloatBuffer();
		obj.setVertexArrayBuffer(vertexArrayBuffer);
		obj.setDrawUsingArrays(true);

		final FloatBuffer vertexBuffer = obj.getVerts();
		final IntBuffer indexBuffer = faces.getIndexBuffer();
		for (int i = 0; i < faces.getVerticesReferencesCount(); i++) {
			vertexArrayBuffer.put(i*3,vertexBuffer.get(indexBuffer.get(i) * 3));
			vertexArrayBuffer.put(i*3+1,vertexBuffer.get(indexBuffer.get(i) * 3 + 1));
			vertexArrayBuffer.put(i*3+2,vertexBuffer.get(indexBuffer.get(i) * 3 + 2));
		}

        // Normals buffer size = Number_of_faces X 3 (vertices_per_face) X 3 (coords_per_normal) X 4 (bytes_per_float)
		final FloatBuffer vertexNormalsArrayBuffer = createNativeByteBuffer(faces.getSize() * 3 * 3 * 4).asFloatBuffer();;
		obj.setVertexNormalsArrayBuffer(vertexNormalsArrayBuffer);

		// build file normals
		final FloatBuffer vertexNormalsBuffer = obj.getNormals();
		if (vertexNormalsBuffer.capacity() > 0) {
			for (int n=0; n<faces.facesNormIdxs.size(); n++) {
				int[] normal = faces.facesNormIdxs.get(n);
				for (int i = 0; i < normal.length; i++) {
					vertexNormalsArrayBuffer.put(n*9+i*3,vertexNormalsBuffer.get(normal[i] * 3));
					vertexNormalsArrayBuffer.put(n*9+i*3+1,vertexNormalsBuffer.get(normal[i] * 3 + 1));
					vertexNormalsArrayBuffer.put(n*9+i*3+2,vertexNormalsBuffer.get(normal[i] * 3 + 2));
				}
			}
		} else {
			// calculate normals for all triangles
			final float[] v0 = new float[3], v1 = new float[3], v2 = new float[3];
			for (int i = 0; i < faces.getIndexBuffer().capacity(); i += 3) {
				try {
					v0[0] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3);
					v0[1] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3 + 1);
					v0[2] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3 + 2);

					v1[0] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3);
					v1[1] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3 + 1);
					v1[2] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3 + 2);

					v2[0] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3);
					v2[1] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3 + 1);
					v2[2] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3 + 2);

					float[] normal = Math3DUtils.calculateFaceNormal2(v0, v1, v2);

					vertexNormalsArrayBuffer.put(i*3,normal[0]);
					vertexNormalsArrayBuffer.put(i*3+1,normal[1]);
					vertexNormalsArrayBuffer.put(i*3+2,normal[2]);
					vertexNormalsArrayBuffer.put(i*3+3,normal[0]);
					vertexNormalsArrayBuffer.put(i*3+4,normal[1]);
					vertexNormalsArrayBuffer.put(i*3+5,normal[2]);
					vertexNormalsArrayBuffer.put(i*3+6,normal[0]);
					vertexNormalsArrayBuffer.put(i*3+7,normal[1]);
					vertexNormalsArrayBuffer.put(i*3+8,normal[2]);
				} catch (BufferOverflowException ex) {
					throw new RuntimeException("Error calculating mormal for face ["+i/3+"]");
				}
			}
		}


		FloatBuffer colorArrayBuffer = null;
		if (materials != null) {
			materials.readMaterials(obj.getCurrentDir(), obj.getAssetsDir(), assets);
		}

		if (materials != null && !faceMats.isEmpty()) {
			colorArrayBuffer = createNativeByteBuffer(4 * faces.getVerticesReferencesCount() * 4)
					.asFloatBuffer();
			boolean anyOk = false;
			float[] currentColor = DEFAULT_COLOR;
			for (int i = 0; i < faces.getSize(); i++) {
				if (faceMats.findMaterial(i) != null) {
					Material mat = materials.getMaterial(faceMats.findMaterial(i));
					if (mat != null) {
						currentColor = mat.getKdColor() != null ? mat.getKdColor() : currentColor;
						anyOk = anyOk || mat.getKdColor() != null;
					}
				}
				colorArrayBuffer.put(currentColor);
				colorArrayBuffer.put(currentColor);
				colorArrayBuffer.put(currentColor);
			}
			if (!anyOk) {
				colorArrayBuffer = null;
			}
		}
		obj.setVertexColorsArrayBuffer(colorArrayBuffer);


		String texture = null;
		byte[] textureData = null;
		if (materials != null && !materials.materials.isEmpty()) {
            for (Material mat : materials.materials.values()) {
				if (mat.getTexture() != null) {
					texture = mat.getTexture();
					break;
				}
			}
			if (texture != null) {
				if (obj.getCurrentDir() != null) {
					File file = new File(obj.getCurrentDir(), texture);
					Log.i("Object3DBuilder", "Loading texture '" + file + "'...");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					FileInputStream fis = new FileInputStream(file);
                    IOUtils.copy(fis, bos);
                    fis.close();
					textureData = bos.toByteArray();
					bos.close();
				} else {
					String assetResourceName = obj.getAssetsDir() + "/" + texture;
					Log.i("Object3DBuilder", "Loading texture '" + assetResourceName + "'...");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					InputStream fis = assets.open(assetResourceName);
                    IOUtils.copy(fis, bos);
                    fis.close();
					textureData = bos.toByteArray();
					bos.close();
				}
			} else {
				Log.i("Object3DBuilder", "Found material(s) but no texture");
			}
		} else{
			Log.i("Object3DBuilder", "No materials -> No texture");
		}


		if (textureData != null) {
			ArrayList<Tuple3> texCoords = obj.getTexCoords();
			if (texCoords != null && texCoords.size() > 0) {

				FloatBuffer textureCoordsBuffer = createNativeByteBuffer(texCoords.size() * 2 * 4).asFloatBuffer();
				for (Tuple3 texCor : texCoords) {
					textureCoordsBuffer.put(texCor.getX());
					textureCoordsBuffer.put(obj.isFlipTextCoords() ? 1 - texCor.getY() : texCor.getY());
				}

				FloatBuffer textureCoordsArraysBuffer = createNativeByteBuffer(2 * faces.getVerticesReferencesCount() * 4).asFloatBuffer();
				obj.setTextureCoordsArrayBuffer(textureCoordsArraysBuffer);

				try {

					boolean anyTextureOk = false;
					String currentTexture = null;

					int counter = 0;
					for (int i = 0; i < faces.facesTexIdxs.size(); i++) {

						// get current texture
						if (!faceMats.isEmpty() && faceMats.findMaterial(i) != null) {
							Material mat = materials.getMaterial(faceMats.findMaterial(i));
							if (mat != null && mat.getTexture() != null) {
								currentTexture = mat.getTexture();
							}
						}

						// check if texture is ok (Because we only support 1 texture currently)
						boolean textureOk = false;
						if (currentTexture != null && currentTexture.equals(texture)) {
							textureOk = true;
						}

						// populate texture coords if ok
						int[] text = faces.facesTexIdxs.get(i);
						for (int j = 0; j < text.length; j++) {
							if (textureOk) {
								anyTextureOk = true;
								textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[j] * 2));
								textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[j] * 2 + 1));
							} else {
								textureCoordsArraysBuffer.put(counter++, 0f);
								textureCoordsArraysBuffer.put(counter++, 0f);
							}
						}
					}

					if (!anyTextureOk) {
						Log.i("Object3DBuilder", "Texture is wrong. Applying global texture");
						counter = 0;
						for (int j=0; j<faces.facesTexIdxs.size(); j++) {
							int[] text = faces.facesTexIdxs.get(j);
							for (int i = 0; i < text.length; i++) {
								textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[i] * 2));
								textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[i] * 2 + 1));
							}
						}
					}
				} catch (Exception ex) {
					Log.e("Object3DBuilder", "Failure to load texture coordinates", ex);
				}
			}
		}
		obj.setTextureData(textureData);

		return obj;
	}

	public Object3D getFaceNormalsDrawer() {
		return object3dv1;
	}

	public Object3D getPointDrawer() {
		if (object3dv0 == null) {
			object3dv0 = new Object3DV0();
		}
		return object3dv0;
	}

	/**
	 * Builds a wireframe of the model by drawing all lines (3) of the triangles. This method uses
	 * the drawOrder buffer.
	 * @param objData the 3d model
	 * @return the 3d wireframe
	 */
	public static Object3DData buildWireframe(Object3DData objData) {

		if (objData.getDrawOrder() != null) {

			try {
				IntBuffer drawBuffer = objData.getDrawOrder();
				IntBuffer wireframeDrawOrder = createNativeByteBuffer(drawBuffer.capacity() * 2 * 4).asIntBuffer();
				for (int i = 0; i < drawBuffer.capacity(); i += 3) {
					int v0 = drawBuffer.get(i);
					int v1 = drawBuffer.get(i + 1);
					int v2 = drawBuffer.get(i + 2);
					if (objData.isDrawUsingArrays()) {
						v0 = i;
						v1 = i + 1;
						v2 = i + 2;
					}
					wireframeDrawOrder.put(v0);
					wireframeDrawOrder.put(v1);
					wireframeDrawOrder.put(v1);
					wireframeDrawOrder.put(v2);
					wireframeDrawOrder.put(v2);
					wireframeDrawOrder.put(v0);
				}
				return new Object3DData(objData.getVertexArrayBuffer()).setVertexBuffer(objData.getVertexBuffer()).setDrawOrder(wireframeDrawOrder).
						setVertexNormalsArrayBuffer(objData.getVertexNormalsArrayBuffer()).setColor(objData.getColor())
						.setVertexColorsArrayBuffer(objData.getVertexColorsArrayBuffer()).setTextureCoordsArrayBuffer(objData.getTextureCoordsArrayBuffer())
						.setPosition(objData.getPosition()).setRotation(objData.getRotation()).setScale(objData.getScale())
						.setDrawMode(GLES20.GL_LINES).setDrawUsingArrays(false);
			} catch (Exception ex) {
				Log.e("Object3DBuilder", ex.getMessage(), ex);
			}
		}
		else if (objData.getVertexArrayBuffer() != null){
			FloatBuffer vertexBuffer = objData.getVertexArrayBuffer();
			IntBuffer wireframeDrawOrder = createNativeByteBuffer(vertexBuffer.capacity()/3 * 2 * 4).asIntBuffer();
			for (int i = 0; i < vertexBuffer.capacity()/3; i += 3) {
				wireframeDrawOrder.put(i);
				wireframeDrawOrder.put(i+1);
				wireframeDrawOrder.put(i+1);
				wireframeDrawOrder.put(i+2);
				wireframeDrawOrder.put(i+2);
				wireframeDrawOrder.put(i);
			}
			return new Object3DData(objData.getVertexArrayBuffer()).setVertexBuffer(objData.getVertexBuffer()).setDrawOrder(wireframeDrawOrder).
					setVertexNormalsArrayBuffer(objData.getVertexNormalsArrayBuffer()).setColor(objData.getColor())
					.setVertexColorsArrayBuffer(objData.getVertexColorsArrayBuffer()).setTextureCoordsArrayBuffer(objData.getTextureCoordsArrayBuffer())
					.setPosition(objData.getPosition()).setRotation(objData.getRotation()).setScale(objData.getScale())
					.setDrawMode(GLES20.GL_LINES).setDrawUsingArrays(false);
		}
		return objData;
	}

	/**
	 * Generate a new object that contains all the line normals for all the faces for the specified object
	 * <p>
	 * This only works for objects made of triangles.
	 *
	 * @param obj the object to which we calculate the normals.
	 * @return the model with all the normal lines
	 */
	public static Object3DData buildFaceNormals(Object3DData obj) {
		if (obj.getDrawMode() != GLES20.GL_TRIANGLES) {
			return null;
		}

		FloatBuffer vertexBuffer = obj.getVertexArrayBuffer() != null ? obj.getVertexArrayBuffer()
				: obj.getVertexBuffer();
		if (vertexBuffer == null) {
			Log.v("Builder", "Generating face normals for '" + obj.getId() + "' I found that there is no vertex data");
			return null;
		}

		FloatBuffer normalsLines;
		IntBuffer drawBuffer = obj.getDrawOrder();
		if (drawBuffer != null) {
			int size = 2 * 3 * (drawBuffer.capacity() / 3) * 4;
			normalsLines = createNativeByteBuffer(size).asFloatBuffer();
			drawBuffer.position(0);
			for (int i = 0; i < drawBuffer.capacity(); i += 3) {
				int v1 = drawBuffer.get() * COORDS_PER_VERTEX;
				int v2 = drawBuffer.get() * COORDS_PER_VERTEX;
				int v3 = drawBuffer.get() * COORDS_PER_VERTEX;
				float[][] normalLine = Math3DUtils.calculateFaceNormal(
						new float[]{vertexBuffer.get(v1), vertexBuffer.get(v1 + 1), vertexBuffer.get(v1 + 2)},
						new float[]{vertexBuffer.get(v2), vertexBuffer.get(v2 + 1), vertexBuffer.get(v2 + 2)},
						new float[]{vertexBuffer.get(v3), vertexBuffer.get(v3 + 1), vertexBuffer.get(v3 + 2)});
				normalsLines.put(normalLine[0]).put(normalLine[1]);
			}
		} else {
			if (vertexBuffer.capacity() % (/* COORDS_PER_VERTEX */3 * /* VERTEX_PER_FACE */ 3) != 0) {
				// something in the data is wrong
				Log.v("Builder", "Generating face normals for '" + obj.getId() + "' I found that vertices are not multiple of 9 (3*3): " + vertexBuffer.capacity());
				return null;
			}

			normalsLines = createNativeByteBuffer(6 * vertexBuffer.capacity() / 9 * 4).asFloatBuffer();
			vertexBuffer.position(0);
			for (int i = 0; i < vertexBuffer.capacity() / /* COORDS_PER_VERTEX */ 3 / /* VERTEX_PER_FACE */3; i++) {
				float[][] normalLine = Math3DUtils.calculateFaceNormal(
						new float[]{vertexBuffer.get(), vertexBuffer.get(), vertexBuffer.get()},
						new float[]{vertexBuffer.get(), vertexBuffer.get(), vertexBuffer.get()},
						new float[]{vertexBuffer.get(), vertexBuffer.get(), vertexBuffer.get()});
				normalsLines.put(normalLine[0]).put(normalLine[1]);

			}
		}

		return new Object3DData(normalsLines).setDrawMode(GLES20.GL_LINES).setColor(obj.getColor())
				.setPosition(obj.getPosition()).setVersion(1);
	}

	private static ByteBuffer createNativeByteBuffer(int length) {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(length);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		return bb;
	}

}