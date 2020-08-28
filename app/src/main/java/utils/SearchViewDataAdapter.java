//package utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.util.DiffUtil;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.cloudwalker.search.R;
//
//import java.util.ArrayList;
//
//import cde.CDEServiceOuterClass;
//
//
//public class SearchViewDataAdapter extends RecyclerView.Adapter<SearchViewDataAdapter.ViewHolder> {
//
//    private static final String TAG = "SearchViewDataAdapter";
//    private Context mContext;
//    private ArrayList<CDEServiceOuterClass.Optimus> rowItems;
//
//    public SearchViewDataAdapter(Context context) {
//        this.mContext = context;
//        rowItems = new ArrayList<>();
//    }
//
//    public void addData(ArrayList<CDEServiceOuterClass.Optimus> newData) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallBack(newData, rowItems));
//        diffResult.dispatchUpdatesTo(this);
//        rowItems.clear();
//        this.rowItems.addAll(newData);
//    }
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_search_result, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//
//        if (!rowItems.get(position).getMedia().getLandscapeList().isEmpty() && rowItems.get(position).getMedia().getLandscape(0) != null && !rowItems.get(position).getMedia().getLandscape(0).isEmpty()) {
//            Log.d(TAG, "onBindViewHolder: " + rowItems.get(position).getMedia().getLandscape(0) + "    " + rowItems.get(position).getMetadata().getTitle());
//            String carouselBaseUrl = "http://cloudwalker-assets-prod.s3.ap-south-1.amazonaws.com/images/tiles/";
//            Glide.with(mContext)
//                    .load(carouselBaseUrl + rowItems.get(position).getMedia().getLandscape(0))
//                    .placeholder(R.color.shimmer_bg_color)
//                    .error(R.color.shimmer_bg_color)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Glide.with(mContext).load(rowItems.get(position).getMedia().getLandscape(0)).into(holder.imgViewSearch);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    }).into(holder.imgViewSearch);
//        }
//
//        holder.tvSearchTitle.setText(rowItems.get(position).getMetadata().getTitle());
//
////        Log.e("SearchResult ", "adapter" + rowItems.get(position).getTitle());
//
//        holder.parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    holder.layoutSelector.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drawable_search_tile_focus));
//                } else {
//                    holder.layoutSelector.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drawable_un_focused));
//                }
//            }
//        });
//
//        holder.parentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(v.getContext(), "Not Implemented Yet !!!", Toast.LENGTH_SHORT).show();
////                try {
////                    handleTileClick(rowItems.get(position), v.getContext());
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//            }
//        });
//
//    }
//
//
////    private void handleTileClick(CDEServiceOuterClass.Optimus contentTile, Context context) {
////        //check if the package is there or not
////        if (!isPackageInstalled(contentTile.get.getPlay(0).getPackage(), context.getPackageManager())) {
////            Toast.makeText(context, "App not installed " + contentTile.getPlay(0).getPackage(), Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        if (contentTile.getPlay(0).getPackage().contains("youtube")) {
////            if (contentTile.getPlay(0).getTarget().startsWith("PL")) {
////                startYoutube("OPEN_PLAYLIST", context, contentTile.getPlay(0).getTarget());
////            } else if (contentTile.getPlay(0).getTarget().startsWith("UC")) {
////                startYoutube("OPEN_CHANNEL", context, contentTile.getPlay(0).getTarget());
////            } else {
////                startYoutube("PLAY_VIDEO", context, contentTile.getPlay(0).getTarget());
////            }
////            return;
////        }
////
////        if (contentTile.getPlay(0).getTarget().isEmpty()) {
////            Intent intent = context.getPackageManager().getLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
////            if (intent == null) {
////                intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
////            }
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(intent);
////            return;
////        }
////
////        //if package is installed
////        if (contentTile.getPlay(0).getPackage().contains("hotstar")) {
////            // if hotstar
////            try {
////                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
////                intent.setPackage(contentTile.getPlay(0).getPackage());
////                context.startActivity(intent);
////            } catch (ActivityNotFoundException e) {
////                e.printStackTrace();
////                Intent intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(contentTile.getPlay(0).getPackage());
////                if (contentTile.getPlay(0).getTarget().contains("https")) {
////                    intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("https://www.hotstar.com", "hotstar://content")));
////                } else {
////                    intent.setData(Uri.parse(contentTile.getPlay(0).getTarget().replace("http://www.hotstar.com", "hotstar://content")));
////                }
////                context.startActivity(intent);
////            }
////        } else {
////            // if other app
////            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentTile.getPlay(0).getTarget()));
////            intent.setPackage(contentTile.getPlay(0).getPackage());
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(intent);
////        }
////    }
////
////    private boolean startYoutube(String type, Context mActivity, String target) {
////        if (type.compareToIgnoreCase("PLAY_VIDEO") == 0 || type.compareToIgnoreCase("CWYT_VIDEO") == 0) {
////            Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mActivity, target, true, true);
////            intent.setPackage("com.google.android.youtube.tv");
////            mActivity.startActivity(intent);
////        } else if (type.compareToIgnoreCase("OPEN_PLAYLIST") == 0) {
////            Intent intent = YouTubeIntents.createOpenPlaylistIntent(mActivity, target);
////            intent.setPackage("com.google.android.youtube.tv");
////            intent.putExtra("finish_on_ended", true);
////            mActivity.startActivity(intent);
////        } else if (type.compareToIgnoreCase("PLAY_PLAYLIST") == 0 || type.compareToIgnoreCase("CWYT_PLAYLIST") == 0) {
////            Intent intent = YouTubeIntents.createPlayPlaylistIntent(mActivity, target);
////            intent.setPackage("com.google.android.youtube.tv");
////            intent.putExtra("finish_on_ended", true);
////            mActivity.startActivity(intent);
////        } else if (type.compareToIgnoreCase("OPEN_CHANNEL") == 0) {
////            Intent intent = YouTubeIntents.createChannelIntent(mActivity, target);
////            intent.setPackage("com.google.android.youtube.tv");
////            intent.putExtra("finish_on_ended", true);
////            mActivity.startActivity(intent);
////        } else if (type.compareToIgnoreCase("OPEN_USER") == 0) {
////            Intent intent = YouTubeIntents.createUserIntent(mActivity, target);
////            mActivity.startActivity(intent);
////        } else if (type.compareToIgnoreCase("OPEN_SEARCH") == 0) {
////            Intent intent = YouTubeIntents.createSearchIntent(mActivity, target);
////            mActivity.startActivity(intent);
////        } else {
////            return false;
////        }
////        return true;
////    }
////
//
//    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
//        try {
//            packageManager.getPackageInfo(packagename, 0);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return rowItems.size();
//    }
//
//    public void clear() {
//        final int size = rowItems.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                rowItems.remove(0);
//            }
//
//            notifyItemRangeRemoved(0, size);
//            notifyDataSetChanged();
//        }
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView imgViewSearch;
//        LinearLayout parentView, layoutSelector;
//        TextView tvSearchTitle;
//
//
//        public ViewHolder(View v) {
//            super(v);
//            imgViewSearch = (ImageView) v.findViewById(R.id.imgViewSearch);
//            parentView = v.findViewById(R.id.parentView);
//            layoutSelector = v.findViewById(R.id.layoutSelector);
//            tvSearchTitle = v.findViewById(R.id.tvSearchTitle);
//
//        }
//    }
//
//
//    public class MyDiffUtilCallBack extends DiffUtil.Callback {
//        ArrayList<CDEServiceOuterClass.Optimus> newList;
//        ArrayList<CDEServiceOuterClass.Optimus> oldList;
//
//        public MyDiffUtilCallBack(ArrayList<CDEServiceOuterClass.Optimus> newList, ArrayList<CDEServiceOuterClass.Optimus> oldList) {
//            this.newList = newList;
//            this.oldList = oldList;
//        }
//
//        @Override
//        public int getOldListSize() {
//            return oldList != null ? oldList.size() : 0;
//        }
//
//        @Override
//        public int getNewListSize() {
//            return newList != null ? newList.size() : 0;
//        }
//
//        @Override
//        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//            return newList.get(newItemPosition).getMetadata().getTitle().equals(oldList.get(oldItemPosition).getMetadata().getTitle());
//        }
//
//        @Override
//        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//            return newList.get(newItemPosition).getMetadata().getTitle().equals(oldList.get(oldItemPosition).getMetadata().getTitle());
//        }
//
//        @Nullable
//        @Override
//        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//
//            CDEServiceOuterClass.Optimus newModel = newList.get(newItemPosition);
//            CDEServiceOuterClass.Optimus oldModel = oldList.get(oldItemPosition);
//
//            Bundle diff = new Bundle();
//
//            if (!newModel.getMetadata().getTitle().equals(oldModel.getMetadata().getTitle())) {
//                diff.putString("title", newModel.getMetadata().getTitle());
//            }
//            if (diff.size() == 0) {
//                return null;
//            }
//            return diff;
//        }
//    }
//
//
//
//}
//
//
//
//
//
//
//
