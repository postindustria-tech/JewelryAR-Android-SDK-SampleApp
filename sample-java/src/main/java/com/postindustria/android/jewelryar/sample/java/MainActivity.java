package com.postindustria.android.jewelryar.sample.java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int FIRST_MODEL_ID = 1;
    private static final int SECOND_MODEL_ID = 2;

    private Button tryOnFirstRingButton;
    private Button tryOnSecondRingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
    }

    private void findViews() {
        tryOnFirstRingButton = findViewById(R.id.try_on_first_ring_button);
        tryOnSecondRingButton = findViewById(R.id.try_on_second_ring_button);
    }

    private void setListeners() {
        tryOnFirstRingButton.setOnClickListener((view) -> goToArScreen(FIRST_MODEL_ID));
        tryOnSecondRingButton.setOnClickListener((view) -> goToArScreen(SECOND_MODEL_ID));
    }

    private void goToArScreen(int ringId) {
        Intent intent = new Intent(this, ArActivity.class);
        intent.putExtra(ArActivity.EXTRA_RING_ID, ringId);
        startActivity(intent);
    }
}