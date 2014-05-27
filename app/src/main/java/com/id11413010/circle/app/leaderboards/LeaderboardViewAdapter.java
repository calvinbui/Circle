package com.id11413010.circle.app.leaderboards;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.Ranking;

import java.util.ArrayList;

public final class LeaderboardViewAdapter extends BaseAdapter implements RemoveListener, DropListener {
    private int[] mIds;
    private int[] mLayouts;
    private LayoutInflater mInflater;
    private ArrayList<Ranking> mContent;

    public LeaderboardViewAdapter(Context context, ArrayList<Ranking> content) {
        init(context,new int[]{android.R.layout.simple_list_item_1},new int[]{android.R.id.text1}, content);
    }

    public LeaderboardViewAdapter(Context context, int[] itemLayouts, int[] itemIDs, ArrayList<Ranking> content) {
        init(context,itemLayouts,itemIDs, content);
    }

    private void init(Context context, int[] layouts, int[] ids, ArrayList<Ranking> content) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        mIds = ids;
        mLayouts = layouts;
        mContent = content;
    }

    /**
     * The number of items in the list
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return mContent.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficient to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public Ranking getItem(int position) {
        return mContent.get(position);
    }

    /**
     * Use the array index as a unique id.
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(mLayouts[0], null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(mIds[0]);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        new RetrieveUserTask(mContent.get(position), holder).execute();
        // Bind the data efficiently with the holder.

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }

    public void onRemove(int which) {
        if (which < 0 || which > mContent.size()) return;
        mContent.remove(which);
    }

    public void onDrop(int from, int to) {
        Ranking temp = mContent.get(from);
        mContent.remove(from);
        mContent.add(to,temp);
    }

    private class RetrieveUserTask extends AsyncTask<Void, Void, String> {
        private Ranking r;
        private ViewHolder v;

        private RetrieveUserTask(Ranking r, ViewHolder v) {
            this.r = r;
            this.v = v;
        }

        protected String doInBackground(Void... params) {
            return UserDAO.retrieveUserNames(r.getUser());
        }

        @Override
        protected void onPostExecute(String json) {
            v.text.setText(json);
        }
    }
}