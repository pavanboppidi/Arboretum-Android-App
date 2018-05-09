package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nwmissouri.edu.staticclasses.ToastMessage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultAct.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultAct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultAct extends Fragment implements GestureDetector.OnGestureListener, MediaPlayer.OnPreparedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    MediaPlayer mediaPlayer;
    float initialXPoint;
    GestureDetector gestureDetector = null;
    ViewFlipper viewflipperVF;
    boolean isPlaying = false;
    private int timeWhenPaused = 0;
    private int currentTime = 0;
    private int duration = 0;
    private SeekBar mediaSeekBarPlayer;
    private TextView mMediaTime;
    private boolean running = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultAct1.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultAct newInstance(String param1, String param2) {
        ResultAct fragment = new ResultAct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultAct() {
        // Required empty public constructor
    }


    Bundle bundleObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.navdrawerappicon);
        getActivity().getActionBar().setTitle("Tree information");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bundleObject = getArguments();
    }


    private String treeId;
    TextView commonNameTV;
    TextView scientificNameTV;
    TextView descriptionTV;
    TextView treedescriptionTV;
    TextView walknameTV;
    TextView trailIDTV;
    TreeBean bean;
    ImageView playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_tree_information, container, false);
        viewflipperVF = (ViewFlipper) view.findViewById(R.id.flipper1);
        commonNameTV = (TextView) view.findViewById(R.id.cNameTV);
        scientificNameTV = (TextView) view.findViewById(R.id.sNameTV);
        descriptionTV = (TextView) view.findViewById(R.id.descTV);
        treedescriptionTV = (TextView) view.findViewById(R.id.treedescriptionTV);
        walknameTV = (TextView) view.findViewById(R.id.walknameTV);
        trailIDTV = (TextView) view.findViewById(R.id.treetrailID_TV);
        mMediaTime = (TextView) view.findViewById(R.id.mediaTime);
        mediaSeekBarPlayer = (SeekBar) view.findViewById(R.id.progress_bar);
        /**
         * Handling Play button
         */
        playButton = (ImageView) view.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAudioExistence(bean.audioUrl);
            }
        });
        mediaSeekBarPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /**
                 * If media player is null, initiate the player then update time accordingly.
                 */
                if (mediaPlayer == null) {
                    checkAudioExistence(bean.audioUrl);
                } else if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    updateTime();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bean = (TreeBean) bundleObject.getSerializable("tree");
        treeId = bean.getTreeId();
        // downloadImages(bean.getTreeId());
        //imageView.setImageBitmap(BitmapFactory.decodeFile(TreeImageJsonParser.cacheImagePathDir + bean.getTreeId() + ".png"));
        try {
            getImageObject();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }

