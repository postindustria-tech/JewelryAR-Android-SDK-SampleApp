package com.postindustria.android.jewelryar.sample.kotlin.singleactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.postindustria.android.jewelryar.views.ArView

class KotlinHomeFragment : Fragment() {

    private val tryOnFirstRingButton: Button
        get() = requireView().findViewById(R.id.try_on_first_ring_button)

    private val tryOnSecondRingButton: Button
        get() = requireView().findViewById(R.id.try_on_second_ring_button)

    private val modelProgressTextView: TextView
        get() = requireView().findViewById(R.id.model_progress_text_view)

    private val tfModelProgressTextView: TextView
        get() = requireView().findViewById(R.id.tf_model_progress_text_view)

    private val homeRootConstraintLayout: ConstraintLayout
        get() = requireView().findViewById(R.id.home_root_constraint_layout)

    private var arView: ArView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        tryOnFirstRingButton.setOnClickListener {
            showArExperience(FIRST_MODEL_ID)
        }
        tryOnSecondRingButton.setOnClickListener {
            showArExperience(SECOND_MODEL_ID)
        }
    }

    private fun showArExperience(ringId: Int) {
        createArViewIfNotExists()
        arView?.modelId = ringId
        modelProgressTextView.bringToFront()
        tfModelProgressTextView.bringToFront()
    }

    private fun createArViewIfNotExists() {
        if (arView == null) {
            arView = ArView.withApiKey(
                requireActivity(),
                apiKey = "spWyH9aA-OEL-Bl27KHAeQ",
                apiUrl = "https://stage-api-ar.postindustria.com/v1",
                sslCertFingerprint = "B802CE0DE1C57C53414CDEAE9253123475828F39",
            ).apply {
                addToRoot(this)
                lifecycle.addObserver(this)
                listenArProgress(this)
            }
        }
    }

    private fun addToRoot(arView: ArView) {
        arView.id = View.generateViewId()
        val constraintSet = ConstraintSet()
        homeRootConstraintLayout.addView(arView)
        constraintSet.apply {
            clone(homeRootConstraintLayout)
            connect(
                arView.id,
                ConstraintSet.TOP,
                homeRootConstraintLayout.id,
                ConstraintSet.TOP
            )
            connect(
                arView.id,
                ConstraintSet.BOTTOM,
                homeRootConstraintLayout.id,
                ConstraintSet.BOTTOM
            )
            connect(
                arView.id,
                ConstraintSet.START,
                homeRootConstraintLayout.id,
                ConstraintSet.START
            )
            connect(
                arView.id,
                ConstraintSet.END,
                homeRootConstraintLayout.id,
                ConstraintSet.END
            )
            applyTo(homeRootConstraintLayout)
        }
    }

    private fun listenArProgress(arView: ArView) {
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
        requireActivity().runOnUiThread {
            modelProgressTextView.isVisible = visibility
        }
    }

    private fun changeTfModelProgressVisibility(visibility: Boolean) {
        requireActivity().runOnUiThread {
            tfModelProgressTextView.isVisible = visibility
        }
    }


    companion object {

        private const val FIRST_MODEL_ID = 1
        private const val SECOND_MODEL_ID = 2

        fun newInstance() = KotlinHomeFragment()
    }
}