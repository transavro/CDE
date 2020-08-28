package com.cloudwalker.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cloudwalker.CDEServiceOuterClass;
import utils.PlayOnTv;


public class SearchFragment extends VerticalGridSupportFragment
{

    private ArrayObjectAdapter rowAdapter;
    private static final String TAG = "SearchFragment";

    private static DiffCallback<CDEServiceOuterClass.ContentDelivery> DIFF_UTILS = new DiffCallback<CDEServiceOuterClass.ContentDelivery>() {
        @Override
        public boolean areItemsTheSame(@NonNull CDEServiceOuterClass.ContentDelivery oldItem, @NonNull CDEServiceOuterClass.ContentDelivery newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CDEServiceOuterClass.ContentDelivery oldItem, @NonNull CDEServiceOuterClass.ContentDelivery newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    public void refreshData(List<CDEServiceOuterClass.ContentDelivery> data){
        prepareEntranceTransition();
        Log.d(TAG, "refreshData: "+rowAdapter);
        if(rowAdapter != null)
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
                if(item instanceof CDEServiceOuterClass.ContentDelivery){
                    logAnalyticsEvent((CDEServiceOuterClass.ContentDelivery) item);
                    handleTileClick((CDEServiceOuterClass.ContentDelivery) item, itemViewHolder.view.getContext(), "");
                }
            }
        });
    }

    private void logAnalyticsEvent(CDEServiceOuterClass.ContentDelivery item) {

        for(int tileIndex = 0 ; tileIndex < getAdapter().size() ; tileIndex++ ){
            CDEServiceOuterClass.ContentDelivery content = (CDEServiceOuterClass.ContentDelivery) getAdapter().get(tileIndex);
            if(content.getTitle().equals(item.getTitle())) {
                //FireBase Analytics Stuff
                Intent analyticsIntent = new Intent("tv.cloudwalker.cde.action.CLICKED");
                Bundle fireBundle = new Bundle();
                fireBundle.putString("TILE_ID", item.getContentId());
                fireBundle.putLong("TILE_INDEX", tileIndex);
                fireBundle.putString("TILE_TITLE", item.getTitle());
//                fireBundle.putString("TILE_SOURCE", item..getSource());
                fireBundle.putString("SEARCH_QUERY", ((SearchActivity) Objects.requireNonNull(getActivity())).getSearchText());
                analyticsIntent.putExtra("info", fireBundle);
                getActivity().sendBroadcast(analyticsIntent);
                break;
            }
        }
    }


    public void handleTileClick(CDEServiceOuterClass.ContentDelivery contentTile, Context context , String preferredSource) {
//        ((SearchActivity)getActivity()).logClickEvent(contentTile.getContentId());
        if(contentTile.getPlayList().size() == 0) return;
        if(contentTile.getPlayList().get(0).getPackage().isEmpty()) return;
//        showPlayPopup(context, contentTile);

        if(!preferredSource.isEmpty()){
            for (CDEServiceOuterClass.PLAY p: contentTile.getPlayList()) {
                if(p.getSource().toLowerCase().equals(preferredSource.toLowerCase())){
                    PlayOnTv playOnTv = new PlayOnTv(context, p.getPackage(), p.getTarget());
                    int r = playOnTv.trigger();
                    if(r == -1){
                        Toast.makeText(context, p.getPackage()+" is not installed.", Toast.LENGTH_SHORT).show();
                    }else if( r == 0){
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }

        PlayOnTv playOnTv = new PlayOnTv(context,
                contentTile.getPlay(0).getPackage(),
                contentTile.getPlay(0).getTarget());
        playOnTv.trigger();
    }


    private void showPlayPopup(final Context context, CDEServiceOuterClass.ContentDelivery contentTile){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Play on CloudTV");

        ArrayList<String> sources = new ArrayList<>();
        final ArrayList<CDEServiceOuterClass.PLAY> temp = new ArrayList<>();
        for (CDEServiceOuterClass.PLAY play: contentTile.getPlayList()) {
            if(!play.getSource().isEmpty() && !sources.contains(play.getSource())){
                sources.add(play.getSource());
                temp.add(play);
            }
        }

        builder.setItems(GetStringArray(sources), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                Toast.makeText(context, "Selected:" + index, Toast.LENGTH_SHORT).show();
                PlayOnTv playOnTv = new PlayOnTv(context,
                        temp.get(index).getPackage(),
                        temp.get(index).getTarget());
                playOnTv.trigger();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private String[] GetStringArray(ArrayList<String> arr){
        String[] str = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++)
            str[j] = arr.get(j);
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rowAdapter.clear();
        rowAdapter = null;
    }
}
