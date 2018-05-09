package nwmissouri.edu.missouriarboretum;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DonorTreesMap extends FragmentActivity implements GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager lManager;
    boolean isConnected;
    HashMap<String, HashMap> extraMarkerInfo = new HashMap<String, HashMap>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_trees_map);
        isConnected = InternetConnection.isConnected(getApplicationContext());
        getActionBar().setIcon(R.drawable.navdrawerappicon);

        viewflipperVF = (ViewFlipper) findViewById(R.id.flipper1);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
        if(Adoptatree.SELECTED_MODULE=="commemorative trees")
            this.getActionBar().setTitle("Commemorative map");
        if (Adoptatree.SELECTED_MODULE=="donors")
            this.getActionBar().setTitle("Donors map");
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {

//        Adoptatree.SELECTED_MODULE;
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0l,1.0f,new LocationListener(){
                @Override
                public void onLocationChanged(Location location) {
                    CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(17).bearing(90).tilt(01).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.350681, -94.882484), 17));
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        latitude = getIntent().getDoubleExtra("Latitude", 0.0);
        longitude = getIntent().getDoubleExtra("Longitude", 0.0);
        trailid = getIntent().getStringExtra("Trailid");
        commonName=getIntent().getStringExtra("Common name");
        scientificName=getIntent().getStringExtra("Scientific name");
        description=getIntent().getStringExtra("Description");
//        try{
        trailName=getIntent().getStringExtra("Walkname");
        treeid=getIntent().getStringExtra("Tree id");
//        final TreeBean bean = (TreeBean) getIntent().getExtras().get("tree");

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
        String treeid = marker_data.get("Tree id");

        Intent intent = new Intent(this, CommAndDonorTreesInfo.class);
        intent.putExtra("Common name", commonName);
        intent.putExtra("Scientific name", scientificName);
        intent.putExtra("Description", description);
//        intent.putExtra("Trail name", trailName);
        intent.putExtra("Tree id", treeid);
        startActivity(intent);

        return true;
    }
       /* setContentView(R.layout.activity_gaunt_trail_trees);
//        ImageView imageView = (ImageView) findViewById(R.id.treeimage_iv);
        //ImageView playSound=(ImageView)findViewById(R.id.imageView_play);
        TextView commonNameTV = (TextView) findViewById(R.id.cNameTV);
        TextView scientificNameTV = (TextView) findViewById(R.id.sNameTV);
        TextView descriptionTV = (TextView) findViewById(R.id.descTV);
        TextView treedescriptionTV = (TextView) findViewById(R.id.treeDescriptionTV);
        TextView trailNameTV = (TextView) findViewById(R.id.trailNameTV);

        gestureDetector = new GestureDetector(this, this);
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
            ImageView imageViewIV = new ImageView(DonorTreesMap.this);
            if (BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()) != null) {
                imageViewIV.setImageBitmap(BitmapFactory.decodeFile(imageFileResult.get(i).getAbsolutePath()));
                try {
                    viewflipperVF.addView(imageViewIV);
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }


        }

        if (Adoptatree.SELECTED_MODULE=="commemorative trees")
            getActionBar().setTitle("Commemorative trees");
        if (Adoptatree.SELECTED_MODULE=="donors")
            getActionBar().setTitle("Donor trees");
        commonNameTV.setText(commonName);
        scientificNameTV.setText(scientificName);
        treedescriptionTV.setText(description);
        trailNameTV.setText(trailName);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float sensitvity = 50;

        if ((e1.getX() - e2.getX()) > sensitvity) {
            viewflipperVF.showPrevious();
        } else if ((e2.getX() - e1.getX()) > sensitvity) {
            viewflipperVF.showNext();
        }

        return true;
    }*/

}
