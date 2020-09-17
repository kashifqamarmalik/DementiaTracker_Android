package com.example.dementiatracker


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    lateinit var arFragment: ArFragment
    lateinit var modelRenderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment;
        setUpModel()
        setUpPlane()
    }


    fun setUpModel(){
        ModelRenderable.builder()
                //.setSource(this, R.raw.gltf)
                .setSource(this, RenderableSource.builder().setSource(
                        this,
                        Uri.parse("https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/CesiumMan/glTF/CesiumMan.gltf"),
                        RenderableSource.SourceType.GLTF2)
                        .setScale(0.5f)  // Scale the original model to 50%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(R.raw.asim)
                .build()
                .thenAccept{it -> modelRenderable = it}
    }

    fun setUpPlane() {
        arFragment.setOnTapArPlaneListener{ hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? ->
            if(modelRenderable == null) {
                return@setOnTapArPlaneListener
            }
            val anchor= hitResult!!.createAnchor()
            val anchorNode= AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            CreateModel(anchorNode)
    }
}

    fun CreateModel(anchorNode: AnchorNode){
        val viewNode= TransformableNode(arFragment.transformationSystem)
        viewNode.setParent(anchorNode)
        viewNode.renderable= modelRenderable
        viewNode.select()

    }


}