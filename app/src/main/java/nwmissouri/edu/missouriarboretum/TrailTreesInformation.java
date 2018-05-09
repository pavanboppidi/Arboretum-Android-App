package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrailTreesInformation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrailTreesInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrailTreesInformation extends Fragment implements GestureDetector.OnGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    GestureDetector gestureDetector = null;
    ViewFlipper viewflipperVF;
    private FragmentActivity fa;


    private String treeId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrailTreesInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static TrailTreesInformation newInstance(String param1, String param2) {
        TrailTreesInformation fragment = new TrailTreesInformation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TrailTreesInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().getActionBar().setIcon(R.drawable.navdrawerappicon);
//        viewflipperVF = (ViewFlipper) getActivity().findViewById(R.id.flipper1);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        fa= (FragmentActivity) super.getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trail_trees_information, container, false);
        TextView commonNameTV = (TextView) view.findViewById(R.id.cNameTV);
        TextView scientificNameTV = (TextView) view.findViewById(R.id.sNameTV);
        TextView descriptionTV = (TextView) view.findViewById(R.id.descTV);
        TextView treedescriptionTV = (TextView) view.findViewById(R.id.treeDescriptionTV);
        TextView trailNameTV = (TextView) view.findViewById(R.id.trailNameTV);
        TextView trailIdTV = (TextView) view.findViewById(R.id.trailIdTV);
        viewflipperVF = (ViewFlipper) view.findViewById(R.id.flipper1);
//        String commonName = fa.getIntent().getStringExtra("Common name");
//        String scientificName = fa.getIntent().getStringExtra("Scientific name");
//        String description = fa.getIntent().getStringExtra("Description");
//        String trailName = fa.getIntent().getStringExtra("Trail name");
//        String treeid=fa.getIntent().getStringExtra("Tree id");
//        String trailid=fa.getIntent().getStringExtra("Trail id");

        Bundle args = getArguments();
        String commonName = args.getString("Common name");
        String scientificName = args.getString("Scientific name");
        String description = args.getString("Description");
        String trailName = args.getString("Trail name");


//        String trailid= args.getString("Trail id");
        try {
            commonNameTV.setText(commonName);
            scientificNameTV.setText(scientificName);
            treedescriptionTV.setText(description);
            trailNameTV.setText(trailName);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
//        trailIdTV.setText(trailid);

//        final TreeBean bean = (TreeBean) getIntent().getExtras().get("Tree id");
        gestureDetector = new GestureDetector(getActivity(), TrailTreesInformation.this);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        treeId = getArguments().getString("Tree id").toString();
        try {
            getImageObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TrailTreesImageUri uri=new TrailTreesImageUri(getActivity().getApplicationContext(),getArguments().get("Tree id").toString());
        try {
            uri.getImageObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v2) {
        float sensitvity = 50;

        if ((e1.getX() - e2.getX()) > sensitvity) {
            viewflipperVF.showPrevious();
        } else if ((e2.getX() - e1.getX()) > sensitvity) {
            viewflipperVF.showNext();
        }

        return true;
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
//            mListener = (OnFragmentInteractionListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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


            processUrl(treeId, imageBeanArrayList);
        } catch (IOException e) {
            Log.e("EXECPTION", e.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    ProgressDialog progressDialog;

    private void processUrl(final String treeId, final ArrayList<TreeImageBean> imageList) {

        final ArrayList<String> imageurlList = new ArrayList<String>();
        for (TreeImageBean imageObject : imageList) {

            if (treeId.equalsIgnoreCase(imageObject.getTreeId())) {
                imageurlList.add(imageObject.getImageUrl());
            }
        }


        new AsyncTask<Void, Void, Void>() {


            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                for (int ctr = 0; ctr < imageurlList.size(); ctr++) {
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

                File imagedir = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator + "TreeImages" + File.separator);

                ArrayList<File> imageFile = new ArrayList<File>();
                File[] arr = imagedir.listFiles();
//                Log.d("RESULT ACTIVITY ", "----------" + arr.length);

                ArrayList<File> imageFileResult = new ArrayList<File>();
                for (File f : arr) {
                    String name = f.getName();
                    if (name.indexOf("_") != -1)
                        name = name.substring(0, name.indexOf("_"));
                    if (name.equalsIgnoreCase(treeId)) {
                        imageFileResult.add(f);
                    }

                }
                for (int i = 0; i < imageFileResult.size(); i++) {
                    ImageView imageViewIV = new ImageView(getActivity());
                    if (BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()) != null) {
                        imageViewIV.setImageBitmap(BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()));

                        for (int ctr = 0; ctr < imageurlList.size(); ctr++) {
                            synchronized (imageurlList.get(ctr)) {

                                Picasso.with(getActivity()).load(imageList.get(ctr).getImageUrl()).into(imageViewIV);
                            }
                        }



                        try {
                            viewflipperVF.addView(imageViewIV);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                }
                progressDialog.dismiss();
            }
        }.execute();

    }


    private void downloadimages(String treeId, String imageUrl, int index) {
        URL url;
        HttpURLConnection connection;
        try {

            File imageDir = new File(TreeJsonParser.CACHE_DIR + File.separator + "Trails" + File.separator + "TreeImages" + File.separator);
            imageDir.mkdir();

            File imageFile = new File(imageDir.getAbsolutePath() + File.separator + treeId + "_" + index + ".png");


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


}
