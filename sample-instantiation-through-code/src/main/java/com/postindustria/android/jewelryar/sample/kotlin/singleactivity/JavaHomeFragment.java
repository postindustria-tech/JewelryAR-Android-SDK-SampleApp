package com.postindustria.android.jewelryar.sample.kotlin.singleactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.postindustria.android.jewelryar.unity.communication.events.entities.CurrentDownloads;
import com.postindustria.android.jewelryar.unity.communication.events.entities.DownloadOperation;
import com.postindustria.android.jewelryar.views.ArView;

import java.util.List;

public class JavaHomeFragment extends Fragment {

    private static final int FIRST_MODEL_ID = 1;
    private static final int SECOND_MODEL_ID = 2;

    private Button tryOnFirstRingButton;
    private Button tryOnSecondRingButton;

    private TextView modelProgressTextView;
    private TextView tfModelProgressTextView;

    private ConstraintLayout homeRootConstraintLayout;

    private ArView arView;

    public static JavaHomeFragment newInstance() {
        return new JavaHomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        setListeners();
    }

    private void setListeners() {
        tryOnFirstRingButton.setOnClickListener(view -> showArExperience(FIRST_MODEL_ID));
        tryOnSecondRingButton.setOnClickListener(view -> showArExperience(SECOND_MODEL_ID));
    }

    private void showArExperience(int ringId) {
        createArViewIfNotExists();
        arView.setModelId(ringId);
    }

    private void createArViewIfNotExists() {
        if (arView == null) {
            arView = ArView.withApiKey(
                    requireActivity(),
                    "spWyH9aA-OEL-Bl27KHAeQ",
                    "https://stage-api-ar.postindustria.com/v1",
                    "B802CE0DE1C57C53414CDEAE9253123475828F39"
            );
            addToRoot(arView);
            getLifecycle().addObserver(arView);
            listenArProgress(arView);
        }
    }

    private void addToRoot(ArView arView) {
        arView.setId(View.generateViewId());
        ConstraintSet constraintSet = new ConstraintSet();
        homeRootConstraintLayout.addView(arView);
        constraintSet.clone(homeRootConstraintLayout);
        constraintSet.connect(arView.getId(), ConstraintSet.TOP, homeRootConstraintLayout.getId(), ConstraintSet.TOP);
        constraintSet.connect(arView.getId(), ConstraintSet.BOTTOM, homeRootConstraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(arView.getId(), ConstraintSet.START, homeRootConstraintLayout.getId(), ConstraintSet.START);
        constraintSet.connect(arView.getId(), ConstraintSet.END, homeRootConstraintLayout.getId(), ConstraintSet.END);
        constraintSet.applyTo(homeRootConstraintLayout);
    }

    private void listenArProgress(ArView arView) {
        arView.setOnDownloadProgress(downloadProgress -> {
            final CurrentDownloads currentDownloads = downloadProgress.getCurrentDownloads();
            if (currentDownloads == null) return;
            List<DownloadOperation> models = currentDownloads.getModels();
            boolean isAnyModelDownloadInProgress = false;
            if (models != null) {
                isAnyModelDownloadInProgress = models
                        .stream()
                        .anyMatch((downloadOperation) -> !downloadOperation.isFinished());
            }

            List<DownloadOperation> tfModels = currentDownloads.getTfModels();
            boolean isAnyTfModelDownloadInProgress = false;
            if (tfModels != null) {
                isAnyTfModelDownloadInProgress = tfModels
                        .stream()
                        .anyMatch((downloadOperation) -> !downloadOperation.isFinished());
            }

            changeModelProgressVisibility(isAnyModelDownloadInProgress);
            changeTfModelProgressVisibility(isAnyTfModelDownloadInProgress);
        });
    }

    private void changeTfModelProgressVisibility(boolean isAnyTfModelDownloadInProgress) {
        if (isAnyTfModelDownloadInProgress) {
            requireActivity().runOnUiThread(() -> tfModelProgressTextView.setVisibility(View.VISIBLE));
        } else {
            requireActivity().runOnUiThread(() -> tfModelProgressTextView.setVisibility(View.GONE));
        }
    }

    private void changeModelProgressVisibility(boolean isAnyModelDownloadInProgress) {
        if (isAnyModelDownloadInProgress) {
            requireActivity().runOnUiThread(() -> modelProgressTextView.setVisibility(View.VISIBLE));
        } else {
            requireActivity().runOnUiThread(() -> modelProgressTextView.setVisibility(View.GONE));
        }
    }

    private void findViews() {
        tryOnFirstRingButton = requireView().findViewById(R.id.try_on_first_ring_button);
        tryOnSecondRingButton = requireView().findViewById(R.id.try_on_second_ring_button);
        modelProgressTextView = requireView().findViewById(R.id.model_progress_text_view);
        tfModelProgressTextView = requireView().findViewById(R.id.tf_model_progress_text_view);
        homeRootConstraintLayout = requireView().findViewById(R.id.home_root_constraint_layout);
    }
}