package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Photo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Photo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Photo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private File imageFile;
    ImageButton takeaphotoIBTN;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Photo.
     */
    // TODO: Rename and change types and number of parameters
    public static Photo newInstance(String param1, String param2) {
        Photo fragment = new Photo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Photo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.navdrawerappicon);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        takeaphotoIBTN = (ImageButton)getActivity().findViewById(R.id.TakeaPhotoIBTN);
        getActivity().getActionBar().setIcon(R.drawable.navdrawerappicon);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture" + (new Random()).nextInt() + ".jpg");
        Uri tempUri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        getActivity().sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(Uri.fromFile(imageFile).toString()))
        );
        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (imageFile.exists()) {
                        Toast.makeText(getActivity(), "file saved", Toast.LENGTH_SHORT).show();
                        ImageView cameraIV = (ImageView)getActivity().findViewById(R.id.cameraIV);
                        try {
                            cameraIV.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                        }catch (NullPointerException npe){
                            npe.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "file not saved ", Toast.LENGTH_SHORT).show();
                    }
            }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        // Set title
        getActivity().getActionBar()
                .setTitle("Photo");
//        if (takeaphotoIBTN.hasOnClickListeners()) {
//        takeaphotoIBTN.setOnClickListener(cameraListener);
//        }
    }
}
