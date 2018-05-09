package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import nwmissouri.edu.staticclasses.ToastMessage;

public class ShrubWalk extends Fragment implements GoogleMap.OnMarkerClickListener, BearingToNorthProvider.ChangeEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private BearingToNorthProvider mBearingProvider;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager lManager;
    boolean isConnected;
    String commonName;
    int imgcount=1;
    String scientificName;
    String description;
    String trailName;
    String treeid;
    String trailId;
    String Audio;
    private int count = 0;
    ArrayList<Marker> mapDataList = new ArrayList<Marker>();
    HashMap<String, HashMap> extraMarkerInfo = new HashMap<String, HashMap>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gaunt.
     */
    // TODO: Rename and change types and number of parameters
    public static ShrubWalk newInstance(String param1, String param2) {
        ShrubWalk fragment = new ShrubWalk();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ShrubWalk() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle("Shrub walk trees");
        if(InternetConnection.isConnected(getActivity())) {
            mBearingProvider = new BearingToNorthProvider(getActivity());
            mBearingProvider.setChangeEventListener(this);
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(InternetConnection.isConnected(getActivity())) {
            mBearingProvider.stop();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gaunt,null);
        if(InternetConnection.isConnected(getActivity())) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapperific)).getMap();
            mMap.setMyLocationEnabled(true);
            lManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 1.0f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(17).bearing(90).tilt(01).build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
            if (mMap != null) {
                setUpMap();
            }
        }else {
            ToastMessage.message("No Internet Connection",getActivity());
            /**
             * If there is no internet connection, set back page.
             */
            getFragmentManager().popBackStack();
        }

        final ImageButton img = (ImageButton)view.findViewById(R.id.navigbutton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imgcount == 0) {

                    disableBearing();
                    imgcount = 1;
                    img.setImageResource(R.drawable.large);


                } else {
                    disableBearing();
                    imgcount = 0;
                    img.setImageResource(R.drawable.unselected_navigation);
                }

            }
        });


        return view;
    }

    private void setUpMap() {
        try {
            URL url = new URL("http://csgrad10.nwmissouri.edu/arboretum/shrubWalkTrees.php");
//            URL url = new URL("http://192.168.43.30/shrubWalkTrees.php");
            new DownloadJSONTask().execute(url);
            mMap.setOnMarkerClickListener(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void disableBearing(){

        count++;
        if (count % 2 != 0) {
            mBearingProvider.stop();
            mMap.setMyLocationEnabled(false);
        } else {
            mBearingProvider.start();
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onBearingChanged(final double bearing) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraPosition cp = new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(17)
                        .bearing((float) bearing)
                        .tilt(25)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
            }

        });
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
                       Log.d("checking",line);
                    publishProgress(++numLinesRead);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonToParse = jsonStrBuilder.toString();

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
            if (isConnected) {
                try {
                    Log.d("INTERNET", "Internet is connected");
                    JSONArray gauntTrailTrees = new JSONArray(jsonToParse);
                    JSONObject gauntTrailTreesContent = null;
                    for (int i = 0; i < gauntTrailTrees.length(); i++) {
                        gauntTrailTreesContent = gauntTrailTrees.getJSONObject(i);
                        double latitude = gauntTrailTreesContent.getDouble("latitude");
                        double longitude = gauntTrailTreesContent.getDouble("longitude");
                        trailId = gauntTrailTreesContent.getString("trailid");
                        commonName = gauntTrailTreesContent.getString("cname");
                        scientificName = gauntTrailTreesContent.getString("sname");
                        description = gauntTrailTreesContent.getString("description");
                        treeid=gauntTrailTreesContent.getString("treeid");
                        trailName = gauntTrailTreesContent.getString("walkname");
                        HashMap<String, String> mapData = new HashMap<String, String>();
                        mapData.put("Common name", commonName);
                        mapData.put("Scientific name", scientificName);
                        mapData.put("Description", description);
                        mapData.put("Trail name", trailName);
                        mapData.put("Tree id",treeid);
                        mapData.put("Trail id",trailId);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.shrub_walk_trail_icon))
                                .position(new LatLng(latitude, longitude))
                                .title(trailId)
                                .visible(true));
                        extraMarkerInfo.put(marker.getId(), mapData);
                        mapDataList.add(marker);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mapDataList) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(40.350681, -94.882484)).zoom(17).bearing(90).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //mMap.moveCamera(cu);
        }
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
        Bundle args=new Bundle();
        args.putString("Common name", commonName);
        args.putString("Scientific name",scientificName);
        args.putString("Description",description);
        args.putString("Trail name",trailName);
        args.putString("Tree id",treeId);
        Fragment sendFragment=new TrailTreesInformation();
        sendFragment.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,sendFragment);
        ft.addToBackStack(null);
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
            isConnected=InternetConnection.isConnected(activity);
        } catch (ClassCastException e) {
            ToastMessage.message("No Internet Connection",getActivity());
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapperific);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
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
     * <p>
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
        //Set title for action bar when application resumes.
        getActivity().getActionBar().setTitle("Shrub walk");
        //Starts maps ratation with respect to sensor.
        if(InternetConnection.isConnected(getActivity())) {
            mBearingProvider.start();
        }
    }
}
