package nwmissouri.edu.missouriarboretum;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TreeImageJsonParser {

    private Context mContext;
    private String url;
    public static String cacheImagePathDir = TreeJsonParser.CACHE_DIR + File.separator + "treeImages" + File.separator;
    public static String filePath=cacheImagePathDir+File.separator+"imageObect.nwm";
    private final String TAG = getClass().getSimpleName();

    public TreeImageJsonParser(Context mContext, String url) {
        this.mContext = mContext;
        this.url = url;
        checkImageDir(cacheImagePathDir);
    }

    private void checkImageDir(String cacheImagePathDir2) {
        File dir = new File(cacheImagePathDir2);
        if (!dir.exists())
            dir.mkdir();

    }

    public void getImages() {
        new ImageRequest().execute(this.url);
    }

    private ArrayList<TreeImageBean> parseJsonString(String treeJson) {
        ArrayList<TreeImageBean> treeImageList = new ArrayList<TreeImageBean>();
        // ArrayList<TreeBean> treeinfo=new ArrayList<TreeBean>();
        try {
            JSONArray treeImageJson = new JSONArray(treeJson);
            // JSONArray treeinfoJson=new JSONArray();
            for (int i = 0; i < treeImageJson.length(); i++) {

                TreeImageBean imageBean = new TreeImageBean();
                JSONObject treeImageObject = treeImageJson.getJSONObject(i);

                if (treeImageObject.has("treeid"))
                    imageBean.setTreeId(treeImageObject.getString("treeid"));
                if (treeImageObject.has("imageid")) {
                    imageBean.setImageUrl(treeImageObject.getString("imageid"));
//					imageBean
//							.setImageUrl("https://www.gstatic.com/webp/gallery3/2_webp_ll.png");
                }
                Log.d("Image id:", String.valueOf(imageBean));

                if (treeImageObject.has("cname"))
                    imageBean.setcName(treeImageObject.getString("cname"));
                if (treeImageObject.has("sname"))
                    imageBean.setsName(treeImageObject.getString("sname"));
                if (treeImageObject.has("description"))
                    imageBean.setDescription(treeImageObject.getString("description"));

                treeImageList.add(imageBean);

            }


            writeDataToCache(treeImageList);
//            for (TreeImageBean treeImageBean : treeImageList) {
//                synchronized (treeImageBean) {
//                    downloadimages(treeImageBean.getTreeId(),
//                            treeImageBean.getImageUrl(), treeImageList.indexOf(treeImageBean));
//                }
//
//            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return treeImageList;
    }

    private void downloadimages(String treeId, String imageUrl, int index) {
        URL url;
        HttpURLConnection connection;
        try {


            File imageFile = new File(cacheImagePathDir + treeId + "_" + (index + 1) + ".png");

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


    private void writeDataToCache(ArrayList<TreeImageBean> treeImageList) {

        if (checkImageObejct()) {
            Log.i(TAG, "data already exists");
        } else {

            File cacheFile = new File(filePath);
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                cacheFile.createNewFile();

                fos = new FileOutputStream(cacheFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(treeImageList);

                oos.flush();
                fos.flush();
            } catch (IOException e) {
                Log.e(TAG, e.toString());

            } finally {

                try {
                    oos.close();
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }

    private boolean checkImageObejct(){

       return  new File(filePath).exists();
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

    private class ImageRequest extends AsyncTask<String, Integer, ArrayList<TreeImageBean>> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(
                    TreeImageJsonParser.this.mContext);
            progressDialog.setTitle("Downloading tree images");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
        }

        @Override
        protected ArrayList<TreeImageBean> doInBackground(String... params) {

            URL url;
            HttpURLConnection httUrlConnection;
            try {
                url = new URL(params[0]);
                httUrlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = httUrlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String data;
                while ((data = br.readLine()) != null) {

                    sb.append(data);
                }

                String treeImageJson = sb.toString();

                return parseJsonString(treeImageJson);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.toString());

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TreeImageBean> result) {

            super.onPostExecute(result);
            progressDialog.cancel();
            //changeActivity();
            System.out.println(result);
        }

    }

    private void changeActivity() {
        mContext.startActivity(new Intent(mContext, ViewInfo.class));

    }
}
