package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.Random;

import nwmissouri.edu.missouriarboretum.R;

public class CameraPhoto extends Activity {

    //    private static int TAKE_PICTURE=1;
//    private Uri imageUri;
    private File imageFile;
    ImageButton takeaphotoIBTN;
//    ImageView cameraIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);
        takeaphotoIBTN = (ImageButton) findViewById(R.id.TakeaPhotoIBTN);

        takeaphotoIBTN.setOnClickListener(cameraListener);
        getActionBar().setIcon(R.drawable.navdrawerappicon);
    }

    public View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture" + (new Random()).nextInt() + ".jpg");
            //"test.jpg"
            Uri tempUri = Uri.fromFile(imageFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//            mediaScanIntent.setData(tempuri);
//            this.sendBroadcast(mediaScanIntent);
//            this.sendBroadcast(
//                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                            Uri.parse(tempUri.toString())));

            startActivityForResult(intent, 0);


        }
    };

//    public void takePhoto(View v)
//    {
//        Intent camareIntent=new Intent("android.media.action.IMAGE_CAPTURE");
//        //final String path= Environment.DIRECTORY_DCIM;
//        File photo= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM.concat("/Camera23")),"picture"+(new Random()).nextInt()+".jpg");
////         File photo= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"picture"+(new Random()).nextInt()+".jpg");
////        File photo= Environment.getExternalStoragePublicDirectory()+File.separator+("picture"+(new Random()).nextInt()+".jpg");
//
//        //File photo=new File(Environment.);
//        imageUri=Uri.fromFile(photo);
////         camareIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//        startActivityForResult(camareIntent,TAKE_PICTURE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        this.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(Uri.fromFile(imageFile).toString()))
        );
//        if(imageFile.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//
//            cameraIV = (ImageView) findViewById(R.id.cameraIV);
//
//            cameraIV.setImageBitmap(myBitmap);
//
//        }
        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (imageFile.exists()) {
                        Toast.makeText(this, "file saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                        ImageView cameraIV = (ImageView) findViewById(R.id.cameraIV);
//                        cameraIV.setImageURI(Uri.fromFile(imageFile));
//                        InputStream is = getClass().getResourceAsStream("/drawable/" + imageFile);
//                        cameraIV.setImageDrawable(Drawable.createFromStream(is, ""));
                        try {
                            cameraIV.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                        }catch (NullPointerException npe){
                            npe.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "file not saved ", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }
}


