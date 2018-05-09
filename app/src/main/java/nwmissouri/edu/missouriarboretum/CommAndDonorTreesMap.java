package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommAndDonorTreesMap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommAndDonorTreesMap#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CommAndDonorTreesMap extends Fragment implements GoogleMap.OnMarkerClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager lManager;
    boolean isConnected;
    HashMap<String, HashMap> extraMarkerInfo = new HashMap<String, HashMap>();
    ArrayList<Marker> mapDataList = new ArrayList<Marker>();
    double latitude;
    double longitude;
    String trailid;
    String commonName;
    String scientificName;
    String description;
    String trailName;
    String treeid;
    GestureDetector gestureDetector = null;
    ViewFlipper viewflipperVF;
    String trailId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommAndDonorTreesMap.
     */
    // TODO: Rename and change types and number of parameters
    public static CommAndDonorTreesMap newInstance(String param1, String param2) {
        CommAndDonorTreesMap fragment = new CommAndDonorTreesMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public CommAndDonorTreesMap() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.navdrawerappicon);
//        viewflipperVF = (ViewFlipper) getActivity().findViewById(R.id.flipper1);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comm_and_donor_trees_map, container, false);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapperific)).getMap();
//            mMap=smf.getMap();
//            SupportMapFragment smf=(SupportMapFragment)getFragmentManager().findFragmentById(R.id.map);
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
//            mMap=smf.getMap();
        mMap.setMyLocationEnabled(true);
        lManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0l,1.0f,new LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(17).bearing(90).tilt(01).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.350681, -94.882484), 10));

        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            setUpMap();
        }
        //}
        return view;
    }

    private void setUpMap() {
        Bundle args=getArguments();
        commonName = args.getString("Common name");
        scientificName = args.getString("Scientific name");
        description = args.getString("Description");
//        trailName = args.getString("Trail name");
//        treeid= args.getString("Tree id");
        latitude=args.getDouble("Latitude");
        longitude= args.getDouble("Longitude");

        HashMap<String, String> mapData = new HashMap<String, String>();
        mapData.put("Common name",commonName);
        mapData.put("Scientific name",scientificName);
        mapData.put("Description",description);
        mapData.put("Trail name",trailName);
        mapData.put("Tree id",treeid);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gaunttrailtreeicon))
                .position(new LatLng(latitude, longitude))
                .title(trailid)
                .visible(true));
        extraMarkerInfo.put(marker.getId(),mapData);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        HashMap<String, String> marker_data = extraMarkerInfo.get(marker.getId());
        String commonName = marker_data.get("Common name");
        String scientificName = marker_data.get("Scientific name");
        String description = marker_data.get("Description");
        String trailName = marker_data.get("Trail name");
        String treeId=marker_data.get("Tree id");
        String trailId=marker_data.get("Trail id");

//        Intent intent = new Intent(this, GauntTrailTrees.class);
//        intent.putExtra("Common name", commonName);
//        intent.putExtra("Scientific name", scientificName);
//        intent.putExtra("Description", description);
//        intent.putExtra("Trail name", trailName);
//        intent.putExtra("Tree id",treeId);
//        intent.putExtra("Trail id",trailId);
//        startActivity(intent);

        Bundle args=new Bundle();
        args.putString("Common name", commonName);
        args.putString("Scientific name",scientificName);
        args.putString("Description",description);
        args.putString("Trail name",trailName);
        args.putString("Tree id",treeId);
//        args.putString("Trail id",trailid);
        Fragment sendFragment=new CommAndDonorTreesInfo();
        sendFragment.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,sendFragment);
        ft.addToBackStack(null);
        //ft.add(R.id.content_frame,sendFragment);
        ft.commit();
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
            isConnected=InternetConnection.isConnected(activity);
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
    public void onSaveInstanceState(Bundle outState) {
        /**
         * Force Vm to stop building instance
         */
    }

    @Override
    public void onStop() {
        super.onStop();

        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapperific);
        if (f != null) {

            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
        if(Adoptatree.SELECTED_MODULE=="commemorative trees")
            getActivity().getActionBar().setTitle("Commemorative map");
        if (Adoptatree.SELECTED_MODULE=="donors")
            getActivity().getActionBar().setTitle("Donors map");
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
