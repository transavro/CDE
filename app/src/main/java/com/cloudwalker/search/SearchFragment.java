package com.cloudwalker.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DiffCallback;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import cde.CDEServiceOuterClass;
import utils.PlayOnTv;


public class SearchFragment extends VerticalGridSupportFragment {

    private ArrayObjectAdapter rowAdapter;
    private static final String TAG = "SearchFragment";
    private PlayOnTv playOnTv;

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

    public void refreshData(List<CDEServiceOuterClass.ContentDelivery> data) {
        prepareEntranceTransition();
        Log.d(TAG, "refreshData: " + rowAdapter);
        if (rowAdapter != null)
            rowAdapter.setItems(data, DIFF_UTILS);
        startEntranceTransition();
    }

    public void clearData() {
        rowAdapter.clear();
    }

    private PlayOnTv getPlayOnTv(Context context){
        if(playOnTv == null){
            playOnTv = new PlayOnTv(context);
        }
        return  playOnTv;
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
                if (item instanceof CDEServiceOuterClass.ContentDelivery) {
                    logAnalyticsEvent((CDEServiceOuterClass.ContentDelivery) item);
                    handleTileClick((CDEServiceOuterClass.ContentDelivery) item, itemViewHolder.view.getContext(), "");
                }
            }
        });
    }

    private void logAnalyticsEvent(CDEServiceOuterClass.ContentDelivery item) {

        for (int tileIndex = 0; tileIndex < getAdapter().size(); tileIndex++) {
            CDEServiceOuterClass.ContentDelivery content = (CDEServiceOuterClass.ContentDelivery) getAdapter().get(tileIndex);
            if (content.getTitle().equals(item.getTitle())) {
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


    public void handleTileClick(CDEServiceOuterClass.ContentDelivery contentTile, Context context, String preferredSource) {
        if (contentTile.getPlayList().size() == 0) return;
        if (contentTile.getPlayList().get(0).getPackage().isEmpty()) return;

        //log content select analytics
        if ((getActivity()) != null) {
            ((SearchActivity) getActivity()).logContentSelected(contentTile.getContentId(), contentTile.getTitle());
        }

        if (!preferredSource.isEmpty()) {
            for (CDEServiceOuterClass.PLAY p : contentTile.getPlayList()) {
                if (p.getSource().toLowerCase().equals(preferredSource.toLowerCase())) {
                    getPlayOnTv(context).trigger(p.getPackage(), p.getTarget());
                    return;
                }
            }
        }
        getPlayOnTv(context).trigger(contentTile.getPlay(0).getPackage(), contentTile.getPlay(0).getTarget());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rowAdapter.clear();
        rowAdapter = null;
    }
}
