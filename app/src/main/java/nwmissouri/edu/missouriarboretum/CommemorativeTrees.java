package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nwmissouri.edu.missouriarboretum.CommemorativeTrees.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nwmissouri.edu.missouriarboretum.CommemorativeTrees#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommemorativeTrees extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //    String[] dTrees={"In honor of Dr. Robert Foster", "In memory of Jennifer Shaw Suhr","In memory of Cassalou Stanton"};
//    ArrayList<String> donorTrees;
    ListView commemorativeTreesLV;
    CommemrativeAdapter commadapter;
    //    ArrayList<String> dummy=dummy=new ArrayList<String>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommemorativeTrees.
     */
    // TODO: Rename and change types and number of parameters
    public static CommemorativeTrees newInstance(String param1, String param2) {
        CommemorativeTrees fragment = new CommemorativeTrees();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CommemorativeTrees() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commemorative_trees, container, false);

        commemorativeTreesLV = (ListView) view.findViewById(R.id.commemorativeTreesLV);
        CommemrativeJsonParser commparser = new CommemrativeJsonParser(getActivity(), "http://csgrad10.nwmissouri.edu/arboretum/commemorativetable.php");

        ArrayList<CommemrativeBean> commList = commparser.getTreeList();
        commadapter = new CommemrativeAdapter(getActivity(), android.R.layout.simple_list_item_1, commList);
        try {
            commadapter.sort(new Comparator<CommemrativeBean>() {
                @Override
                public int compare(CommemrativeBean commemrativeBean, CommemrativeBean commemrativeBean1) {
                    return commemrativeBean.getCommLname().compareTo(commemrativeBean.getCommLname());
                }
            });
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        try {
            commadapter.sort(new Comparator<CommemrativeBean>() {
                @Override
                public int compare(CommemrativeBean lhs, CommemrativeBean rhs) {
                    return lhs.getCommCompany().compareTo(rhs.getCommCompany());
                }
            });
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        commemorativeTreesLV.setAdapter(commadapter);
        commadapter.notifyDataSetChanged();
        commemorativeTreesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                CommemrativeBean cbean = (CommemrativeBean) parent.getAdapter().getItem(position);
//                Toast.makeText(getActivity(), cbean.getCommId(), Toast.LENGTH_LONG).show();
                double latitude = cbean.getCommemrativeLatitude();
                double longitude = cbean.getCommemrativelongitude();
                String trailid = cbean.getCommemrativetrailId();
                String commonName = cbean.getCommemrativeCname();
                String scientificName = cbean.getCommemrativeSname();
                String description = cbean.getCommemrativeDescription();
                String walkname = cbean.getCommemrativeWalkname();
                String treeid = cbean.getCommemrativeTreeId();

                Bundle args=new Bundle();
                args.putString("Common name", commonName);
                args.putString("Scientific name", scientificName);
                args.putString("Description", description);
                args.putDouble("Latitude", latitude);
                args.putDouble("Longitude", longitude);
//        args.putString("Trail id",trailid);
                Fragment sendFragment=new CommAndDonorTreesMap();
                sendFragment.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,sendFragment);
                ft.addToBackStack(null);
                //ft.add(R.id.content_frame,sendFragment);
                ft.commit();
            }
        });

//        commemorativeTreesLV= (ListView) view.findViewById(R.id.commemorativeTreesLV);
//        donorTrees=new ArrayList<String>();
//        for(String tree:dTrees)
//            donorTrees.add(tree);
////        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, donorTrees){
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, donorTrees){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
//                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
//
//                text1.setText(donorTrees.get(position));
//                //text2.setText(donorTrees.get(position));
//                return view;
//            }
//        };
//        commemorativeTreesLV.setAdapter(adapter);
//        commemorativeTreesLV.setDivider(null);
//        commemorativeTreesLV.setDividerHeight(0);
//        this.setHasOptionsMenu(true);
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

    SearchView commsearchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_commemerative, menu);

        commsearchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();


