package com.id11413010.circle.app.leaderboards;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.LeaderboardDAO;
import com.id11413010.circle.app.pojo.Ranking;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class LeaderboardView extends ListActivity {

    private ArrayList<Ranking> arrayList = null;
    private LeaderboardViewAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listleaderboard);
        // initialise arrayList
        arrayList = new ArrayList<Ranking>();
        new RetrieveRankingsTask().execute();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new LeaderboardViewAdapter(this, new int[]{R.layout.leaderboarddragitem}, new int[]{R.id.TextView01}, arrayList);
        setListAdapter(adapter);//new DragNDropAdapter(this,content)
        ListView listView = getListView();

        if (listView instanceof DragNDropListView) {
            ((DragNDropListView) listView).setDropListener(mDropListener);
            ((DragNDropListView) listView).setRemoveListener(mRemoveListener);
            ((DragNDropListView) listView).setDragListener(mDragListener);
        }
    }

    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof LeaderboardViewAdapter) {
                        ((LeaderboardViewAdapter)adapter).onDrop(from, to);
                        getListView().invalidateViews();
                    }
                }
            };

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof LeaderboardViewAdapter) {
                        ((LeaderboardViewAdapter)adapter).onRemove(which);
                        getListView().invalidateViews();
                    }
                }
            };

    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = 0xe0103010;
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView) {
                    // TODO Auto-generated method stub
                }

                public void onStartDrag(View itemView) {
                    itemView.setVisibility(View.INVISIBLE);
                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
                    if (iv != null) iv.setVisibility(View.INVISIBLE);
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
                    if (iv != null) iv.setVisibility(View.VISIBLE);
                }

            };

    private class RetrieveRankingsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            Intent intent = new Intent();
            return LeaderboardDAO.retrieveRankings(intent.getIntExtra(Constants.LEADERBOARD_ID,0));
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<Ranking>>(){}.getType();
            List<Ranking> list = new Gson().fromJson(json, collectionType);
            for (Ranking r : list)
                arrayList.add(r);
            adapter.notifyDataSetInvalidated();
        }
    }
}
