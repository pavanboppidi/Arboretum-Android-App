package nwmissouri.edu.missouriarboretum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nwmissouri.edu.staticclasses.ToastMessage;

public class TreeJsonParser {

    private String mUrl;
    private Context mContext;
    ArrayList<TreeBean> treeInfoArrayList;
    public static String CACHE_DIR = Environment.getExternalStorageDirectory() + File.separator + "NWMSU";
    public static final String cacheTreeInfoFile = CACHE_DIR + File.separator
            + "treeinfo.nw";
    private final String TAG = getClass().getSimpleName();
    public TreeJsonParser(Context context, String url) {

        this.mUrl = url;
        this.mContext = context;
        createCacheDir();
    }

    public ArrayList<TreeBean> getTreeList() {
        SendTreeInfoRequest request = new SendTreeInfoRequest();
        request.execute(mUrl);
        try {
            return request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createCacheDir() {
        new File(CACHE_DIR).mkdir();
    }

    @SuppressLint("NewApi")
    private ArrayList<TreeBean> parseJsonString(String JsonString) {
        ArrayList<TreeBean> treeInfoArrayList = new ArrayList<TreeBean>();
        try {
            JSONArray treeinfoArray = new JSONArray(JsonString);
            for (int i = 0; i < treeinfoArray.length(); i++) {
                TreeBean bean = new TreeBean();
                JSONObject treeJsonObject = treeinfoArray.getJSONObject(i);

                if (treeJsonObject.getString("treeid").equalsIgnoreCase("2014-999")) {
                    Log.d("--- ", "----------");
                }
                /**
                 * Retrieving all the json tree data and storing its data in a TreeBean object.
                 */
                if (treeJsonObject.has("treeid"))
                    bean.setTreeId(treeJsonObject.getString("treeid"));
                if (treeJsonObject.has("trailid"))
                    bean.setTrailId(treeJsonObject.getString("trailid"));
                if (treeJsonObject.has("cname"))
                    bean.setcName(treeJsonObject.getString("cname"));
                if (treeJsonObject.has("sname"))
                    bean.setsName(treeJsonObject.getString("sname"));
                if (treeJsonObject.has("description"))
                    bean.setDescription(treeJsonObject.getString("description"));
                if (treeJsonObject.has("latitude"))
                    bean.setLatitude(treeJsonObject.getDouble("latitude"));
                if (treeJsonObject.has("longitude"))
                    bean.setLongitude(treeJsonObject.getDouble("longitude"));
                if (treeJsonObject.has("status"))
                    bean.setStatus(treeJsonObject.getString("status"));
                if (treeJsonObject.has("year"))
                    bean.setYear(treeJsonObject.getString("year"));
                if (treeJsonObject.has("note"))
                    bean.setNote(treeJsonObject.getString("note"));
                try {
                    if (treeJsonObject.has("audio")) {
                        bean.setAudioUrl(treeJsonObject.getString("audio"));
                    }
                } catch (Exception e) {

                    System.out.println(e);
                }
                if (treeJsonObject.has("walkname"))
                    bean.setWalkName(treeJsonObject.getString("walkname"));
                treeInfoArrayList.add(bean);
            }
            //============Commented by Pavan======================//


//            for (int i = 0; i < treeInfoArrayList.size(); i++) {
//                synchronized (treeInfoArrayList) {
//                    downloadAudioFiles(treeInfoArrayList.get(i).getTreeId(),
//                            treeInfoArrayList.get(i).getAudioUrl());
//                }
//            }
        } catch (JSONException e) {
            ToastMessage.message("Something went wrong", (Activity) mContext);
        }

        return treeInfoArrayList;
    }

    //============Commented by Pavan======================//


//    private void downloadAudioFiles(String fileIndex, String urlString) {
//        HttpURLConnection httUrlConnection;
//        try {
//            File audioFile = new File(CACHE_DIR + File.separator + fileIndex
//                    + ".amr");
//
//            if (!audioFile.exists()) {
//                URL url = new URL(urlString);
//                httUrlConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream is = httUrlConnection.getInputStream();
//
//                writeToDisk(audioFile, is);
//            }
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//        }
//    }



    private void writeToDisk(File audioFile, InputStream is) throws IOException {
        try {

            audioFile.createNewFile();
            OutputStream os = new FileOutputStream(audioFile);
            byte data[] = new byte[1024];

            int count = 0;

            while ((count = is.read(data)) != -1) {
                os.write(data, 0, count);
            }

            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        }

    }

    //============Commented by Pavan======================//


    private void writeDataToCache(ArrayList<TreeBean> treeInfoList) {

        if (checkCacheTreeInfo()) {
            Log.i(TAG, "data already exists");
        } else {

            File cacheFile = new File(cacheTreeInfoFile);
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                cacheFile.createNewFile();

                fos = new FileOutputStream(cacheFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(treeInfoList);

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

    //============Commented by Pavan======================//


    private boolean checkCacheTreeInfo() {
        return new File(cacheTreeInfoFile).exists();

    }

    private class SendTreeInfoRequest extends
            AsyncTask<String, Integer, ArrayList<TreeBean>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TreeJsonParser.this.mContext);
            progressDialog.setTitle("Downloading tree information");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected ArrayList<TreeBean> doInBackground(String... params) {

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
                String treeJson = sb.toString();
                return parseJsonString(treeJson);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.toString());

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TreeBean> result) {

            super.onPostExecute(result);
            //============Commented by Pavan======================//
            writeDataToCache(result);
            TreeJsonParser.this.treeInfoArrayList = result;
            progressDialog.cancel();

        }
        /**
         * starting image downloads
         System.out.println(result);*/
    }
}