//        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
//        final CharSequence s=searchView.getQuery();
//        searchView.setQueryHint("Search");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(!TextUtils.isEmpty(newText)) {
//                   // Call filter here
//                   return true;
//                }
//                return false;
//            }
//
//            @Override
//           public boolean onQueryTextSubmit(String query) {
//                // Do something
//                String name= null;
//                String fname=null;
//                String lname=null;
//
////                if(s!=null){
////
////                    for(String st : dTrees){
////                        name=st.substring(st.lastIndexOf("of "));
////                        fname=name.substring(0,name.indexOf(" "));
////                        lname=name.substring(name.indexOf(" ")+1);
////                        if(fname.contains(s)){
////                            dummy.add(st);
////                        }
////                        else  if(lname.contains(s)){
////                            dummy.add(st);
////                        }
////                    }
////                }
//                return true;
//            }
//        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_bar:

                final CharSequence s = commsearchView.getQuery();
                commsearchView.setQueryHint("Search");
                commsearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if (newText.isEmpty()) {
                            commadapter.getFilter().filter("");

                            commemorativeTreesLV.clearTextFilter();
                        } else
                            commadapter.getFilter().filter(newText);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Do something
                        String name = null;
                        String fname = null;
                        String lname = null;


                        return true;
                    }
                });


                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar()
                .setTitle("Commemorative trees");
    }


    // Class CommemrativeAdapter extends ArrayAdapter<CommemrativeBean>
    class CommemrativeAdapter extends ArrayAdapter<CommemrativeBean> {
        ArrayList<CommemrativeBean> localobject;
        ArrayList<CommemrativeBean> localobject1;

        public CommemrativeAdapter(Context context, int resource, ArrayList<CommemrativeBean> objects) {
            super(context, resource, objects);
            localobject = objects;
            try {
                localobject1 = new ArrayList<CommemrativeBean>(objects);
            }
            catch(NullPointerException e)
            {
              e.printStackTrace();
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.commemrative_list_row, null);
            try {
                TextView ctypeTV = (TextView) convertView.findViewById(R.id.commemorative_type_TV);
                ctypeTV.setText(localobject.get(position).getType());
                TextView cLnameTV = (TextView) convertView.findViewById(R.id.commemorative_lname_TV);
                TextView cFnameTV = (TextView) convertView.findViewById(R.id.commemrative_fname_TV);
                String firstName = localobject.get(position).getCommLname();
                String lastName=localobject.get(position).getCommFname();
                String company=localobject.get(position).getCommCompany();
                if(company.length()>0) {
                    cLnameTV.setText(company);
                    cFnameTV.setText("");
                }else{
                    if(lastName.contains("Mr.& Mrs.") || firstName.contains("Mr.& Mrs.")) {
                        String titleFromLastName = lastName.substring(0, lastName.lastIndexOf(".") + 1);
                        lastName = lastName.substring(lastName.lastIndexOf(". ") + 1).trim();
                        String titleFromFirstName=firstName.substring(0, firstName.lastIndexOf(".")+1);
                        firstName=firstName.substring(firstName.lastIndexOf(". ")+1);
                    }else if(lastName.contains("Mr.") || firstName.contains("Mr.") || lastName.contains("Mrs.")||
                            firstName.contains("Mrs.") || lastName.contains("Miss.")|| firstName.contains("Miss.")){
                        String titleFromLastName = lastName.substring(0, lastName.indexOf(" ") + 1);
                        lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();
                        String titleFromFirstName=firstName.substring(0, firstName.indexOf(" ")+1);
                        firstName=firstName.substring(firstName.indexOf(" ")+1);
                    }
//                  cLnameTV.setText(localobject.get(position).getCommLname());
                    cLnameTV.setText(lastName);
                    cFnameTV.setText(firstName);
                }
            } catch (Exception e) {
                System.out.println("No data found " + e);
            }
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    constraint = constraint.toString().toLowerCase();
                    FilterResults result = new FilterResults();

                    if (constraint != null && constraint.toString().length() > 0) {
                        List<CommemrativeBean> founded = new ArrayList<CommemrativeBean>();
                        for (CommemrativeBean item : localobject1) {
                            if (item.getCommFname().toString().toLowerCase().contains(constraint)|| item.getCommCompany().toString().toLowerCase().contains(constraint) || item.getCommLname().toString().toLowerCase().contains(constraint)) {
                                founded.add(item);
                            }
                        }

                        result.values = founded;
                        result.count = founded.size();
                    } else {
                        result.values = localobject1;
                        result.count = localobject1.size();
                    }
                    return result;


                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    clear();
                    for (CommemrativeBean item : (List<CommemrativeBean>) results.values) {
                        add(item);
                    }
                    notifyDataSetChanged();

                }

            };
        }


    }

}


