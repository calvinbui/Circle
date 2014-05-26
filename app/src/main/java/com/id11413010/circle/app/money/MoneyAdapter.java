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
 * TODO
 */
public class MoneyAdapter extends ArrayAdapter<Money> {
    private int resource;
    /**
     * Initialise the adapter
     */
    public MoneyAdapter(Context context, int resource, List<Money> moneys) {
        super(context, resource, moneys);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout moneyList;
        Money m = getItem(position);

        if(convertView == null) {
            moneyList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, moneyList, true);
        } else {
            moneyList = (RelativeLayout)convertView;
        }

        TextView ower = (TextView)moneyList.findViewById(R.id.owerFirstNamelist);
        TextView lendor = (TextView)moneyList.findViewById(R.id.lenderFirstNamelist);
        TextView amount = (TextView)moneyList.findViewById(R.id.moneyAmountOwed);
        amount.setText(R.string.dollarSign + Double.toString(m.getAmount()));
        new GetUserNames(ower, lendor).execute(m.getFrom(), m.getTo());

        return moneyList;
    }

    private class GetUserNames extends AsyncTask<Integer, Void, String[]> {
        private TextView owerName;
        private TextView lendorName;

        private GetUserNames(TextView ower, TextView lendor) {
            owerName = ower;
            lendorName = lendor;
        }

        protected String[] doInBackground(Integer... params) {
            return new String[] {
                    UserDAO.retrieveUserNames(params[0]),
                    UserDAO.retrieveUserNames(params[1])
            };
        }

        protected void onPostExecute(String... result) {
            owerName.setText(result[0]);
            lendorName.setText(result[1]);
        }
    }
}
