package rs.edu.educons.pma

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat.setBackground
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var arrayView: Array<View> //Ovde stoje arrayView zbog mogucnosti dodavanja vise modela kasnije
    lateinit var bearRenderable:ModelRenderable // Ovo omogucava pravljenje 3D modela, verteksa, i tekstura


    internal var selected = 1 //Bira se medved posto je prvi

    lateinit var arFragment:ArFragment //Pravi se ar fragment

    override fun onClick(view: View?) { // Kada se klikne na medveda, dobije se providna pozadina
        if (view!!.id==R.id.bear){
            selected = 1
            mySetBackground(view.id)
        }
    }

    private fun mySetBackground(id: Int) { // Ona slicica koja je trentno selektovana dobija transparentnu pozadinu
        for (i in arrayView.indices){
            if (arrayView[i].id==id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"))
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupArray()
        setupClickListener()
        setupModel()

        arFragment = supportFragmentManager.findFragmentById(R.id.scene_form_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent -> // Na bele tackice se postavlja model
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            createModel (anchorNode,selected)
        }
    }

    private fun createModel(anchorNode: AnchorNode, selected: Int) { // Kreiranje modela
        if(selected == 1) {
            val bear = TransformableNode(arFragment.transformationSystem)
            bear.setParent(anchorNode)
            bear.renderable = bearRenderable
            bear.select()
        }
    }

    private fun setupModel() {
        ModelRenderable.builder() // Sastavljanje modela zajedno sa teksturama
            .setSource(this, R.raw.bear)
            .build()
            .thenAccept{ modelRenderable -> bearRenderable = modelRenderable }
            .exceptionally {  throwable -> // Ukoliko dodje do greske, ispisuje se TOAST poruka
                Toast.makeText(this@MainActivity,"Greska pri ucitavanju", Toast.LENGTH_SHORT).show()
                null
            }


    }

    private fun setupClickListener() { // Cim se desi klik, izvrsava se kod
        for (i in arrayView.indices){
            arrayView[i].setOnClickListener(this)
        }

    }

    private fun setupArray() { // Punjenje liste
        arrayView=arrayOf(
            bear

        )
    }
}
