package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nwmissouri.edu.missouriarboretum.TreeInformation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nwmissouri.edu.missouriarboretum.TreeInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeInformation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final String TAG_WALKNAME = "walkname";
    static final String TAG_CNAME = "cname";
    static final String TAG_TRAILID = "trailid";
    public static ListView listview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ArrayList<HashMap<String, String>> Treelist;
    private OnFragmentInteractionListener mListener;

    TreeAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TreeInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static TreeInformation newInstance(String param1, String param2) {
        TreeInformation fragment = new TreeInformation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TreeInformation() {
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
        View view = inflater.inflate(R.layout.fragment_treeinformationsample, container, false);
        listview = (ListView) view.findViewById(R.id.List);
        TreeJsonParser parser = new TreeJsonParser(getActivity(), "http://csgrad10.nwmissouri.edu/arboretum/treetable.php");
        ArrayList<TreeBean> treeList = parser.getTreeList();
        TreeImageJsonParser imageParser = new TreeImageJsonParser(getActivity(), "http://csgrad10.nwmissouri.edu/arboretum/images.php");
        imageParser.getImages();

        TreeAdapter adapter = new TreeAdapter(getActivity(), android.R.layout.simple_list_item_1, treeList);
        try {
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TreeBean bean = (TreeBean) parent.getAdapter().getItem(position);
                Bundle args = new Bundle();
                args.putSerializable("tree", bean);
                Fragment sendFragment = new ResultAct();
                sendFragment.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, sendFragment);
                ft.addToBackStack(null);
                ft.commit();
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
        getActivity().getActionBar()
                .setTitle("Tree information");
    }

    /**
     * This custom Array Adapter is used to display tree information in a table in a list view.
     */
    class TreeAdapter extends ArrayAdapter<TreeBean> {
        ArrayList<TreeBean> localObj;

        public TreeAdapter(Context context, int resource, ArrayList<TreeBean> objects) {
            super(context, resource, objects);
            localObj = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
            TextView name = (TextView) convertView.findViewById(R.id.TreecommonNameTV);
            TextView Tname = (TextView) convertView.findViewById(R.id.TreetrailnameTV);
            TextView tid = (TextView) convertView.findViewById(R.id.TreetrailIdTV);
            name.setText(localObj.get(position).getcName());
            String walkname = localObj.get(position).getWalkName();
            walkname = walkname.substring(0, walkname.indexOf(" "));
            Tname.setText(walkname);
            tid.setText(localObj.get(position).getTrailId());
            return convertView;
        }
    }
}
