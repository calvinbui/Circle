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

/**
 * The underlying adapter to a sortable rankings list view
 * @author Eric Harlow
 */
public final class LeaderboardViewAdapter extends BaseAdapter implements RemoveListener, DropListener {
    /**
     * An array to hold the Ranking IDs
     */
    private int[] mIds;
    /**
     * An array to hold the Ranking layout
     */
    private int[] mLayouts;
    /**
     * A layout inflater to make the layout visible
     */
    private LayoutInflater mInflater;
    /**
     * An array list of the rankings within a leaderboard
     */
    private ArrayList<Ranking> mContent;

    /**
     * A Constructor to initialise the adapter
     */
    public LeaderboardViewAdapter(Context context, int[] itemLayouts, int[] itemIDs, ArrayList<Ranking> content) {
        init(context,itemLayouts,itemIDs, content);
    }

    /**
     * Values to initialise the constructor with
     */
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
        // an asynctask to retrieve the user's name from their user id
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

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving names from the database.
     */
    private class RetrieveUserTask extends AsyncTask<Void, Void, String> {
        /**
         * A user ranking object
         */
        private Ranking r;
        /**
         * A ViewHolder TextView widget
         */
        private ViewHolder v;

        /**
         * Constructor which initialises the ranking and textview
         */
        private RetrieveUserTask(Ranking r, ViewHolder v) {
            this.r = r;
            this.v = v;
        }

        protected String doInBackground(Void... params) {
            // get user names from the circle
            return UserDAO.retrieveUserNames(r.getUser());
        }

        @Override
        protected void onPostExecute(String json) {
            // set TextView with the user's names
            v.text.setText(json);
        }
    }
}