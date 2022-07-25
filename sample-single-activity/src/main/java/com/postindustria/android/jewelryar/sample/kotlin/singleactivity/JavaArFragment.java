package com.postindustria.android.jewelryar.sample.kotlin.singleactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.postindustria.android.jewelryar.unity.communication.events.entities.CurrentDownloads;
import com.postindustria.android.jewelryar.unity.communication.events.entities.DownloadOperation;
import com.postindustria.android.jewelryar.views.ArView;

import java.util.List;

public class JavaArFragment extends Fragment {

    private static final String EXTRA_RING_ID = "EXTRA_RING_ID";

    public static JavaArFragment newInstance(int ringId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_RING_ID, ringId);
        JavaArFragment fragment = new JavaArFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView modelProgressTextView;
    private TextView tfModelProgressTextView;

    private ArView arView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        getLifecycle().addObserver(arView);
        setListeners();
        updateModelId();
    }

    private void updateModelId() {
        arView.setModelId(requireArguments().getInt(EXTRA_RING_ID));
    }

    private void setListeners() {
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
        modelProgressTextView = requireView().findViewById(R.id.model_progress_text_view);
        tfModelProgressTextView = requireView().findViewById(R.id.tf_model_progress_text_view);
        arView = requireView().findViewById(R.id.ar_view);
    }
}
