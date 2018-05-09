package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommAndDonorTreesInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommAndDonorTreesInfo#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CommAndDonorTreesInfo extends Fragment implements GestureDetector.OnGestureListener{
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommAndDonorTreesInfo1.
     */
    // TODO: Rename and change types and number of parameters
    public static CommAndDonorTreesInfo newInstance(String param1, String param2) {
        CommAndDonorTreesInfo fragment = new CommAndDonorTreesInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public CommAndDonorTreesInfo() {
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
        if (Adoptatree.SELECTED_MODULE=="commemorative trees")
            getActivity().getActionBar().setTitle("Commemorative trees");
        if (Adoptatree.SELECTED_MODULE=="donors")
            getActivity().getActionBar().setTitle("Donor trees");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_comm_and_donor_trees_info1, container, false);
        TextView commonNameTV = (TextView)view.findViewById(R.id.cNameTV);
        TextView scientificNameTV = (TextView) view.findViewById(R.id.sNameTV);
        TextView descriptionTV = (TextView) view.findViewById(R.id.descTV);
        TextView treedescriptionTV = (TextView) view.findViewById(R.id.treeDescriptionTV);
//        TextView trailNameTV = (TextView)view.findViewById(R.id.trailNameTV);
//        TextView trailIdTV=(TextView)view.findViewById(R.id.trailIdTV);
        viewflipperVF = (ViewFlipper)view.findViewById(R.id.flipper1);
//        String commonName = fa.getIntent().getStringExtra("Common name");
//        String scientificName = fa.getIntent().getStringExtra("Scientific name");
//        String description = fa.getIntent().getStringExtra("Description");
//        String trailName = fa.getIntent().getStringExtra("Trail name");
//        String treeid=fa.getIntent().getStringExtra("Tree id");
//        String trailid=fa.getIntent().getStringExtra("Trail id");

        Bundle args=getArguments();
        String commonName = args.getString("Common name");
        String scientificName = args.getString("Scientific name");
        String description = args.getString("Description");
        String trailName = args.getString("Trail name");
        String treeid= args.getString("Tree id");
//        String trailid= args.getString("Trail id");
        try {
            commonNameTV.setText(commonName);
            scientificNameTV.setText(scientificName);
            treedescriptionTV.setText(description);
//            trailNameTV.setText(trailName);
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
//        trailIdTV.setText(trailid);

//        final TreeBean bean = (TreeBean) getIntent().getExtras().get("Tree id");
        gestureDetector = new GestureDetector(getActivity(),CommAndDonorTreesInfo.this);
        File imagedir = new File(TreeImageJsonParser.cacheImagePathDir);

        ArrayList<File> imageFile = new ArrayList<File>();
        File[] arr = imagedir.listFiles();
        Log.d("RESULT ACTIVITY ", "----------" + arr.length);

        ArrayList<File> imageFileResult = new ArrayList<File>();
        for (File f : arr) {
            String name = f.getName();
            if (name.indexOf("_") != -1)
                name = name.substring(0, name.indexOf("_"));
            if (name.equalsIgnoreCase(treeid)) {
                imageFileResult.add(f);
            }

        }
        for (int i = 0; i < imageFileResult.size(); i++) {
            ImageView imageViewIV = new ImageView(getActivity());
            if (BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()) != null) {
                imageViewIV.setImageBitmap(BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()));
                try {
                    viewflipperVF.addView(imageViewIV);
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
