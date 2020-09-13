package com.cloudwalker.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import cde.CDEServiceOuterClass;
import utils.AppUtils;


public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 305;
    private static final int CARD_HEIGHT = 180;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    boolean fallback = false;
    int imageIndex = 0;
    private static final String TAG = "CardPresenter";
//    String carouselBaseUrl = "http://asset.s4.cloudwalker.tv/images/tiles/";


    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.transparent);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.dark_grey);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };


        cardView.setFocusable(true);
        cardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
        cardView.setFocusableInTouchMode(true);
        cardView.setClipToPadding(true);
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Object item) {
        final ImageCardView cardView = (ImageCardView) viewHolder.view;
        final CDEServiceOuterClass.ContentDelivery movie = (CDEServiceOuterClass.ContentDelivery) item;
        String title = movie.getTitle();
        if(movie.getSeason() != null && !movie.getSeason().isEmpty()){
            title = title + " S "+movie.getSeason()+ " E "+movie.getEpisode();
        }
        ((ImageCardView)viewHolder.view).setTitleText(title);
        StringBuilder contentText = new StringBuilder();
        for(CDEServiceOuterClass.PLAY p : movie.getPlayList()){
            contentText.append(p.getSource()).append(" | ");
        }
        ((ImageCardView)viewHolder.view).setContentText(contentText.toString());
        if(movie.getPosterCount() > 0){
            imageIndex = 0;
            loadPoster(cardView, movie.getPosterList());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }


    private List<String> filterValidNActiveUrl(Context context, CDEServiceOuterClass.ContentDelivery movie){
        List<String> result = new ArrayList<>();
        //merging
        for (String url: movie.getPosterList()) {
            if(AppUtils.isServerReachable(context, url)){
                result.add(url);
            }
        }
        for (String url: movie.getPortriatList()) {
            if(AppUtils.isServerReachable(context, url)){
                result.add(url);
            }
        }
        Log.d(TAG, "filterValidNActiveUrl: "+result.toString());
        return result;
    }




    private void loadPoster(final ImageCardView cardView, final List<String> imageUrlList){
        Log.d(TAG, "loadPoster: POSTER "+imageIndex);
        Glide.with(cardView.getContext())
                .load(imageUrlList.get(imageIndex))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(CARD_WIDTH, CARD_HEIGHT)
                .skipMemoryCache(true)
                .placeholder(R.color.shimmer_bg_color)
                .into(new ImageViewTarget<GlideDrawable>(cardView.getMainImageView()) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        Log.d(TAG, "setResource: IMAGE SUCCESS ");
                        cardView.getMainImageView().setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Log.d(TAG, "onLoadFailed: IMAGE FAILED ");
                        if(imageIndex < imageUrlList.size()){
                            imageIndex++;
                            Log.d(TAG, "onLoadFailed: IMAGE TRY ");
                            loadPoster(cardView, imageUrlList);
                        }
                    }
                });
    }
}













//Glide.with(cardView.getContext())
//        .load(movie.getPosterList().get(imageIndex))
//        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//        .override(CARD_WIDTH, CARD_HEIGHT)
//        .error(R.drawable.movie)
//        .skipMemoryCache(true)
//        .placeholder(R.color.shimmer_bg_color)
//        .error(R.color.shimmer_bg_color)
//        .listener(new RequestListener<String, GlideDrawable>() {
//@Override
//public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//        Log.d(TAG, "*****Glide onException: "+model);
//        if(!fallback && imageIndex < (movie.getPosterList().size()-1)){
//        Log.d(TAG, "loadPoster: trying image but failed ==> "+movie.getPosterList().get(imageIndex));
//        imageIndex = imageIndex + 1;
//        loadPoster(cardView, movie);
//        return false;
//        }
//        for (String i: movie.getPosterList()) {
//        Log.d(TAG, "This set didnt work out : "+i);
//        }
//        return false;
//        }
//
//@Override
//public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//        Log.d(TAG, "onResourceReady: "+model);
//        return false;
//        }
//        }).into(cardView.getMainImageView());











//***************for the selected strip at the bottom do this ***********
//        BaseCardView.LayoutParams layoutParams = (BaseCardView.LayoutParams) cardView.findViewById(R.id.info_field).getLayoutParams();
//        layoutParams.height = 10;
//        cardView.findViewById(R.id.info_field).setLayoutParams(layoutParams);





//    private int dpToPx(Context ctx, int dp) {
//        float density = ctx.getResources()
//                .getDisplayMetrics()
//                .density;
//        return Math.round((float) dp * density);
//    }






//   Glide.with(viewHolder.view.getContext())
//                    .load(carouselBaseUrl + (movie.getPoster(0)))
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .override(CARD_WIDTH, CARD_HEIGHT)
//                    .error(R.drawable.movie)
//                    .skipMemoryCache(true)
//                    .placeholder(R.color.shimmer_bg_color)
//                    .error(R.color.shimmer_bg_color)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Glide.with(viewHolder.view.getContext()).load(movie.getPoster(0))
//                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                                    .override(CARD_WIDTH, CARD_HEIGHT)
//                                    .error(R.drawable.movie)
//                                    .skipMemoryCache(true).into(cardView.getMainImageView());
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    }).into(cardView.getMainImageView());
