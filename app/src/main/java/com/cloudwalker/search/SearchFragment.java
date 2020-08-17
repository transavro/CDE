package com.cloudwalker.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.VerticalGridSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DiffCallback;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import com.google.android.youtube.player.YouTubeIntents;

import java.util.List;

import cloudwalker.CDEServiceOuterClass;


public class SearchFragment extends VerticalGridSupportFragment
{

    private ArrayObjectAdapter rowAdapter;
    private static final String TAG = "SearchFragment";

    private static DiffCallback<CDEServiceOuterClass.Optimus> DIFF_UTILS = new DiffCallback<CDEServiceOuterClass.Optimus>() {
        @Override
        public boolean areItemsTheSame(@NonNull CDEServiceOuterClass.Optimus oldItem, @NonNull CDEServiceOuterClass.Optimus newItem) {
            return oldItem.getMetadata().getTitle().equals(newItem.getMetadata().getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CDEServiceOuterClass.Optimus oldItem, @NonNull CDEServiceOuterClass.Optimus newItem) {
            return oldItem.getMetadata().getTitle().equals(newItem.getMetadata().getTitle());
        }
    };

    public void refreshData(List<CDEServiceOuterClass.Optimus> data){
        prepareEntranceTransition();
        rowAdapter.setItems(data, DIFF_UTILS);
        startEntranceTransition();
    }


    public void clearData(){
       rowAdapter.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CardPresenter cardPresenter = new CardPresenter();
        rowAdapter = new ArrayObjectAdapter(cardPresenter);
        setUpGridUI();
        setAdapter(rowAdapter);
    }

    private void setUpGridUI() {
        setTitle("Content Discovery Engine");
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false);
        gridPresenter.enableChildRoundedCorners(true);
        gridPresenter.setKeepChildForeground(true);
        gridPresenter.setShadowEnabled(false);
        gridPresenter.setNumberOfColumns(4);
        setGridPresenter(gridPresenter);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if(item instanceof CDEServiceOuterClass.Optimus){
                    logAnalyticsEvent((CDEServiceOuterClass.Optimus) item);
//                    handleTileClick((CDEServiceOuterClass.Optimus) item, itemViewHolder.view.getContext());
                }
            }
        });
    }

    private void logAnalyticsEvent(CDEServiceOuterClass.Optimus item) {

        for(int tileIndex = 0 ; tileIndex < getAdapter().size() ; tileIndex++ ){
            CDEServiceOuterClass.Optimus content = (CDEServiceOuterClass.Optimus) getAdapter().get(tileIndex);
            if(content.getMetadata().getTitle().equals(item.getMetadata().getTitle())) {
                //FireBase Analytics Stuff
                Intent analyticsIntent = new Intent("tv.cloudwalker.cde.action.CLICKED");
                Bundle fireBundle = new Bundle();
                fireBundle.putString("TILE_ID", item.getRefId());
                fireBundle.putLong("TILE_INDEX", tileIndex);
                fireBundle.putString("TILE_TITLE", item.getMetadata().getTitle());
//                fireBundle.putString("TILE_SOURCE", item..getSource());
                fireBundle.putString("SEARCH_QUERY", ((SearchActivity)getActivity()).getSearchText());
                analyticsIntent.putExtra("info", fireBundle);
                getActivity().sendBroadcast(analyticsIntent);
                break;
            }
        }
    }


//    public void handleTileClick(CDEServiceOuterClass.Optimus contentTile, Context context) {
//
//        if(contentTile.getPlayList().size() == 0) return;
//        if(contentTile.getPlayList().get(0).getPackage().isEmpty()) return;
//
//
//        for(CDEServiceOuterClass.Play p : contentTile.getPlayList()){
//            Log.d(TAG, "handleTileClick: package ==> "+p.getPackage());
//            Log.d(TAG, "handleTileClick: source ==> "+p.getSource());
//            Log.d(TAG, "handleTileClick: target ==> "+p.getTarget());
//            Log.d(TAG, "handleTileClick: Monetize ==> "+p.getMonetize());
//        }
//
//        String packageName = contentTile.getPlayList().get(0).getPackage();
//
//        if(packageName.contains("youtube")) packageName = "com.google.android.youtube.tv";
//
//        if(!isPackageInstalled(packageName, context.getPackageManager())){
//            Intent appStoreIntent = context.getPackageManager().getLeanbackLaunchIntentForPackage("com.replete.cwappstore");
//            if (appStoreIntent == null) return;
//            appStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(appStoreIntent);
//            return;
//        }
//
//
//        if (contentTile.getPlay(0).getPackage().contains("youtube")) {
//            if (contentTile.getPlay(0).getTarget().startsWith("PL")) {
//                startYoutube("OPEN_PLAYLIST", context, contentTile.getPlay(0).getTarget());
//            } else if (contentTile.getPlay(0).getTarget().startsWith("UC")) {
//                startYoutube("OPEN_CHANNEL", context, contentTile.getPlay(0).getTarget());
//            } else {
//                startYoutube("PLAY_VIDEO", context, contentTile.getPlay(0).getTarget());
//            }
//            return;
//        }
//
//        //check if the package is there or not
//        if (!isPackageInstalled(contentTile.getPlay(0).getPackage(), context.getPackageManager())) {
//            Toast.makeText(context, "App not installed " + contentTile.getPlay(0).getPackage(), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (contentTile.getPlay(0).getTarget().isEmpty()) {
//            Intent intent = context.getPackageManager().getLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
//            if (intent == null) {
//                intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            return;
//        }
//
//        //if package is installed
//        if (contentTile.getPlay(0).getPackage().contains("hotstar")) {
//            // if hotstar
//            try {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
//                intent.setPackage(contentTile.getPlay(0).getPackage());
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                e.printStackTrace();
//                Intent intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
//                if (contentTile.getPlay(0).getTarget().contains("https")) {
//                    intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("https://www.hotstar.com", "hotstar://content")));
//                } else {
//                    intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("http://www.hotstar.com", "hotstar://content")));
//                }
//                context.startActivity(intent);
//            }
//        } else {
//            // if other app
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
//            intent.setPackage(contentTile.getPlay(0).getPackage());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//    }

    private boolean startYoutube(String type, Context mActivity, String target) {
        if (type.compareToIgnoreCase("PLAY_VIDEO") == 0 || type.compareToIgnoreCase("CWYT_VIDEO") == 0) {
            Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mActivity, target, true, true);
            intent.setPackage("com.google.android.youtube.tv");
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_PLAYLIST") == 0) {
            Intent intent = YouTubeIntents.createOpenPlaylistIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("PLAY_PLAYLIST") == 0 || type.compareToIgnoreCase("CWYT_PLAYLIST") == 0) {
            Intent intent = YouTubeIntents.createPlayPlaylistIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_CHANNEL") == 0) {
            Intent intent = YouTubeIntents.createChannelIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_USER") == 0) {
            Intent intent = YouTubeIntents.createUserIntent(mActivity, target);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_SEARCH") == 0) {
            Intent intent = YouTubeIntents.createSearchIntent(mActivity, target);
            mActivity.startActivity(intent);
        } else {
            return false;
        }
        return true;
    }


    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rowAdapter.clear();
        rowAdapter = null;
    }
}
