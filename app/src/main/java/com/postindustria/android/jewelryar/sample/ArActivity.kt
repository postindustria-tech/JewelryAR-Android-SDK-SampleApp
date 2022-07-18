package com.postindustria.android.jewelryar.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.postindustria.android.jewelryar.views.ArView

class ArActivity : AppCompatActivity() {

    private val arView: ArView by lazy {
        findViewById(R.id.ar_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        intent.extras?.getInt(EXTRA_RING_ID)?.let { ringId ->
            arView.modelId = ringId
        }
    }

    override fun onDestroy() {
        arView.destroy()
        super.onDestroy()
    }


    companion object {

        const val EXTRA_RING_ID = "EXTRA_RING_ID"
    }
}