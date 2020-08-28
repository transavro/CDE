package utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;


//***********passed
//zee5
//youtube
//hotstar

//*********failed
//sunnext
//yupptv
//sonylive failed partial





public class PlayOnTv {
    String packageName, deeplink;
    Context context;
    private static final String TAG = "PlayOnTv";
    private String[] cwPartner = {"com.zee5.aosp", "in.startv.hotstar", "com.sonyliv"};


    public PlayOnTv(Context context , String packageName, String targetUrl){
        this.context = context;
        this.packageName = packageName;
        this.deeplink = targetUrl;
    }

    public int trigger(){
        if(context == null || packageName == null || deeplink == null) return 0;

        //manupilate acoording to CW
        manupilateCW();

        //check if the app is installed or not
        if(!isPackageInstalled(packageName, context.getPackageManager())){
//            Toast.makeText(context.getApplicationContext(), packageName+ " is not installed.", Toast.LENGTH_SHORT).show();
            //TODO Go To AppStore
            goToAppStore(packageName);
            return -1;
        }

        //if app is installed
        return play(packageName, deeplink);
    }



    private void manupilateCW() {
        Log.d(TAG,"CONTENT PLAY START ===>>>  "+ packageName + "      "+ deeplink);
        if (deeplink == null) {
            deeplink = "";
        }

        if (packageName.contains("youtube")) {
            if (!deeplink.contains("https://")) {
                packageName = packageName + ".tv";

                if (deeplink.startsWith("PL") || deeplink.startsWith("RD")) {
                    deeplink = "https://www.youtube.com/playlist?list=$deeplink";
                } else if (deeplink.startsWith("UC")) {
                    deeplink = "https://www.youtube.com/channel/$deeplink";
                } else {
                    deeplink = "https://www.youtube.com/watch?v=$deeplink";
                }
            }
        } else if (packageName.contains("graymatrix")) {
            packageName = "com.zee5.aosp";
        } else if (packageName.contains("amazon")) {
            deeplink = deeplink.replaceFirst("https://www", "intent://app");
            String[] result = deeplink.split("\\?");
            if (result.length > 1) {
                deeplink = deeplink.replaceAll(result[1], "");
            }
            deeplink = deeplink + "&time=500";
        } else if (packageName.contains("hotstar")) {
            deeplink = deeplink.replaceFirst("https://www.hotstar.com", "hotstar://content");
            deeplink = deeplink.replaceFirst("http://www.hotstar.com", "hotstar://content");
        } else if (packageName.contains("jio")) {
            packageName = "com.jio.media.stb.ondemand";
        }else if(packageName.equals("com.tru")){
            packageName = "com.yupptv.cloudwalker";
        }else if(packageName.equals("com.sonyliv")){
            deeplink = deeplink.replace( "https://www.sonyliv.com/details/full movie", "sony://player");
            String[] tmp = deeplink.split("/");
            deeplink = deeplink.replace(tmp[tmp.length - 1], "");
        }

        //playing...
        Log.d(TAG,"CONTENT PLAY END ===>>>  "+ packageName + "      "+ deeplink);
    }

