package com.id11413010.circle.app.money;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.Money;

import java.util.List;

/**
 * An adapter which acts as a bridge between an activity and database.
 * Retrieves data form the database and updates a view for each item within a data set.
 * Responsible for displaying Money Owing.
 */
public class MoneyAdapter extends ArrayAdapter<Money> {
    /**
     * The index to a layout view
     */
    private int resource;
    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public MoneyAdapter(Context context, int resource, List<Money> moneys) {
        super(context, resource, moneys);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        RelativeLayout moneyList;
        // the object at the current array position
        Money m = getItem(position);
        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            moneyList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, moneyList, true);
        } else {
            moneyList = (RelativeLayout)convertView;
        }

        //finds and stores a view that was identified by the id attribute
        TextView ower = (TextView)moneyList.findViewById(R.id.owerFirstNamelist);
        TextView lendor = (TextView)moneyList.findViewById(R.id.lenderFirstNamelist);
        TextView amount = (TextView)moneyList.findViewById(R.id.moneyAmountOwing);
        //set the amount
        amount.setText(Double.toString(m.getAmount()));
        // retrieve owner and lendor first names
        new GetUserNames(ower, lendor).execute(m.getFrom(), m.getTo());
        //return the row
        return moneyList;
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving names from the database.
     */
    private class GetUserNames extends AsyncTask<Integer, Void, String[]> {
        /**
         * A TextView for the name of the owing user
         */
        private TextView owerName;
        /**
         * A TextView for the name of the lending user
         */
        private TextView lendorName;

        /**
         * Constructor for the AsyncTask.
         */
        private GetUserNames(TextView ower, TextView lendor) {
            owerName = ower;
            lendorName = lendor;
        }

        protected String[] doInBackground(Integer... params) {
            // return the name of the ower and lender
            return new String[] {
                    UserDAO.retrieveUserNames(params[0]),
                    UserDAO.retrieveUserNames(params[1])
            };
        }

        protected void onPostExecute(String... result) {
            // set the TextView to the ower and lendor names
            owerName.setText(result[0]);
            lendorName.setText(result[1]);
        }
    }
}
