package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Adoptatree.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Adoptatree#newInstance} factory method to
 * create an instance of this fragment.
 */


//New comment
public class Adoptatree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //Initial Test
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static String SELECTED_MODULE="";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Adoptatree.
     */
    // TODO: Rename and change types and number of parameters
    public static Adoptatree newInstance(String param1, String param2) {
        Adoptatree fragment = new Adoptatree();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Adoptatree() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adoptatree, container, false);

        ImageButton commemorativeTreesIBTN = (ImageButton) view.findViewById(R.id.commemorativeTreesIBTN);
        commemorativeTreesIBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SELECTED_MODULE="commemorative trees";
                Fragment fragment = new CommemorativeTrees();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        ImageButton donorsIBTN = (ImageButton) view.findViewById(R.id.donorsIBTN);
        donorsIBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SELECTED_MODULE="donors";
                Fragment fragment = new Donors();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        ImageButton supportTheArboretumIBTN = (ImageButton) view.findViewById(R.id.supportTheArboretumIBTN);
        supportTheArboretumIBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new SupportTheArboretum();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
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
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
//new edit test
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar()
                .setTitle("Adopt a tree");
    }
}
