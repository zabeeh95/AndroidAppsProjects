package com.example.filterapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap;
    Button camera, saveImage;
    Uri imageUri;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        camera = findViewById(R.id.camera);
        saveImage = findViewById(R.id.saveImage);

        permissions();

        saveImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void permissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        saveImage.setVisibility(View.INVISIBLE);
        createFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, 2);
    }

    //only creating file to use for camera to deliver intent to save picture returning image uri
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createFile() {
        //  OutputStream fos = null;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        path = Environment.DIRECTORY_DCIM + "/zabeeh";

        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

    }

    public void choosePhoto(View v) {                        //view v is used for onclick button
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);        //open android builtin gallery by using Intent constructor
        intent.setType("image/*");                  //any sort of image either jpeg or bmp or else
        startActivityForResult(intent, 1);       //request code specify from which intent we r coming back from
    }

    //creating empty png file then using fos writing that bitmap visible to that file
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void saveFile() {
        OutputStream fos;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        path = Environment.DIRECTORY_DCIM + "/zabeeh";

        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try {
            fos = resolver.openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {          //activity.ok special constant to check nothing went wrong and data exists
            Uri uri = data.getData();            //destination path of file which is being opened
            Glide.with(this).load(uri).into(imageView);
            // imageView.setImageBitmap(bitmap);

            ParcelFileDescriptor parcelFileDescriptor;   //resolver is class allowing file operation
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);             //factory word means is a class whose job is to instatntiate the object
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == 2 && resultCode == RESULT_OK) {

            //  imageView.setImageURI(Uri.parse(String.valueOf(imageUri)));

            ParcelFileDescriptor parcelFileDescriptor;   //resolver is class allowing file operation
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(imageUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);             //factory word means is a class whose job is to instatntiate the object
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Glide.with(this).load(imageUri).into(imageView);


        }

    }

  /*          Glide
                .with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(new SepiaFilterTransformation()))
                .into(imageView);
    */

    public void apply(Transformation<Bitmap> filter) {
        Glide
                .with(this)
                .load(bitmap)                                 //bitmap required for filter transform
                .apply(RequestOptions.bitmapTransform(filter))
                .into(imageView);

    }

    public void applySketch(View v) {
        apply(new SketchFilterTransformation());

    }

    public void applySepia(View v) {
        apply(new SepiaFilterTransformation());

        Log.d("new bitmap", "before darwable exists ");

    }

    public void applyToon(View v) {
        apply(new ToonFilterTransformation());
    }

    public void applyInvertFilterTransformation(View v) {
        apply(new InvertFilterTransformation());
    }

    public void applyGrayscaleTransformation(View v) {
        apply(new GrayscaleTransformation());
    }



    public void bitmapSave(View view) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap newBitmap = bitmapDrawable.getBitmap();

        Log.d("new bitmap", "imageview exists ");
        //savemage(newBitmap,"zabeehbitmap");
        secondSaveImage(newBitmap);
    }

    private void secondSaveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, this.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    this.getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}