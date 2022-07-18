package com.postindustria.android.jewelryar.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.postindustria.android.jewelryar.sample.ArActivity.Companion.EXTRA_RING_ID

class MainActivity : AppCompatActivity() {

    private val tryOnFirstRingButton: Button by lazy {
        findViewById(R.id.try_on_first_ring_button)
    }

    private val tryOnSecondRingButton: Button by lazy {
        findViewById(R.id.try_on_second_ring_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tryOnFirstRingButton.setOnClickListener {
            goToArActivity(FIRST_MODEL_ID)
        }
        tryOnSecondRingButton.setOnClickListener {
            goToArActivity(SECOND_MODEL_ID)
        }
    }

    private fun goToArActivity(ringId: Int) {
        Intent(this, ArActivity::class.java).apply {
            putExtra(EXTRA_RING_ID, ringId)
            startActivity(this)
        }
    }


    companion object {

        private const val FIRST_MODEL_ID = 1
        private const val SECOND_MODEL_ID = 2
    }
}