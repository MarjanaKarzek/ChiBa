package de.emm.teama.chibaapp.Model3D.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.emm.teama.chibaapp.Model3D.entities.Camera;
import de.emm.teama.chibaapp.Model3D.model.Object3D;
import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;
import de.emm.teama.chibaapp.Model3D.model.Object3DImpl;
import de.emm.teama.chibaapp.Model3D.sceneloader.SceneLoader;
import de.emm.teama.chibaapp.Model3D.util.GLUtil;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.widget.Toast;

/**
 * <h1>ModelRenderer Class</h1>
 * This class implements the generic GLSurfaceView.Renderer interface and is responsible for OpenGL calls to render a frame.
 * <p>
 *
 * @author Andres Oviedo, modified by Natalie Grasser
 * @version 1.0
 * @since 2017-04-23, modified 2017-06-28
 * Title: Android 3D Model Viewer
 * Availability: https://github.com/andresoviedo/android-3D-model-viewer
 *
 */

public class ModelRenderer implements GLSurfaceView.Renderer {

    private final static String TAG = ModelRenderer.class.getName();

    private ModelSurfaceView main;      // 3D window (parent component)
    private int width;                  // width of the screen
    private int height;                 // height of the screen
    private Camera camera;              // Out point of view handler

    private float near = 1f;            // frustrum - nearest pixel
    private float far = 10f;            // frustrum - fartest pixel

    private Object3DBuilder drawer;

    private Map<Object3DData, Object3DData> wireframes = new HashMap<Object3DData, Object3DData>();     // The wireframe associated shape (it should be made of lines only)
    private Map<byte[], Integer> textures = new HashMap<byte[], Integer>();    // The loaded textures
    private Map<Object3DData, Object3DData> normals = new HashMap<Object3DData, Object3DData>();        // The corresponding opengl bounding boxes

