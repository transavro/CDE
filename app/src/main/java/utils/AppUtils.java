package utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
    }

    public static String getSystemProperty(String key) {
        String value = null;
        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getEthMacAddress() {
        try {
            return loadFileAsString().toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String loadFileAsString() throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader("/sys/class/net/eth0/address"));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        Log.i(TAG, "isPackageInstalled: "+packagename);
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
