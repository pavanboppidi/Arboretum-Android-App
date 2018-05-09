package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link nwmissouri.edu.missouriarboretum.Treevia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Treevia extends Fragment {
    //    Random rand=new Random();
    TextView questionTV;
    TextView answerTV;
    Bundle bundle=null;
    JSONArray treevia;
    JSONObject treeviaContent;
    boolean isConnected;
    boolean networkInfo;
    String question;
    int randomIndex;
    SharedPreferences sharedPref;
    static final String QUESTION = "Question";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Treevia1.
     */
    // TODO: Rename and change types and number of parameters
    public static Treevia newInstance(String param1, String param2) {
        Treevia fragment = new Treevia();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Treevia() {
        // Required empty public constructor
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

   /* @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            questionTV.setText(savedInstanceState.getString(QUESTION));
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }*/

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
//            savedInstanceState.putString(QUESTION, getActivity().getString(Integer.parseInt((treeviaContent.getString("question")))));
//            savedInstanceState.pu
            treeviaContent= (JSONObject) treevia.get(randomIndex);
//            question=treeviaContent.getString("question");
            outState.putString(QUESTION, treeviaContent.getString("question"));
//            savedInstanceState.putString(QUESTION,question.toString());
//            sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(QUESTION, question);
//            editor.commit();
//            outState.putString(QUESTION,editor.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/


    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        savedInstanceState.getString(QUESTION);
        if(savedInstanceState!=null) {
            questionTV= (TextView) getActivity().findViewById(R.id.questionTV);
//            sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//            String ques=sharedPref.getString(QUESTION,"");
//            questionTV.setText(ques);
//            ques=null;
//            questionTV.setText(savedInstanceState.getString(sharedPref.getString(QUESTION,null)));
//            questionTV.setText(savedInstanceState.getString(QUESTION));
//            String ques=savedInstanceState.getString(QUESTION);
//            Log.d("QUES",ques);
              questionTV.setText(question);
//            int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
//            long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sharedPref=getActivity().getPreferences(Context.MODE_PRIVATE);
//        questionTV = (TextView) getActivity().findViewById(R.id.questionTV);
//        answerTV = (TextView) getActivity().findViewById(R.id.answerTV);

//        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        if (getArguments() != null) {
//
//        }
//        setRetainInstance(true);
        /*if (savedInstanceState != null) {
            // Restore value of members from saved state
            questionTV.setText(savedInstanceState.getString(QUESTION));
        } else {
            // Probably initialize members with default values for a new instance
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        inflater=(LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_treevia, container, false);
//        this.setRetainInstance(true);
//        final WebView webview = (WebView) getActivity().findViewById(R.id.treevia);
        /*if(savedInstanceState!=null) {
            questionTV = (TextView) getActivity().findViewById(R.id.questionTV);
            try {
                question = savedInstanceState.getString(QUESTION);

//            questionTV.setText(savedInstanceState.getString(QUESTION));
                questionTV.setText(question);
            }catch (NullPointerException npe){
                npe.printStackTrace();
            }
        }*/
        downloadJSON(view);
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }g

    public void downloadJSON(View v) {
        try {
            URL url = new URL("http://csgrad10.nwmissouri.edu/arboretum/treevia.php");
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
            //System.out.println(jsonToParse);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
//            System.out.printf("We have read %d lines\n", progress[0]);

        }

        protected void onPostExecute(Long result) {
            if (isConnected) {
                try {
//                    Log.d("INTERNET", "Internet is connected");
//                    Log.d("NETWORK", "The Network is " + networkInfo);
//                    JSONArray treevia = new JSONArray(jsonToParse);
                    treevia = new JSONArray(jsonToParse);
                    //final int index=rand.nextInt(treevia.length());
//                int max = treevia.length();
                    final List<Integer> indices = new ArrayList<Integer>(treevia.length());
                    for (int c = 0; c < treevia.length(); ++c) {
                        indices.add(c);
                    }
                    final int arrIndex = (int) ((double) indices.size() * Math.random());
                    int randomIndex = indices.get(arrIndex);
                    indices.remove(arrIndex);
//                int index = (int)((double)treevia.length() * Math.random());
//                    final JSONObject treeviaContent = (JSONObject) treevia.get(randomIndex);
                    treeviaContent= (JSONObject) treevia.get(randomIndex);
//                    setTreeviaContent(treeviaContent);
                    /*try {
                        Bundle bundle1=null;
                        String ques=treeviaContent.getString("question");
//                        bundle1.putString("Question", ques);
//                        Bundle bundle1=new Bundle(Integer.parseInt(treeviaContent.getString("question")));
//                        bundle1.
                        onSaveInstanceState(bundle1.getBundle("Question"));
                    }catch (NullPointerException npe){
                        npe.printStackTrace();
                    }*/

                    questionTV = (TextView) getActivity().findViewById(R.id.questionTV);
                    answerTV = (TextView) getActivity().findViewById(R.id.answerTV);
                    try {
//                        question=treeviaContent.getString("question");
                        questionTV.setText(treeviaContent.getString("question"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ImageButton AnswerIBTN = (ImageButton) getActivity().findViewById(R.id.answerIBTN);
                    AnswerIBTN.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            try {
                                answerTV.setText(treeviaContent.getString("answer"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    ImageButton moreTreeviaIBTN = (ImageButton) getActivity().findViewById(R.id.moreTreeviaIBTN);
                    moreTreeviaIBTN.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            answerTV.setText("");
                            downloadJSON(view);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            isConnected = InternetConnection.isConnected(activity);
            networkInfo = InternetConnection.isNetworkAvailable(activity);
//            mListener = (OnFragmentInteractionListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().getActionBar()
                .setTitle("Treevia");
    }

}
