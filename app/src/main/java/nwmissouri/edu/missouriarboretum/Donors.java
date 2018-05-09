package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nwmissouri.edu.missouriarboretum.Donors.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nwmissouri.edu.missouriarboretum.Donors#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Donors extends Fragment {

    //    TextView find1TV, find2TV;
//    String [] donornames={"Dr. Robert Foster", "Dr. Kent Porterfield"};
    ListView donorslistLV;


    //    ArrayList<String> donorTrees;
//    ArrayList<String> dummy=dummy=new ArrayList<String>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DonorAdapter donoradapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Donors.
     */
    // TODO: Rename and change types and number of parameters
    public static Donors newInstance(String param1, String param2) {
        Donors fragment = new Donors();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Donors() {
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
        View view = inflater.inflate(R.layout.fragment_donors, container, false);
        donorslistLV = (ListView) view.findViewById(R.id.donorslistLV);

        DonorJsonParser donorparser = new DonorJsonParser(getActivity(), "http://csgrad10.nwmissouri.edu/arboretum/treedonortable.php");

        ArrayList<DonorBean> donorList = donorparser.getTreeList();

        //donorList.get(0).get

        donoradapter = new DonorAdapter(getActivity(), android.R.layout.simple_list_item_1, donorList);
        donoradapter.sort(new Comparator<DonorBean>() {
            @Override
            public int compare(DonorBean donorBean, DonorBean donorBean1) {
                return donorBean.getDonorLname().compareTo(donorBean1.getDonorLname());
            }
        });
          donoradapter.sort(new Comparator<DonorBean>() {
              @Override
              public int compare(DonorBean donorBean1, DonorBean donorBean2) {
                  return donorBean1.getCompanyname().compareTo(donorBean2.getCompanyname());
              }
          });
        donorslistLV.setTextFilterEnabled(true);
        donorslistLV.setAdapter(donoradapter);


        donoradapter.notifyDataSetChanged();

        donorslistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonorBean dbean = (DonorBean) parent.getAdapter().getItem(position);
                double latitude = dbean.getDonorLatitude();
                double longitude = dbean.getDonorLongitude();
                String trailid = dbean.getDonortrailID();
                String commonName=dbean.getDonorCname();
                String scientificName=dbean.getDonorSname();
                String description=dbean.getDonorDescription();
                String walkname=dbean.getDonorWalkname();
                String treeid=dbean.getDonorTreeId();
                //Toast.makeText(getActivity(), bean.getTreeId(), Toast.LENGTH_LONG).show();
//                Intent donorTreeIntent = new Intent(getActivity(), DonorTreesMap.class);
//                donorTreeIntent.putExtra("Latitude", latitude);
//                donorTreeIntent.putExtra("Longitude", longitude);
//                donorTreeIntent.putExtra("Trailid", trailid);
//                donorTreeIntent.putExtra("Common name",commonName);
//                donorTreeIntent.putExtra("Scientific name",scientificName);
//                donorTreeIntent.putExtra("Description",description);
//                donorTreeIntent.putExtra("Walkname",walkname);
//                donorTreeIntent.putExtra("Tree id",treeid);
//                startActivity(donorTreeIntent);
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

    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_commemerative, menu);

        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();

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

                final CharSequence s = searchView.getQuery();
                searchView.setQueryHint("Search");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if (newText.isEmpty()) {
                            donoradapter.getFilter().filter("");

                            donorslistLV.clearTextFilter();
                        } else
                            donoradapter.getFilter().filter(newText);

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
        // Set title
        getActivity().getActionBar()
                .setTitle("Donors");
    }

    class DonorAdapter extends ArrayAdapter<DonorBean> implements Filterable {
        ArrayList<DonorBean> localobject;
        ArrayList<DonorBean> localobject1;

        public DonorAdapter(Context context, int resource, ArrayList<DonorBean> objects) {
            super(context, resource, objects);
            localobject = objects;
            localobject1 = new ArrayList<DonorBean>(objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.donor_tree_row, null);

//            TextView dLnameTV = (TextView) convertView.findViewById(R.id.donor_lname_tv);
//            dLnameTV.setText(localobject.get(position).getDonorLname());
//            TextView dFnameTV = (TextView) convertView.findViewById(R.id.donor_fname_tv);
//            dFnameTV.setText(localobject.get(position).getDonorFname());

            try {
                TextView dLnameTV = (TextView) convertView.findViewById(R.id.donor_lname_tv);
                TextView dFnameTV = (TextView) convertView.findViewById(R.id.donor_fname_tv);
                String firstName = localobject.get(position).getDonorLname();
                String lastName=localobject.get(position).getDonorFname();
                String company=localobject.get(position).getCompanyname();
                if(company.length()>0) {
                    dLnameTV.setText(company);
                    dFnameTV.setText("");
                }else{
                    if(lastName.contains("Mr.& Mrs.") || firstName.contains("Mr.& Mrs.") || lastName.contains("Mr. and Mrs.") || firstName.contains("Mr. and Mrs.")) {
                        String titleFromLastName = lastName.substring(0, lastName.lastIndexOf(" ") + 1);
                        lastName = lastName.substring(lastName.lastIndexOf(" ") + 1);
                        String titleFromFirstName=firstName.substring(0, firstName.lastIndexOf(" ")+1);
                        firstName=firstName.substring(firstName.lastIndexOf(" ")+1);
                    }else if(lastName.contains("Mr.")||firstName.contains("Mr.")){
                        String titleFromLastName = lastName.substring(0, lastName.indexOf(" ") + 1);
                        lastName = lastName.substring(lastName.indexOf(" ") + 1);
                        String titleFromFirstName=firstName.substring(0, firstName.indexOf(" ")+1);
                        firstName=firstName.substring(firstName.indexOf(" ")+1);
                    }else {

                    }
//                  cLnameTV.setText(localobject.get(position).getCommLname());
                    dLnameTV.setText(lastName);
                    dFnameTV.setText(firstName);
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
                        List<DonorBean> founded = new ArrayList<DonorBean>();
                        for (DonorBean item : localobject1) {
                            if (item.getDonorFname().toString().toLowerCase().contains(constraint)|| item.getCompanyname().toString().toLowerCase().contains(constraint) || item.getDonorLname().toString().toLowerCase().contains(constraint)) {
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
                    for (DonorBean item : (List<DonorBean>) results.values) {
                        add(item);
                    }
                    notifyDataSetChanged();

                }

            };
        }
    }
}
