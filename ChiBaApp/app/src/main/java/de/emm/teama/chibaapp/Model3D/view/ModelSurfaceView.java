package de.emm.teama.chibaapp.Model3D.view;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.controller.TouchController;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ModelSurfaceView Class</h1>
 * This class provides a OpenGL drawing surface view, which is embedded inside the view hierachy.
 * <p>
 *
 * @author  Natalie Grasser
 * @version 1.0
 * @since   2017-06-28
 */

public class ModelSurfaceView extends GLSurfaceView {

	private MainFragment parent;
	private ModelRenderer mRenderer;
	private TouchController touchHandler;

    public ModelSurfaceView(MainFragment parent)
	{
		super(parent.getContext());

		// parent component
		this.parent = parent;

		// Create an OpenGL ES 2.0 environment.
		setEGLContextClientVersion(2);

		// This is the actual renderer of the 3D space
		mRenderer = new ModelRenderer(this);
		setRenderer(mRenderer);


		touchHandler = new TouchController(this, mRenderer);
	}


	/**
     * This method handles an touch event on an object of the GLSurfaceView.
     * */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return touchHandler.onTouchEvent(event);
	}

	public MainFragment getMainFragment() {
		return parent;
	}

}