    private int play(String packageName, String deeplink){
        //making intent
        Intent playIntent = new Intent();
        playIntent.setPackage(packageName);
        playIntent.setData(Uri.parse(deeplink));
        playIntent.setAction(Intent.ACTION_VIEW);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            context.startActivity(playIntent);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    private void goToAppStore(String packageName){
        Intent intent = new Intent();
        if (Arrays.asList(cwPartner).contains(packageName)){
            //go to cloudwalker appstore
            String uri = "cwmarket://appstore?package=" + packageName;
            intent.setData(Uri.parse(uri));
            intent.setPackage("tv.cloudwalker.market");
            intent.setClassName( "tv.cloudwalker.market" , "tv.cloudwalker.market.activity.AppDetailsActivity" );


        }else{
            //go to cvte Appstore
            String uri = "appstore://appDetail?package=" + packageName;
            intent.setData(Uri.parse(uri));
            intent.setPackage("com.stark.store");
            intent.setClassName( "com.stark.store" , "com.stark.store.ui.detail.AppDetailActivity" );
        }
        try{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            Log.e(TAG, "goToAppStore:Error while triggering AppStore ", e);
        }
    }


    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}











/*   for(CDEServiceOuterClass.PLAY p : contentTile.getPlayList()){
           Log.d(TAG, "handleTileClick: package ==> "+p.getPackage());
           Log.d(TAG, "handleTileClick: source ==> "+p.getSource());
           Log.d(TAG, "handleTileClick: target ==> "+p.getTarget());
           Log.d(TAG, "handleTileClick: Monetize ==> "+p.getMonetize());
           }

           String packageName = contentTile.getPlayList().get(0).getPackage();

           if(packageName.contains("youtube")) packageName = "com.google.android.youtube.tv";

           if(!isPackageInstalled(packageName, context.getPackageManager())){
           Intent appStoreIntent = context.getPackageManager().getLeanbackLaunchIntentForPackage("com.replete.cwappstore");
           if (appStoreIntent == null) return;
           appStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(appStoreIntent);
           return;
           }


           if (contentTile.getPlay(0).getPackage().contains("youtube")) {
           if (contentTile.getPlay(0).getTarget().startsWith("PL")) {
           startYoutube("OPEN_PLAYLIST", context, contentTile.getPlay(0).getTarget());
           } else if (contentTile.getPlay(0).getTarget().startsWith("UC")) {
           startYoutube("OPEN_CHANNEL", context, contentTile.getPlay(0).getTarget());
           } else {
           startYoutube("PLAY_VIDEO", context, contentTile.getPlay(0).getTarget());
           }
           return;
           }

           //check if the package is there or not
           if (!isPackageInstalled(contentTile.getPlay(0).getPackage(), context.getPackageManager())) {
           Toast.makeText(context, "App not installed " + contentTile.getPlay(0).getPackage(), Toast.LENGTH_SHORT).show();
           return;
           }

           if (contentTile.getPlay(0).getTarget().isEmpty()) {
           Intent intent = context.getPackageManager().getLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
           if (intent == null) {
           intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
           }
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent);
           return;*/
//           }
//
//           //if package is installed
//           if (contentTile.getPlay(0).getPackage().contains("hotstar")) {
//           // if hotstar
//           try {
//           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
//           intent.setPackage(contentTile.getPlay(0).getPackage());
//           context.startActivity(intent);
//           } catch (ActivityNotFoundException e) {
//           e.printStackTrace();
//           Intent intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
//           if (contentTile.getPlay(0).getTarget().contains("https")) {
//           intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("https://www.hotstar.com", "hotstar://content")));
//           } else {
//           intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("http://www.hotstar.com", "hotstar://content")));
//           }
//           context.startActivity(intent);
//           }
//           } else {
//           // if other app
//           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
//           intent.setPackage(contentTile.getPlay(0).getPackage());
//           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//           context.startActivity(intent);
//           }






























//    private boolean startYoutube(String type, Context mActivity, String target) {
//        if (type.compareToIgnoreCase("PLAY_VIDEO") == 0 || type.compareToIgnoreCase("CWYT_VIDEO") == 0) {
//            Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mActivity, target, true, true);
//            intent.setPackage("com.google.android.youtube.tv");
//            mActivity.startActivity(intent);
//        } else if (type.compareToIgnoreCase("OPEN_PLAYLIST") == 0) {
//            Intent intent = YouTubeIntents.createOpenPlaylistIntent(mActivity, target);
//            intent.setPackage("com.google.android.youtube.tv");
//            intent.putExtra("finish_on_ended", true);
//            mActivity.startActivity(intent);
//        } else if (type.compareToIgnoreCase("PLAY_PLAYLIST") == 0 || type.compareToIgnoreCase("CWYT_PLAYLIST") == 0) {
//            Intent intent = YouTubeIntents.createPlayPlaylistIntent(mActivity, target);
//            intent.setPackage("com.google.android.youtube.tv");
//            intent.putExtra("finish_on_ended", true);
//            mActivity.startActivity(intent);
//        } else if (type.compareToIgnoreCase("OPEN_CHANNEL") == 0) {
//            Intent intent = YouTubeIntents.createChannelIntent(mActivity, target);
//            intent.setPackage("com.google.android.youtube.tv");
//            intent.putExtra("finish_on_ended", true);
//            mActivity.startActivity(intent);
//        } else if (type.compareToIgnoreCase("OPEN_USER") == 0) {
//            Intent intent = YouTubeIntents.createUserIntent(mActivity, target);
//            mActivity.startActivity(intent);
//        } else if (type.compareToIgnoreCase("OPEN_SEARCH") == 0) {
//            Intent intent = YouTubeIntents.createSearchIntent(mActivity, target);
//            mActivity.startActivity(intent);
//        } else {
//            return false;
//        }
//        return true;
//    }
//