//    private void downloadImages(String treeId) {
//        DynamicImageUrlMap urlMap=new DynamicImageUrlMap(getActivity(),treeId);
//
//        try {
//            urlMap.getImageObject();
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
        /**
         * Stop audio when we navigate back.
         */
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mListener = null;
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        return gestureDetector.onTouchEvent(event);
//    }
//
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
            // Log.e(TAG, e.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    ProgressDialog progressDialog;

    private void processUrl(final String treeId, ArrayList<TreeImageBean> imageList) {

        final ArrayList<String> imageurlList = new ArrayList<String>();
        for (TreeImageBean imageObject : imageList) {

            if (treeId.equalsIgnoreCase(imageObject.getTreeId())) {
                imageurlList.add(imageObject.getImageUrl());
            }
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(
                        getActivity());
                progressDialog.setTitle("Downloading tree images");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                for (String url : imageurlList) {
                    synchronized (url) {

                        downloadimages(treeId,
                                url, new Random().nextInt(50));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    commonNameTV.setText(bean.getcName());
                    scientificNameTV.setText(bean.getsName());
                    treedescriptionTV.setText(bean.getDescription());
                    walknameTV.setText(bean.getWalkName());
                    trailIDTV.setText(bean.getTrailId());
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

                gestureDetector = new GestureDetector(getActivity(), ResultAct.this);
                File imagedir = new File(TreeImageJsonParser.cacheImagePathDir);

                ArrayList<File> imageFile = new ArrayList<File>();
                File[] arr = imagedir.listFiles();
                Log.d("RESULT ACTIVITY ", "----------" + arr.length);

                ArrayList<File> imageFileResult = new ArrayList<File>();
                for (File f : arr) {
                    String name = f.getName();
                    if (name.indexOf("_") != -1)
                        name = name.substring(0, name.indexOf("_"));
                    if (name.equalsIgnoreCase(bean.getTreeId())) {
                        imageFileResult.add(f);
                    }
                }

                for (int i = 0; i < imageFileResult.size(); i++) {
                    ImageView imageViewIV = new ImageView(getActivity());
                    if (BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()) != null) {
                        imageViewIV.setImageBitmap(BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()));
                        viewflipperVF.addView(imageViewIV);
                    }
                }


                progressDialog.cancel();
                ;


            }
        }.execute();

    }


    private void downloadimages(String treeId, String imageUrl, int index) {
        URL url;
        HttpURLConnection connection;
        try {


            File imageFile = new File(TreeImageJsonParser.cacheImagePathDir + treeId + "_" + index + ".png");

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

    /**
     * Check if url contains audio, else hide the player.
     *
     * @param url -- audio url
     */
    public void checkAudioExistence(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(url, new HashMap<String, String>());
            playOrPause(url);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "No Audio exists", Toast.LENGTH_LONG).show();
            mMediaTime.setText("");
            mediaSeekBarPlayer.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
        }
    }

    /**
     * This method toggle's between play and pause.
     *
     * @param url -- Url of the audio.
     */
    public void playOrPause(String url) {

        /**
         * If audio is not playing or paused.
         */
        if (isPlaying == false) {
            if (mediaPlayer == null) {
                //If media player is not yet started.
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(this);
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                    mMediaTime.setText("Buffering....");
                    mediaSeekBarPlayer.postDelayed(onEverySecond, 1000);
                    mediaPlayer.seekTo(timeWhenPaused);

                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "No Connection ", Toast.LENGTH_LONG).show();
                }
            } else {
                //If media player is in paused state.
                mediaPlayer.start();
                mediaSeekBarPlayer.setProgress(timeWhenPaused);
                mMediaTime.setText("Buffering....");
                mediaSeekBarPlayer.postDelayed(onEverySecond, 1000);
                updateTime();
            }
            playButton.setImageResource(R.drawable.pause);
            isPlaying = true;
        }
        /**
         * If audio is playing...
         */
        else {
            playButton.setImageResource(R.drawable.play);
            isPlaying = false;
            timeWhenPaused = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }


    /**
     * This method calls when ever audio is completely loaded or buffered.
     *
     * @param mp MediaPlayer object.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mediaPlayer == mp) {
            mediaPlayer.start();
        }
        duration = mediaPlayer.getDuration();
        mediaSeekBarPlayer.setMax(duration);
        mediaSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }


    /**
     * This runnable interface lets to synchronize between progressbar and time.
     */
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (true == running) {
                if (mediaSeekBarPlayer != null) {
                    mediaSeekBarPlayer.setProgress(mediaPlayer.getCurrentPosition());
                }
                if (mediaPlayer.isPlaying()) {
                    mediaSeekBarPlayer.postDelayed(onEverySecond, 1000);
                    updateTime();
                }
            }
        }
    };

    /**
     * This method is used to update the time w.r.t progress bar.
     */
    private void updateTime() {

        do {
            /**
             * Converting millis seconds to seconds and minutes.
             */
            currentTime = mediaPlayer.getCurrentPosition();
            int dSeconds = (int) (duration / 1000) % 60;
            int dMinutes = (int) ((duration / (1000 * 60)) % 60);
            int dHours = (int) ((duration / (1000 * 60 * 60)) % 24);
            int cSeconds = (int) (currentTime / 1000) % 60;
            int cMinutes = (int) ((currentTime / (1000 * 60)) % 60);
            int cHours = (int) ((currentTime / (1000 * 60 * 60)) % 24);
            /**
             * Diplaying timer for audio.
             */
            if (dHours == 0) {
                mMediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes, dSeconds));
            } else {
                mMediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
            }

            try {
                if (mediaSeekBarPlayer.getProgress() >= 100) {
                    break;
                }
            } catch (Exception e) {

            }
        } while (mediaSeekBarPlayer.getProgress() <= 100);
    }


}
