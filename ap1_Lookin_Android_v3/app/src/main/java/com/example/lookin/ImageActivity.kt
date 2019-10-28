@file:Suppress("DEPRECATION")

package net.kboy.sceneformsample.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_image.*
import net.kboy.sceneformsample.R
import net.kboy.sceneformsample.activity.menu.MainActivity
import net.kboy.sceneformsample.activity.menu.MainActivity2
import net.kboy.sceneformsample.fragment.ImageArFragment
import java.io.IOException

@Suppress("DEPRECATION")
class ImageActivity : AppCompatActivity() {

    private lateinit var arFragment: ImageArFragment
    private var shouldAddModel: Boolean = true
    private  var btnRenderable :ViewRenderable?=null
    var ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


        arFragment = sceneformFragment.let { it as ImageArFragment }
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)
        arFragment.arSceneView.scene.addOnUpdateListener {
            val frame = arFragment.arSceneView.arFrame
            val augmentedImages: Collection<AugmentedImage> = frame.getUpdatedTrackables(AugmentedImage::class.java)


            augmentedImages.forEach {
                if (it.trackingState == TrackingState.TRACKING && shouldAddModel){

                    /*同時に2つの対象が移った時の処理を考えると画像認識したタイミングやなくでID変えるんじゃなくて
                    マーカーをタップしたときにそのマーカー の画像参照する処理に変えたい*/
                    if (it.name == "en") {
                        ID = 111
                        Toast.makeText(applicationContext, "炎 ID =" + ID, Toast.LENGTH_SHORT).show()
                        placeObject(arFragment, it.createAnchor(it.centerPose),Uri.parse("model.sfb"))



                    }
                    if (it.name == "tanaka") {
                        ID = 999
                        Toast.makeText(applicationContext, "田中 ID = "+ ID, Toast.LENGTH_SHORT).show()
                        placeObject_c(arFragment, it.createAnchor(it.centerPose),Uri.parse("model.sfb"))




                    }
                }
            }
        }
    }





    fun setupAugmentedImageDb(config: Config, session: Session){
        val bitmap: Bitmap = loadAugmentedImage() ?: return
        val bitmap1: Bitmap = loadAugmentedImage1() ?: return
        val database = AugmentedImageDatabase(session)
        database.addImage("en", bitmap)
        database.addImage("tanaka", bitmap1)
        config.augmentedImageDatabase = database
    }

    //冗長感極まっとる　治したい
    private fun loadAugmentedImage(): Bitmap? {
            try {
                val inputStream = assets.open("en.jpg")
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                Log.d("imageLoad", "io exception while loading", e)
            }
        return null
    }

    private fun loadAugmentedImage1(): Bitmap? {
        try {
            val inputStream = assets.open("tanaka.jpg")
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Log.d("imageLoad", "io exception while loading", e)
        }
        return null
    }

    // FIXME: copied from MainActivity
    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
                .setSource(fragment.context, model)
                .build()
                .thenAccept {
                    addNodeToScene(fragment, anchor, it)

                }
                .exceptionally {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(it.message).setTitle("Error")
                    val dialog = builder.create()
                    dialog.show()

                    return@exceptionally null
                }


    }
    private fun placeObject_c(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
                .setSource(fragment.context, model)
                .build()
                .thenAccept {
                    addNodeToScene_c(fragment, anchor, it)

                }
                .exceptionally {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(it.message).setTitle("Error")
                    val dialog = builder.create()
                    dialog.show()

                    return@exceptionally null
                }


    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem).apply{
            setParent(anchorNode)
            fragment.arSceneView.scene.addChild(anchorNode)

            localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
        }
        node.renderable = renderable
        node.localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
        node.select()
        addName(anchorNode,node," ");

    }
    private fun addNodeToScene_c(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem).apply{
            setParent(anchorNode)
            fragment.arSceneView.scene.addChild(anchorNode)

            localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
        }
        node.renderable = renderable
        node.localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
        node.select()
        addName_c(anchorNode,node," ");

    }


    private fun addName(anchorNode: AnchorNode, node: TransformableNode, name: String) {
        ViewRenderable.builder().setView(this,R.layout.content_main)
                .build()
                .thenAccept { ViewRenderable ->
                    val nameView = TransformableNode(arFragment.transformationSystem).apply {
                        localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    }
                    nameView.localPosition = Vector3(0f,node.localPosition.y+0f,0f)
                    nameView.localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    nameView.setParent(anchorNode)
                    nameView.renderable = ViewRenderable
                    nameView.select()

                    val txt_name =ViewRenderable.view as TextView

                    val intent = Intent(this, MainActivity::class.java)
                    txt_name.text=name

                    txt_name.setOnClickListener{
                        startActivity(intent)
                    }

                }





    }


    private fun addName_c(anchorNode: AnchorNode, node: TransformableNode, name: String) {
        ViewRenderable.builder().setView(this, R.layout.content_main)
                .build()
                .thenAccept { ViewRenderable ->
                    val nameView = TransformableNode(arFragment.transformationSystem).apply {
                        localRotation = Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    }
                    nameView.localPosition = Vector3(0f, node.localPosition.y + 0f, 0f)
                    nameView.localRotation = Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    nameView.setParent(anchorNode)
                    nameView.renderable = ViewRenderable
                    nameView.select()

                    val txt_name = ViewRenderable.view as TextView

                    val intent = Intent(this, MainActivity2::class.java)
                    txt_name.text = name

                    txt_name.setOnClickListener {
                        startActivity(intent)
                    }

                }

    }
}