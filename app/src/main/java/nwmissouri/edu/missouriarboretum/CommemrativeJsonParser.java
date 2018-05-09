package nwmissouri.edu.missouriarboretum;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by S518620 on 10/30/2014.
 */
public class CommemrativeJsonParser {
    private String mUrl;
    private Context mContext;
    ArrayList<CommemrativeBean> commInfoArrayList;
    public static final String CACHE_DIR = Environment
            .getExternalStorageDirectory() + File.separator + "NWMSU";
    private final String cacheTreeInfoFile = CACHE_DIR + File.separator
            + "comminfo.nw";
    private final String TAG = getClass().getSimpleName();

    public CommemrativeJsonParser(Context context, String url) {

        this.mUrl = url;
        this.mContext = context;
        createCacheDir();
    }

    public ArrayList<CommemrativeBean> getTreeList() {
        SendCommInfoRequest request = new SendCommInfoRequest();
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

    private ArrayList<CommemrativeBean> parseJsonString(String JsonString) {
        ArrayList<CommemrativeBean> commInfoArrayList = new ArrayList<CommemrativeBean>();
        try {
            JSONArray comminfoArray = new JSONArray(JsonString);
            for (int i = 0; i < comminfoArray.length(); i++) {
                CommemrativeBean cbean = new CommemrativeBean();


                //    "treeid":"2000-001",
//            "trailid":"",
//            "cname":"Sugar Maple",
//            "sname":"Black Maple",
//            "description":"This ia Sugar Maple",
//            "latitude":"40.35282302",
//            "longitude":"-94.88287628",
//            "walkname":"Donor Walk",
//            "comid":"3904",
//            "type":"Donated By",
//            "cfname":"carolyn",
//            "clname":"Mr.Koffman",
//            "company":""


                JSONObject commemrativeJsonObject = comminfoArray.getJSONObject(i);
                if (commemrativeJsonObject.has("treeid"))
                    cbean.setCommtreeId(commemrativeJsonObject.getString("treeid"));
                if (commemrativeJsonObject.has("trailid"))
                    cbean.setCommemrativetrailId(commemrativeJsonObject.getString("trailid"));
                if (commemrativeJsonObject.has("cname"))
                    cbean.setCommemrativeCname(commemrativeJsonObject.getString("cname"));
                if (commemrativeJsonObject.has("sname"))
                    cbean.setCommemrativeSname(commemrativeJsonObject.getString("sname"));
                if (commemrativeJsonObject.has("description"))
                    cbean.setCommemrativeDescription(commemrativeJsonObject.getString("description"));
                if (commemrativeJsonObject.has("latitude"))
                    cbean.setCommemrativeLatitude(commemrativeJsonObject.getDouble("latitude"));
                if (commemrativeJsonObject.has("longitude"))
                    cbean.setCommemrativelongitude(commemrativeJsonObject.getDouble("longitude"));
                if (commemrativeJsonObject.has("walkname"))
                    cbean.setCommemrativeWalkname(commemrativeJsonObject.getString("walkname"));

                if (commemrativeJsonObject.has("comid"))
                    cbean.setCommId(commemrativeJsonObject.getInt("comid"));
                if (commemrativeJsonObject.has("type"))
                    cbean.setType(commemrativeJsonObject.getString("type"));
                if (commemrativeJsonObject.has("cfname"))
                    cbean.setCommFname(commemrativeJsonObject.getString("cfname"));
                if (commemrativeJsonObject.has("clname"))
                    cbean.setCommLname(commemrativeJsonObject.getString("clname"));
                if (commemrativeJsonObject.has("company"))
                    cbean.setCommCompany(commemrativeJsonObject.getString("company"));
//                if (commemrativeJsonObject.has("treeid"))
//                    cbean.setCommtreeId(commemrativeJsonObject.getString("treeid"));
                if (commemrativeJsonObject.has("donorid"))
                    cbean.setDonorID(commemrativeJsonObject.getInt("donorid"));
                commInfoArrayList.add(cbean);


            }


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

        return commInfoArrayList;


    }

    private void writeDataToCache(ArrayList<CommemrativeBean> commInfoList) {

        if (checkCacheCommInfo()) {
            Log.i(TAG, "data already exists");
        } else {

            File cacheFile = new File(cacheTreeInfoFile);
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                cacheFile.createNewFile();

                fos = new FileOutputStream(cacheFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(commInfoList);

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

    private boolean checkCacheCommInfo() {
        return new File(cacheTreeInfoFile).exists();

    }

    private class SendCommInfoRequest extends
            AsyncTask<String, Integer, ArrayList<CommemrativeBean>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommemrativeJsonParser.this.mContext);
            progressDialog.setTitle("Downloading Commemrative tree information");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected ArrayList<CommemrativeBean> doInBackground(String... params) {

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
        protected void onPostExecute(ArrayList<CommemrativeBean> result) {

            super.onPostExecute(result);

            CommemrativeJsonParser.this.commInfoArrayList = result;
            progressDialog.cancel();

        }/**
         * starting image downloads


         System.out.println(result);*/


    }


}


