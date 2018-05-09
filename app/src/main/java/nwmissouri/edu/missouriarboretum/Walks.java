package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import nwmissouri.edu.staticclasses.DirectoryEditings;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Walks.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Walks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Walks extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private ProgressDialog progressDialog;
    private ArrayList<TreeData> trArrayList;
    private GridView view;
    JSONArray trails;
    boolean isConnected;
    String walkname;
    String imageId;
    String markerImage;
    public static String SELECTED_TRAIL = "";
    public static String cacheImagePathDir = TreeJsonParser.CACHE_DIR + File.separator + "images" + File.separator;
    public static String filePath = cacheImagePathDir + File.separator + "img.nwm";
    private final String TAG = getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Walks1.
     */
    // TODO: Rename and change types and number of parameters
    public static Walks newInstance(String param1, String param2) {
        Walks fragment = new Walks();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Walks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trArrayList = new ArrayList<TreeData>();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (isConnected) {
            File trailsDir = null;
            if ((trailsDir = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails")).exists()) {
                DirectoryEditings.deleteDirectory(trailsDir);
            }

            if (!new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator).exists())
                downloadJSON();
            else {
                File data = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator + "Object.vm");
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data));
                    Object obj = ois.readObject();
                    if (obj instanceof ArrayList<?>)
                        trArrayList = ((ArrayList<TreeData>) obj);
                    ois.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (StreamCorruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } else {
            Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_SHORT).show();
            //Setting the fragment to back. Programmatically controlling back button.
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View originalView = inflater.inflate(R.layout.fragment_walks1, container, false);
        view = (GridView) originalView.findViewById(R.id.gridView1);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), android.R.layout.simple_gallery_item, trArrayList);
        view.setAdapter(imageAdapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                if (trArrayList.get(position).getName().equalsIgnoreCase("Shrub walk")) {
                    Fragment frag = new ShrubWalk();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, frag)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Fragment frag = new GenericWalkFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("walkName", trArrayList.get(position).getName());
                    bundle.putString("markerUrl", trArrayList.get(position).getMarkerImageUrl());
                    frag.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, frag)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return originalView;
    }

    public void downloadJSON() {
        try {
            URL url = new URL("http://csgrad10.nwmissouri.edu/arboretum/trails.php");
//            URL url = new URL("http://192.168.43.30/trails.php");
            new DownloadJSONTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class DownloadJSONTask extends AsyncTask<URL, Integer, Long> {
        String jsonToParse;

        @Override
        protected Long doInBackground(URL... urls) {

            StringBuilder jsonStrBuilder = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                String line;
                int numLinesRead = 0;
                while ((line = br.readLine()) != null) {
                    jsonStrBuilder.append(line);
                    publishProgress(++numLinesRead);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonToParse = jsonStrBuilder.toString();
            try {
                trails = new JSONArray(jsonToParse);
                JSONObject trailsContent = null;
                walkname = null;
                imageId = null;
                for (int i = 0; i < trails.length(); i++) {
                    trailsContent = (JSONObject) trails.get(i);
                    walkname = trailsContent.getString("walkname");
                    imageId = trailsContent.getString("image");
                    markerImage = trailsContent.getString("flower");
                    trArrayList.add(new TreeData(imageId, walkname, markerImage));
                }

                for (TreeData url : trArrayList) {
                    downloadimages(url.getName(), url.getImageUrl());
                }

                File cacheFile = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator + "Object.vm");
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    cacheFile.createNewFile();
                    fos = new FileOutputStream(cacheFile);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(trArrayList);
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
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

            progressDialog = ProgressDialog.show(getActivity(), "Retrieving trails", "Please wait ...");

        }

        protected void onPostExecute(Long result) {

            ArrayList<TreeData> tempList = new ArrayList<TreeData>();
            ArrayList<String> walkName = new ArrayList<String>();

            for (Iterator<TreeData> ite = trArrayList.iterator(); ite.hasNext(); ) {
                TreeData treeData = ite.next();
                walkName.add(treeData.getName());
            }

            tempList.add(0, trArrayList.get(walkName.indexOf("Shrub walk")));
            trArrayList.remove(trArrayList.get(walkName.indexOf("Shrub walk")));
            tempList.addAll(trArrayList);
            trArrayList = tempList;
            getActivity().getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            ImageAdapter imageAdapter = new ImageAdapter(getActivity(), android.R.layout.simple_gallery_item, trArrayList);
            view.setAdapter(imageAdapter);
            progressDialog.dismiss();
        }
    }

    private void downloadimages(String treeId, String imageUrl) {
        URL url;
        HttpURLConnection connection;
        try {
            File imageDir = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            File imageFile = new File(imageDir.getAbsolutePath() + File.separator + treeId + "_trails" + ".png");
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

    private boolean checkImageObejct() {

        return new File(filePath).exists();
    }

    class ImageAdapter extends ArrayAdapter<TreeData> {
        ArrayList<TreeData> arrayList = new ArrayList<TreeData>();

        public ImageAdapter(Context context, int resource,
                            ArrayList<TreeData> objects) {
            super(context, resource, objects);
            arrayList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView tv = (TextView) convertView.findViewById(R.id.textView1);
            String imgPath = TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator
                    + arrayList.get(position).getName() + "_trails" + ".png";
            if (BitmapFactory.decodeFile(imgPath) != null) {
                iv.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }

            if (trArrayList.get(position).getName().equalsIgnoreCase("Chatauqua trail")) {
                String chatauqua = arrayList.get(position).getName();
                String trail = chatauqua.substring(chatauqua.indexOf("trail"));
                chatauqua = chatauqua.substring(0, chatauqua.indexOf(" "));
                tv.setText(chatauqua + "\n\t\t" + trail);
            } else {
                tv.setText("  " + arrayList.get(position).getName());
            }
            return convertView;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            isConnected = InternetConnection.isConnected(activity);
        } catch (ClassCastException e) {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        /**
         * Force Vm to stop building instance
         */
    }

    @Override
    public void onStop() {
        super.onStop();
        Walks f = (Walks) getFragmentManager().findFragmentById(R.id.linearView);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View gridView = (GridView) getActivity().findViewById(R.id.gridView1);
        View linearView = getActivity().findViewById(R.id.linearView);
        ViewGroup parent = (ViewGroup) gridView.getParent();
        parent.removeView(gridView);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set Title
        getActivity().getActionBar().setTitle("Trails");
    }
}
