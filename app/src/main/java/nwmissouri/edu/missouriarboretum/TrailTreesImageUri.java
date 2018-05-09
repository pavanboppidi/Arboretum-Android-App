package nwmissouri.edu.missouriarboretum;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by s518620 on 12/3/2014.
 */
public class TrailTreesImageUri {

    private String imageUrls;

    private String treeId;
    private Context mContext;



    private final String TAG = getClass().getSimpleName();


    public TrailTreesImageUri(Context context,String treeId) {
        this.treeId = treeId;
        this.mContext=context;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public void getImageObject() throws IOException {


        File cacheFile = new File(TreeImageJsonParser.filePath);


        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;


        try {

            fileInputStream = new FileInputStream(cacheFile);
            objectInputStream = new ObjectInputStream(fileInputStream);

            ArrayList<TreeImageBean> imageBeanArrayList = (ArrayList<TreeImageBean>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            processUrl(getTreeId(),imageBeanArrayList);
        } catch (IOException e) {
            Log.e(TAG, e.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    ProgressDialog progressDialog;
    private void processUrl(final String treeId,ArrayList<TreeImageBean> imageList) {

        final ArrayList<String> imageurlList=new ArrayList<String>();
        for(TreeImageBean imageObject:imageList){

            if(treeId.equalsIgnoreCase(imageObject.getTreeId())){
                imageurlList.add(imageObject.getImageUrl());
            }
        }

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(
                        mContext);

                Log.e("URI ", "ASYNC");
//                progressDialog.setTitle("Downloading tree images");
//                progressDialog.setCancelable(false);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //  progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                for(int ctr=0;ctr<imageurlList.size();ctr++){
                    synchronized (imageurlList.get(ctr)) {

                        downloadimages(treeId,
                                imageurlList.get(ctr), ctr);
                    }
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.cancel();;
            }
        }.execute();

    }


    private void downloadimages(String treeId, String imageUrl, int index) {
        URL url;
        HttpURLConnection connection;
        try {

            File imageDir=new File(TreeJsonParser.CACHE_DIR+File.separator+"Trails"+File.separator+"TreeImages"+File.separator);
            imageDir.mkdir();

            File imageFile = new File(imageDir.getAbsolutePath()+File.separator+ treeId + "_" + index + ".png");



            if (!imageFile.exists()) {
                imageFile.createNewFile();
                url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);


                InputStream input = connection.getInputStream();
                Bitmap treeImage = BitmapFactory.decodeStream(input);

                if (treeImage == null)
                    Log.d("IMAGE PARSER", treeId);
                processBitmap(imageFile, treeImage, 420, 420);
                //}
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void processBitmap(File imagefile, Bitmap treeImage, int newWidth, int newHeight) {


        if (treeImage != null && treeImage.getWidth() > 0 && treeImage.getHeight() > 0) {
            int width = treeImage.getWidth();
            int height = treeImage.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(treeImage, 0, 0, width, height,
                    matrix, false);

            writeImageToDisk(imagefile, resizedBitmap);
        }

    }

    private void writeImageToDisk(File imagefile, Bitmap resizedBitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imagefile);
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 00, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
}
