package com.meiro.meiroassignmentsiddharth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.meiro.meiroassignmentsiddharth.databinding.ActivityCapturedImageBinding;

public class capturedImageActivity extends AppCompatActivity {

    ActivityCapturedImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCapturedImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imageUriString = getIntent().getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        binding.imageView3.setImageURI(imageUri);

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(capturedImageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}