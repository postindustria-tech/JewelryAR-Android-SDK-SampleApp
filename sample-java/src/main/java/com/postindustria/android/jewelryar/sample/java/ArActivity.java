package com.postindustria.android.jewelryar.sample.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.postindustria.android.jewelryar.unity.communication.events.entities.CurrentDownloads;
import com.postindustria.android.jewelryar.unity.communication.events.entities.DownloadOperation;
import com.postindustria.android.jewelryar.views.ArView;

import java.util.List;

public class ArActivity extends AppCompatActivity {

    static final String EXTRA_RING_ID = "EXTRA_RING_ID";

    private ArView arView;
    private TextView tfModelInProgressTextView;
    private TextView modelInProgressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        findViews();
        getLifecycle().addObserver(arView);
        setListeners();
        updateModelIdFromIntent();
    }

    private void updateModelIdFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        Bundle extras = intent.getExtras();
        if (extras == null) return;
        int ringId = extras.getInt(EXTRA_RING_ID);
        arView.setModelId(ringId);
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
            runOnUiThread(() -> tfModelInProgressTextView.setVisibility(View.VISIBLE));
        } else {
            runOnUiThread(() -> tfModelInProgressTextView.setVisibility(View.GONE));
        }
    }

    private void changeModelProgressVisibility(boolean isAnyModelDownloadInProgress) {
        if (isAnyModelDownloadInProgress) {
            runOnUiThread(() -> modelInProgressTextView.setVisibility(View.VISIBLE));
        } else {
            runOnUiThread(() -> modelInProgressTextView.setVisibility(View.GONE));
        }
    }

    private void findViews() {
        arView = findViewById(R.id.ar_view);
        modelInProgressTextView = findViewById(R.id.model_progress_text_view);
        tfModelInProgressTextView = findViewById(R.id.tf_model_progress_text_view);
    }
}