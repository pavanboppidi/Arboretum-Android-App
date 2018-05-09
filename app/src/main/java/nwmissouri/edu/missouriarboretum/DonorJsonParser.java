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
 * Created by S518637 on 10/31/2014.
 */
public class DonorJsonParser {

    private String mUrl;
    private Context mContext;
    ArrayList<DonorBean> donorInfoArrayList;
    public static final String CACHE_DIR = Environment
            .getExternalStorageDirectory() + File.separator + "NWMSU";
    private final String cacheTreeInfoFile = CACHE_DIR + File.separator
            + "donorinfo.nw";
    private final String TAG = getClass().getSimpleName();

    public DonorJsonParser(Context context, String url) {

        this.mUrl = url;
        this.mContext = context;
        createCacheDir();
    }

    public ArrayList<DonorBean> getTreeList() {
        SendDonorInfoRequest request = new SendDonorInfoRequest();
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


    private ArrayList<DonorBean> parseJsonString(String JsonString) {
        ArrayList<DonorBean> donorInfoArrayList = new ArrayList<DonorBean>();
        try {
            JSONArray donorinfoArray = new JSONArray(JsonString);
            for (int i = 0; i < donorinfoArray.length(); i++) {

                DonorBean dbean = new DonorBean();

                JSONObject donorJsonObject = donorinfoArray.getJSONObject(i);
                if (donorJsonObject.has("treeid"))
                    dbean.setDonorTreeId(donorJsonObject.getString("treeid"));
                if (donorJsonObject.has("trailid"))
                    dbean.setDonortrailID(donorJsonObject.getString("trailid"));
                if (donorJsonObject.has("cname"))
                    dbean.setDonorCname(donorJsonObject.getString("cname"));
                if (donorJsonObject.has("sname"))
                    dbean.setDonorSname(donorJsonObject.getString("sname"));
                if (donorJsonObject.has("description"))
                    dbean.setDonorDescription(donorJsonObject.getString("description"));
                if (donorJsonObject.has("latitude"))
                    dbean.setDonorLatitude(donorJsonObject.getDouble("latitude"));
                if (donorJsonObject.has("longitude"))
                    dbean.setDonorLongitude(donorJsonObject.getDouble("longitude"));
                if (donorJsonObject.has("walkname"))
                    dbean.setDonorWalkname(donorJsonObject.getString("walkname"));

                if (donorJsonObject.has("donorid"))
                    dbean.setDonorID(donorJsonObject.getInt("donorid"));
                if (donorJsonObject.has("dfname"))
                    dbean.setDonorFname(donorJsonObject.getString("dfname"));
                if (donorJsonObject.has("dlname"))
                    dbean.setDonorLname(donorJsonObject.getString("dlname"));
                if (donorJsonObject.has("companyname"))
                    dbean.setCompanyname(donorJsonObject.getString("companyname"));

                donorInfoArrayList.add(dbean);


            }


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

        return donorInfoArrayList;


    }

    private void writeDataToCache(ArrayList<DonorBean> donorInfoList) {

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
                oos.writeObject(donorInfoList);

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

    private class SendDonorInfoRequest extends
            AsyncTask<String, Integer, ArrayList<DonorBean>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DonorJsonParser.this.mContext);
            progressDialog.setTitle("Downloading Donor tree information");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected ArrayList<DonorBean> doInBackground(String... params) {

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
        protected void onPostExecute(ArrayList<DonorBean> result) {

            super.onPostExecute(result);
            DonorJsonParser.this.donorInfoArrayList = result;


            progressDialog.cancel();

        }/**
         * starting image downloads


         System.out.println(result);*/


    }
}
