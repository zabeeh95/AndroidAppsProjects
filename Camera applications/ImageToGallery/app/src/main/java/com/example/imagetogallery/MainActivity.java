package com.example.imagetogallery;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int WRITE_EXTERNAL = 500;
    Button cameraBtn,galleryBtn,saveImage;
    ImageView selectedImage;
    String currentPhotoPath;
    String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = findViewById(R.id.cameraBtn);
        selectedImage = findViewById(R.id.imageView);
        galleryBtn = findViewById(R.id.galleryBtn);
        saveImage = findViewById(R.id.saveimage);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  saveImage();
                File f = new File(currentPhotoPath);
                String savedImageURL = null;

                try {
                    savedImageURL = MediaStore.Images.Media.insertImage(
                           getContentResolver(), String.valueOf(f), "Bird", "Image of bird");
                 } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Parse the gallery image url to uri
                Uri savedImageURI = Uri.parse(savedImageURL);
            }
        });

    }

    //saveImage using output stream
  /*  private void saveImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) selectedImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/Mypics");
        dir.mkdirs();

        String fileName = String.format("%d.jpg",System.currentTimeMillis());
        File outFile = new File(dir,currentPhotoPath);
        try {
            outputStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   */

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},CAMERA_PERM_CODE);
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL);
        }else {
           dispatchTakePictureIntent();
        }
    }

   //checking permission granted is of camera     //PERMISSION_GRANTED returns an integer
/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "give permission to open camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

 */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.imagetogallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

     /* File root = Environment.getExternalStorageDirectory();
        File image = new File(root.getAbsolutePath() + "/AppName/Media/Images" + timeStamp + ".jpg");
        image.mkdirs();
        String Dir = "/storage/emulated/0/" + timeStamp +".jpg";
        String Dir = Environment.getExternalStorageDirectory()+ "/my app/";
        File image = new File(Dir);
        image.mkdir();

        String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() +"/newFolder/"+ imageFileName + ".jpg";
        File image = new File(storageDir);
      */

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(   imageFileName,    ".jpg",            storageDir      );


        if (!image.isFile()) {
            Log.d("uploadFile", "Source Fffffffffffffffffffffffffffffffffffffffffffffffile not exist :"+ image.toString() );
        }else{
            Log.d("uploadFile","file exist");
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("photo path", "path of image " + currentPhotoPath.toString());
        return image;
    }
    
    //saving file with name of dir
  /*  public static File getFilePath(String fileName){
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "FOLDER_NAME");
        File filePath = new File(folder + "/" + fileName);
        return filePath;
    }

   */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("URL", "url of file is" + Uri.fromFile(f).toString());



           /*     Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImage.setImageBitmap(imageBitmap);

            */

                //gallery scan intent
               /* Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                */
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_" + getFileEXt(contentUri);
                selectedImage.setImageURI(contentUri);

                File g = new File(imageFileName);
                String saveGalleryImage =  null;

                try {
                    saveGalleryImage = MediaStore.Images.Media.insertImage(
                            getContentResolver(), String.valueOf(g), "Bird", "Image of bird");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    private String getFileEXt(Uri contentUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }
    

}
