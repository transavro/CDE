package utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

public class AppUtils {

    private static final String TAG = "AppUtils";

    static public boolean isServerReachable(Context context, String url) {
        //method 1
        try {
            Log.d(TAG, "isServerReachable: "+ url );
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("ping "+url);  //<- Try ping -c 1 www.serverURL.com
            int mPingResult = proc .waitFor();
            return mPingResult == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }


        //method 2
//        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            try {
//                URL urlServer = new URL(url);
//                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
//                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
//                urlConn.connect();
//                if (urlConn.getResponseCode() == 200) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (MalformedURLException e1) {
//                return false;
//            } catch (IOException e) {
//                return false;
//            }
//        }
//        return false;
    }
}
