package com.meiro.meiroassignmentsiddharth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;

import java.io.IOException;
import java.io.OutputStream;

import android.hardware.Camera;
import android.content.ContentValues;
import android.provider.MediaStore;

import com.meiro.meiroassignmentsiddharth.databinding.ActivityScannerBinding;

public class scannerActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceView;
    private ImageView imageView;
    private Button captureButton;

    ActivityScannerBinding binding;

    Bitmap bitmap;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lottieAnimationView5.setVisibility(View.GONE);

        surfaceView = findViewById(R.id.surfaceView2);
        imageView = findViewById(R.id.imageView);
        captureButton = findViewById(R.id.captureButton);


        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            initializeCamera();
        }


// Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission is already granted, proceed with your code
        }

    }

    // Initialize camera and set up preview
    private void initializeCamera() {
        camera = android.hardware.Camera.open();
        binding.surfaceView2.getHolder().addCallback(this);
    }

    // Capture image and display in ImageView
    public void captureImage(View view) {


        if (camera != null) {
            camera.takePicture(null, null, (data, camera) -> {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image_" + System.currentTimeMillis() + ".jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                // Insert the image into MediaStore and get the content URI
                ContentResolver resolver = getContentResolver();
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try {
                    // Open an OutputStream to the content URI and write the image data
                    OutputStream outputStream = resolver.openOutputStream(imageUri);
                    if (outputStream != null) {
                        outputStream.write(data);
                        outputStream.close();

                        // Display the saved image in the ImageView
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        imageView.setImageBitmap(bitmap);

                        binding.view3.setBackgroundColor(getResources().getColor(android.R.color.black));


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.lottieAnimationView5.setVisibility(View.VISIBLE);

                binding.lottieAnimationView5.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Intent intent = new Intent(scannerActivity.this, capturedImageActivity.class);
                        intent.putExtra("imageUri", imageUri.toString()); // Pass the content URI as a string
                        startActivity(intent);
                        finish();
                    }
                }, 2000);


                // Release the camera
                camera.release();

            });
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // No need to handle
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
    }
}









