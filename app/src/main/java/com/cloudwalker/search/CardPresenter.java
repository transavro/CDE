package com.cloudwalker.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cloudwalker.CDEServiceOuterClass;


public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 305;
    private static final int CARD_HEIGHT = 180;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;
//    String carouselBaseUrl = "http://cloudwalker-assets-prod.s3.ap-south-1.amazonaws.com/images/tiles/";
    String carouselBaseUrl = "http://asset.s4.cloudwalker.tv/images/tiles/";


    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.transparent);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.youtube_red);
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };

        //***************for the selected strip at the bottom do this ***********
//        BaseCardView.LayoutParams layoutParams = (BaseCardView.LayoutParams) cardView.findViewById(R.id.info_field).getLayoutParams();
//        layoutParams.height = 10;
//        cardView.findViewById(R.id.info_field).setLayoutParams(layoutParams);

        cardView.setFocusable(true);
        cardView.setCardType(ImageCardView.CARD_REGION_VISIBLE_ACTIVATED);
        cardView.setFocusableInTouchMode(true);
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Object item) {
        final ImageCardView cardView = (ImageCardView) viewHolder.view;
        CDEServiceOuterClass.Optimus movie = (CDEServiceOuterClass.Optimus) item;
        ((ImageCardView)viewHolder.view).setTitleText(movie.getMetadata().getTitle());
        StringBuilder contentText = new StringBuilder();
        for(CDEServiceOuterClass.ContentAvailable p : movie.getContentAvailableList()){
            contentText.append(p.getSource()).append(" | ");
        }
        ((ImageCardView)viewHolder.view).setContentText(contentText.toString());
        if (!movie.getMedia().getLandscapeList().isEmpty() && !movie.getMedia().getLandscape(0).isEmpty()) {
            Glide.with(viewHolder.view.getContext())
                    .load(carouselBaseUrl + ((CDEServiceOuterClass.Optimus) item).getMedia().getLandscape(0))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(CARD_WIDTH, CARD_HEIGHT)
                    .error(R.drawable.movie)
                    .skipMemoryCache(true)
                    .placeholder(R.color.shimmer_bg_color)
                    .error(R.color.shimmer_bg_color)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(viewHolder.view.getContext()).load(((CDEServiceOuterClass.Optimus) item).getMedia().getLandscape(0))
                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                    .override(CARD_WIDTH, CARD_HEIGHT)
                                    .error(R.drawable.movie)
                                    .skipMemoryCache(true).into(cardView.getMainImageView());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(cardView.getMainImageView());
        }
    }

    private int dpToPx(Context ctx, int dp) {
        float density = ctx.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
