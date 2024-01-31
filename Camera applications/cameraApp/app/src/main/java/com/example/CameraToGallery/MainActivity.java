package com.example.CameraToGallery;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int PERMISSION_CODE = 1000;
    Button cameraBtn, galleryBtn, saveImage;
    ImageView selectedImage;
    String path;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = findViewById(R.id.cameraBtn);
        selectedImage = findViewById(R.id.imageView);
        galleryBtn = findViewById(R.id.galleryBtn);
        saveImage = findViewById(R.id.saveimage);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);

        cameraBtn.setOnClickListener(v -> dispatchTakePictureIntent());

        galleryBtn.setOnClickListener(v -> Toast.makeText(MainActivity.this, "not working", Toast.LENGTH_SHORT).show());

        saveImage.setOnClickListener(v -> Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show());

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private void dispatchTakePictureIntent()  {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            path = Environment.DIRECTORY_DCIM + "/zabeeh";

            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImage.setImageURI(Uri.parse(String.valueOf(imageUri)));
            Log.d("URL", "url of file is  " + Uri.parse(String.valueOf(imageUri)).toString());
            
        /*   // url of file is                file:///DCIM/zabeeh/20201211_110427
            //getExternalStorageState                  file:///mountedDCIM/zabeeh/20201211_111247
            //getRootdirectory  D/URL: url of file is  file:///systemDCIM/zabeeh/20201211_111510

             Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImage.setImageBitmap(imageBitmap);

            */
        }
    }


}
