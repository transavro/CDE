package utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.cloudwalker.search.SearchActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class AppUtils {

    private static final String TAG = "AppUtils";
    private static volatile Method set = null;


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

    public static void setSystemProperty(String prop, String value) {
        try {
            if (null == set) {
                synchronized (SearchActivity.class) {
                    if (null == set) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        set = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
                    }
                }
            }
            set.invoke(null, new Object[]{prop, value});
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
