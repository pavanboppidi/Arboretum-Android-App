package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import nwmissouri.edu.staticclasses.ToastMessage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    boolean isConnected;
    String name;
    String url;
    JSONArray urls;
    JSONObject urlContent;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

           final View view = inflater.inflate(R.layout.fragment_home, container, false);

            ImageView arboretumLogoIV = (ImageView) view.findViewById(R.id.arboretumLogoIV);
            arboretumLogoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadJSON(view);
                }
            });

            ImageButton trailsIBTN = (ImageButton) view.findViewById(R.id.trailsIBTN);
            trailsIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new Walks();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            ImageButton treeInformationIBTN = (ImageButton) view.findViewById(R.id.treeInformationIBTN);
            treeInformationIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new TreeInformation();
                    MyActivity.TAG = "TREEINFO";
                    // Check if there is internet connection else display message.
                    if (InternetConnection.isConnected(getActivity())) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                                .addToBackStack(null)
                                .commit();
                    }else {
                        ToastMessage.message("No Internet Connection",getActivity());
                    }
                }
            });

            ImageButton adoptATreeIBTN = (ImageButton) view.findViewById(R.id.adoptatreeIBTN);
            adoptATreeIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new Adoptatree();
                    MyActivity.TAG = "ADOPT";
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            ImageButton treeviaIBTN = (ImageButton) view.findViewById(R.id.treeviaIBTN);
            treeviaIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new Treevia();
                    MyActivity.TAG = "TREEVIA";
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            ImageButton informationIBTN = (ImageButton) view.findViewById(R.id.informationIBTN);
            informationIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new Information();
                    MyActivity.TAG = "INFORMATION";
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            ImageButton photoIBTN = (ImageButton) view.findViewById(R.id.photoIBTN);
            photoIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment fragment = new Photo();
                    MyActivity.TAG = "PHOTO";
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            ImageButton facebookIBTN = (ImageButton) view.findViewById(R.id.facebookIBTN);
            facebookIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyActivity.TAG = "FACEBOOK";
                    if(InternetConnection.isConnected(getActivity())) {
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/Missouri-State-Arboretum-Northwest-Missouri-State-University/179633572063209"));
                        startActivity(facebookIntent);
                    }else {
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ImageButton northwestWebsiteIBTN = (ImageButton) view.findViewById(R.id.northwestWebsiteIBTN);
            northwestWebsiteIBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyActivity.TAG = "SITE";
                    if(InternetConnection.isConnected(getActivity())) {
                        Intent northwestWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nwmissouri.edu/"));
                        startActivity(northwestWebsiteIntent);
                    }else {
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;

    }

    public void downloadJSON(View v) {
        try {
            URL url = new URL("http://csgrad10.nwmissouri.edu/arboretum/link.php");
            new DownloadJSONTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
//            System.out.printf("We have read %d lines\n", progress[0]);
        }

        protected void onPostExecute(Long result) {
            if (InternetConnection.isConnected(getActivity())) {
                try {
                    urls = new JSONArray(jsonToParse);
                    urlContent = null;
                    for (int i = 0; i < urls.length(); i++) {
                        urlContent = urls.getJSONObject(i);
                        name = urlContent.getString("name");

                        if (name.equalsIgnoreCase("Arboretum website")) {
                            url = urlContent.getString("information");
                            Intent arboretumwebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(arboretumwebsiteIntent);
                            break;
                        } else if (name.equalsIgnoreCase("Facebook fan page")) {
                            url = urlContent.getString("information");
                            Intent arboretumwebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(arboretumwebsiteIntent);
                            break;
                        } else if (name.equalsIgnoreCase("Northwest website")) {
                            url = urlContent.getString("information");
                            Intent arboretumwebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(arboretumwebsiteIntent);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        isConnected = InternetConnection.isConnected(activity);
        if (this.isVisible()) {
            Log.d("---", "--");
        }
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyActivity.TAG = "HOME";
        //Setting the title of the action bar.
        getActivity().getActionBar().setTitle("Missouri Arboretum");
    }
}
