package com.id11413010.circle.app.network;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;

/**
 * A service class for listening to Internet connectivity.
 */
public class NetworkCheck extends Service {
    public NetworkCheck() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Constants.LOG, "Started Network Check Service");
        //registers a receiver to receive a broadcast message
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Log.i(Constants.LOG, "BroadcastReceiver registered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregister the broadcast receiver when the service terminates
        unregisterReceiver(receiver);
    }

    /**
     * A broadcast receiver which will execute if an intent with the corresponding action is received.
     * Checks for network connectivity and toasts the user if there is no connection.
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(Constants.LOG, "Network state changed.");
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                Toast.makeText(getApplicationContext(), getText(R.string.noInternet).toString(), Toast.LENGTH_LONG).show();
                Log.i(Constants.LOG, "Network state: " + !noConnectivity);
            } else {
                Log.i(Constants.LOG, "Network state: " + !noConnectivity);
            }
        }
    };
}
