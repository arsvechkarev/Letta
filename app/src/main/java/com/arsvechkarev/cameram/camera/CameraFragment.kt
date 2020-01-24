package com.arsvechkarev.cameram.camera

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.arsvechkarev.cameram.R
import kotlinx.android.synthetic.main.fragment_camera.textureView
import timber.log.Timber
import java.io.IOException


@Suppress("DEPRECATION")
class CameraFragment : Fragment(R.layout.fragment_camera), TextureView.SurfaceTextureListener {
  
  private var camera: Camera? = null
  private var surfaceTexture: SurfaceTexture? = null
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    textureView.surfaceTextureListener = this
  }
  
  override fun onSurfaceTextureAvailable(
    surface: SurfaceTexture?,
    width: Int,
    height: Int
  ) {
    surfaceTexture = surface
    startPreview()
  }
  
  override fun onSurfaceTextureSizeChanged(
    surface: SurfaceTexture?,
    width: Int,
    height: Int
  ) {
    // Ignored, Camera does all the work for us
  }
  
  override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    // Invoked every time there's a new Camera preview frame
    Timber.d("updated, ts=%s", surface?.timestamp)
  }
  
  override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
    camera!!.stopPreview()
    camera!!.release()
    return true
  }
  
  private fun startPreview() {
    camera = Camera.open()
    if (camera == null) {
      // Seeing this on Nexus 7 2012 -- I guess it wants a rear-facing camera, but
      // there isn't one.  TODO: fix
      throw RuntimeException("Default camera not available")
    }
    try {
      camera!!.setPreviewTexture(surfaceTexture)
      val display = getSystemService(requireContext(), WindowManager::class.java)!!.defaultDisplay
      if (display.rotation == Surface.ROTATION_0) {
        camera!!.setDisplayOrientation(90)
      }
      if (display.rotation == Surface.ROTATION_270) {
        camera!!.setDisplayOrientation(180)
      }
      camera!!.startPreview()
    } catch (ioe: IOException) { // Something bad happened
      Log.e("QWERTY", "Exception starting preview", ioe)
    }
  }
  
}