package de.emm.teama.chibaapp.Model3D.view;

import de.emm.teama.chibaapp.Main.MainFragment;
import de.emm.teama.chibaapp.Model3D.controller.TouchController;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class ModelSurfaceView extends GLSurfaceView {

	private MainFragment parent;
	private ModelRenderer mRenderer;
	private TouchController touchHandler;

	public ModelSurfaceView(MainFragment parent)
	{
		super(parent.getContext());

		// parent component
		this.parent = parent;

		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);

		// This is the actual renderer of the 3D space
		mRenderer = new ModelRenderer(this);
		setRenderer(mRenderer);

		touchHandler = new TouchController(this, mRenderer);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return touchHandler.onTouchEvent(event);
	}

	public MainFragment getMainFragment() {
		return parent;
	}

}