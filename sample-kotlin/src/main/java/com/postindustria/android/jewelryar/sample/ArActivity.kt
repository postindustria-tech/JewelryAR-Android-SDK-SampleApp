package com.postindustria.android.jewelryar.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.postindustria.android.jewelryar.views.ArView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArActivity : AppCompatActivity() {

    private val arView: ArView by lazy {
        findViewById(R.id.ar_view)
    }

    private val modelProgressBar: TextView by lazy {
        findViewById(R.id.model_progress_text_view)
    }

    private val tfModelProgressBar: TextView by lazy {
        findViewById(R.id.tf_model_progress_text_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        lifecycle.addObserver(arView)
        updateModelIdFromIntent()
        setListeners()
    }

    private fun updateModelIdFromIntent() {
        intent.extras?.getInt(EXTRA_RING_ID)?.let { ringId ->
            arView.modelId = ringId
        }
    }

    private fun setListeners() {
        arView.onDownloadProgress = ArView.DownloadProgressListener { downloadProgress ->
            downloadProgress.currentDownloads?.let { currentDownloads ->
                val isAnyModelDownloadInProgress =
                    currentDownloads.models?.any { downloadOperation ->
                        !downloadOperation.isFinished
                    } ?: false
                val isAnyTfModelDownloadInProgress =
                    currentDownloads.tfModels?.any { downloadOperation ->
                        !downloadOperation.isFinished
                    } ?: false
                changeModelProgressVisibility(isAnyModelDownloadInProgress)
                changeTfModelProgressVisibility(isAnyTfModelDownloadInProgress)
            }
        }
    }

    private fun changeModelProgressVisibility(visibility: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {
            modelProgressBar.isVisible = visibility
        }
    }

    private fun changeTfModelProgressVisibility(visibility: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {
            tfModelProgressBar.isVisible = visibility
        }
    }


    companion object {

        const val EXTRA_RING_ID = "EXTRA_RING_ID"
    }
}