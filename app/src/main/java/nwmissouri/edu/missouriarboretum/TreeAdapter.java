package nwmissouri.edu.missouriarboretum;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by S518637 on 10/25/2014.
 */
public class TreeAdapter implements ListAdapter {
    ArrayList<TreeBean> TreesList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public TreeAdapter(Context context, int resource, ArrayList<TreeBean> objects) {
        super();
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        TreesList = objects;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.TVcommonname = (TextView) v.findViewById(R.id.TreecommonNameTV);
            holder.TVtrailname = (TextView) v.findViewById(R.id.TreetrailnameTV);
            holder.TVtrailid = (TextView) v.findViewById(R.id.TreetrailIdTV);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.TVcommonname).execute(TreesList.get(position).getCname());
        holder.TVcommonname.setText(TreesList.get(position).getcName());
        holder.TVtrailname.setText(TreesList.get(position).getWalkName());
        holder.TVtrailid.setText(TreesList.get(position).getTrailId());


        return v;


    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    static class ViewHolder {

        public TextView TVcommonname;
        public TextView TVtrailname;
        public TextView TVtrailid;

    }

}





