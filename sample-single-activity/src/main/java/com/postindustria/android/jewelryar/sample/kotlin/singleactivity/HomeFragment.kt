package com.postindustria.android.jewelryar.sample.kotlin.singleactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private val tryOnFirstRingButton: Button
        get() = requireView().findViewById(R.id.try_on_first_ring_button)

    private val tryOnSecondRingButton: Button
        get() = requireView().findViewById(R.id.try_on_second_ring_button)

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
            goToArScreen(FIRST_MODEL_ID)
        }
        tryOnSecondRingButton.setOnClickListener {
            goToArScreen(SECOND_MODEL_ID)
        }
    }

    private fun goToArScreen(ringId: Int) {
        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.ar_fragment,
//                JavaArFragment.newInstance(ringId)
                KotlinArFragment.newInstance(ringId)
            )
            .addToBackStack(null)
            .commit()
    }


    companion object {

        private const val FIRST_MODEL_ID = 1
        private const val SECOND_MODEL_ID = 2

        fun newInstance() = HomeFragment()
    }
}