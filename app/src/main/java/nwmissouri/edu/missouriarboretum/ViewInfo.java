package nwmissouri.edu.missouriarboretum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class ViewInfo extends Activity {

    private ListView mListview;
    private SearchView mSeaerchView;
    private TreeBean mtreeBean;
    private String treeDataFilePath = TreeJsonParser.CACHE_DIR + File.separator + "treeinfo.nw";
    private ArrayList<TreeBean> treeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_treeinformationsample);

        mListview = (ListView) findViewById(R.id.List);
        //mSeaerchView=(SearchView)findViewById(R.id.searchView1);

        TreeJsonParser parser = new TreeJsonParser(ViewInfo.this, "http://csgrad10.nwmissouri.edu/arboretum/treetable.php");


//        TreeImageJsonParser imageParser=new TreeImageJsonParser(ViewInfo.this, "http://csgrad10.nwmissouri.edu/arboretum/images.php");
//        imageParser.getImages();

        treeList = parser.getTreeList();

        //treeList=readTreeDataFromFile();


        TreeAdapter adapter = new TreeAdapter(ViewInfo.this, android.R.layout.simple_list_item_1, treeList);
        mListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TreeBean bean = (TreeBean) parent.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), bean.getTreeId(), Toast.LENGTH_LONG).show();


                Intent i = new Intent(ViewInfo.this, ResultAct.class);
                i.putExtra("tree", bean);
                startActivity(i);

            }
        });
    }


    private ArrayList<TreeBean> readTreeDataFromFile() {
        File data = new File(treeDataFilePath);


        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data));
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>)
                return ((ArrayList<TreeBean>) obj);

            ois.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    class TreeAdapter extends ArrayAdapter<TreeBean> {
        ArrayList<TreeBean> localObj;

        public TreeAdapter(Context context, int resource, ArrayList<TreeBean> objects) {
            super(context, resource, objects);
            localObj = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);

            TextView name = (TextView) convertView.findViewById(R.id.TreecommonNameTV);
            TextView Tname = (TextView) convertView.findViewById(R.id.TreetrailnameTV);
            TextView tid = (TextView) convertView.findViewById(R.id.TreetrailIdTV);

            name.setText(localObj.get(position).getcName());
            Tname.setText(localObj.get(position).getsName());
            tid.setText(localObj.get(position).getTrailId());
            return convertView;
        }
    }
}
