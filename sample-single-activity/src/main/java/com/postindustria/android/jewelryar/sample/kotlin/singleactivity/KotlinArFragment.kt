package com.postindustria.android.jewelryar.sample.kotlin.singleactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.postindustria.android.jewelryar.views.ArView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KotlinArFragment : Fragment() {

    private val ringId: Int
        get() = requireArguments().getInt(EXTRA_RING_ID)

    private val arView: ArView
        get() = requireView().findViewById(R.id.ar_view)

    private val modelProgressTextView: TextView
        get() = requireView().findViewById(R.id.model_progress_text_view)

    private val tfModelProgressTextView: TextView
        get() = requireView().findViewById(R.id.tf_model_progress_text_view)

    private val scope: CoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(arView)
        setListeners()
        updateModelIdFromIntent()
    }

    private fun updateModelIdFromIntent() {
        arView.modelId = ringId
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
        scope.launch(Dispatchers.Main) {
            modelProgressTextView.isVisible = visibility
        }
    }

    private fun changeTfModelProgressVisibility(visibility: Boolean) {
        scope.launch(Dispatchers.Main) {
            tfModelProgressTextView.isVisible = visibility
        }
    }


    companion object {

        const val EXTRA_RING_ID = "EXTRA_RING_ID"

        fun newInstance(modelId: Int) = KotlinArFragment().apply {
            arguments = bundleOf(EXTRA_RING_ID to modelId)
        }
    }
}