    /* 3D matrices to project our 3D world */
    private final float[] modelProjectionMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];

    private final float[] mvpMatrix = new float[16];                            // mvpMatrix is an abbreviation for "Model View Projection Matrix"

    private final float[] lightPosInEyeSpace = new float[4];                    // light position required to render with lighting

    /**
     * Construct a new renderer for the specified surface view
     *
     * @param modelSurfaceView the 3D window
     */
    public ModelRenderer(ModelSurfaceView modelSurfaceView) {
        this.main = modelSurfaceView;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        float[] backgroundColor = main.getMainFragment().getBackgroundColor();
        GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);

        // Enable depth testing for hidden-surface elimination.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Enable blending for combining colors when there is transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Lets create our 3D world components
        camera = new Camera();

        // This component will draw the actual models using OpenGL
        drawer = new Object3DBuilder();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;

        // Adjust the viewport based on geometry changes, such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // INFO: Set the camera position (View matrix)
        // The camera has 3 vectors (the position, the vector where we are looking at, and the up position (sky)
        Matrix.setLookAtM(modelViewMatrix, 0, camera.xPos, camera.yPos, camera.zPos, camera.xView, camera.yView, camera.zView, camera.xUp, camera.yUp, camera.zUp);

        // the projection matrix is the 3D virtual space (cube) that we want to project
        float ratio = (float) width / height;
        Matrix.frustumM(modelProjectionMatrix, 0, -ratio, ratio, -1, 1, getNear(), getFar());

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // recalculate mvp matrix according to where we are looking at now
        if (camera.hasChanged()) {
            Matrix.setLookAtM(modelViewMatrix, 0, camera.xPos, camera.yPos, camera.zPos, camera.xView, camera.yView,
                    camera.zView, camera.xUp, camera.yUp, camera.zUp);
            // Log.d("Camera", "Changed! :"+camera.ToStringVector());
            Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
            camera.setChanged(false);
        }

        SceneLoader scene = main.getMainFragment().getScene();
        if (scene == null) {return;}    // scene not ready

        camera.setScene(scene);         // camera for the scene

        scene.onDrawFrame();            // animate scene

        if (scene.isDrawLighting()) {   // draw light

            Object3DImpl lightBulbDrawer = (Object3DImpl) drawer.getPointDrawer();

            float[] lightModelViewMatrix = lightBulbDrawer.getMvMatrix(lightBulbDrawer.getMMatrix(scene.getLightBulb()), modelViewMatrix);

            // Calculate position of the light in eye space to support lighting
            Matrix.multiplyMV(lightPosInEyeSpace, 0, lightModelViewMatrix, 0, scene.getLightBulb().getPosition(), 0);

            // Draw a point that represents the light bulb
            lightBulbDrawer.draw(scene.getLightBulb(), modelProjectionMatrix, modelViewMatrix, -1, lightPosInEyeSpace);
        }

        List<Object3DData> objects = scene.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            Object3DData objData = objects.get(i);
            if (objData.isVisible()) {
                try {
                    boolean changed = objData.isChanged();
                    Object3D drawerObject = drawer.getDrawer(objData, scene.isDrawTextures(), scene.isDrawLighting());

                    Integer textureId = textures.get(objData.getTextureData());
                    if (textureId == null && objData.getTextureData() != null) {
                        ByteArrayInputStream textureIs = new ByteArrayInputStream(objData.getTextureData());
                        textureId = GLUtil.loadTexture(textureIs);
                        textureIs.close();
                        textures.put(objData.getTextureData(), textureId);
                    }

                    if (scene.isDrawWireframe() && objData.getDrawMode() != GLES20.GL_POINTS
                            && objData.getDrawMode() != GLES20.GL_LINES && objData.getDrawMode() != GLES20.GL_LINE_STRIP
                            && objData.getDrawMode() != GLES20.GL_LINE_LOOP) {
                        try {
                            Object3DData wireframe = wireframes.get(objData);
                            if (wireframe == null || changed) {
                                wireframe = Object3DBuilder.buildWireframe(objData);
                                wireframes.put(objData, wireframe);
                            }
                            drawerObject.draw(wireframe, modelProjectionMatrix, modelViewMatrix, wireframe.getDrawMode(),
                                    wireframe.getDrawSize(), textureId != null ? textureId : -1, lightPosInEyeSpace);
                        } catch (Error e) {
                            Log.e("ModelRenderer", e.getMessage(), e);
                        }
                    } else if (scene.isDrawPoints() || (objData.getFaces() != null && !objData.getFaces().loaded())) {
                        drawerObject.draw(objData, modelProjectionMatrix, modelViewMatrix
                                , GLES20.GL_POINTS, objData.getDrawSize(),
                                textureId != null ? textureId : -1, lightPosInEyeSpace);
                    } else {
                        drawerObject.draw(objData, modelProjectionMatrix, modelViewMatrix,
                                textureId != null ? textureId : -1, lightPosInEyeSpace);
                    }

                    if (scene.isDrawNormals()) {
                        Object3DData normalData = normals.get(objData);
                        if (normalData == null || changed) {
                            normalData = Object3DBuilder.buildFaceNormals(objData);
                            if (normalData != null) {
                                normals.put(objData, normalData);
                            }
                        }
                        if (normalData != null) {
                            Object3D normalsDrawer = drawer.getFaceNormalsDrawer();
                            normalsDrawer.draw(normalData, modelProjectionMatrix, modelViewMatrix, -1, null);
                        }
                    }

                } catch (IOException ex) {
                    Toast.makeText(main.getMainFragment().getActivity().getApplicationContext(),
                            "There was a problem creating 3D object", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Getter methods for width and height of the screen.
     * As well as model projection matrix, model view matrix and camera.
     *
     * */
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float[] getModelProjectionMatrix() {
        return modelProjectionMatrix;
    }

    public float[] getModelViewMatrix() {
        return modelViewMatrix;
    }

    public Camera getCamera() {
        return camera;
    }
}