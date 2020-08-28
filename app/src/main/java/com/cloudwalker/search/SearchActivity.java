package com.cloudwalker.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cloudwalker.CDEServiceGrpc;
import cloudwalker.CDEServiceOuterClass;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import model.SearchFilter;
import utils.KeyboardDataAdapter;

public class SearchActivity extends FragmentActivity implements View.OnClickListener {

    RecyclerView rvKeyboardKeys;
    int numberOfColumns = 6;
    TextView tvSearchText, tvSerachMessage;
    EditText edtSearchView;
    Button btnDelete, btnSpace, btnClear;
    ProgressBar loadingProgressBar;
    private int finalCount;
    private String searchText = "";
    private static final String TAG = "SearchActivity";
    CDEServiceGrpc.CDEServiceStub cdeServiceStub;
    private SearchFragment searchFragment;
    private ManagedChannel managedChannel;
    private boolean isSearchNPlay = false;
    private boolean isFromDeepLink = false;
    private String sourceToPlay = "";
    private String[] cwCanTrigger = {"youtube", "netflix", "amazon prime", "zee5", "hotstar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        resolveVskDeepLink(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "********************8onNewIntent: ");
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void resolveVskDeepLink(Intent intent) {

        Log.d(TAG, "resolveVskDeepLink: ################################# GOT DEEEP LINK");
        String cmd = intent.getStringExtra("commandType");
        if(cmd != null && !cmd.isEmpty()){
            Log.d(TAG, "resolveVskDeepLink: cmd "+cmd);
        }

        String sf = intent.getStringExtra("searchFilter");
        sourceToPlay = "";

        if(sf == null) return;
        if(sf.isEmpty()) return;

        SearchFilter searchFilter = new Gson().fromJson(sf, SearchFilter.class);
        Log.d(TAG, "resolveVskDeepLink: searchFilter "+sf);

        if (searchFilter != null) {
            Log.d(TAG, "resolveVskDeepLink: got vsk serach ");
            Map<String , CDEServiceOuterClass.FilterKey> searchMeta = new HashMap<>();
            if(searchFilter.getGenres() != null && !searchFilter.getGenres().isEmpty() ){
                //genre added
                Log.d(TAG, "resolveVskDeepLink: genre  "+searchFilter.getGenres());
                searchMeta.put(
                        "metadata.genre", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addAllValues(searchFilter.getGenres())
                                .build()
                );
            }

            if(searchFilter.getAppName() != null && !searchFilter.getAppName().isEmpty() ){
                //source added
                Log.d(TAG, "resolveVskDeepLink: appName  "+searchFilter.getAppName());
                sourceToPlay = searchFilter.getAppName();
                searchMeta.put(
                        "content.sources", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addValues(searchFilter.getAppName())
                                .build()
                );
            }


            if(searchFilter.getMediaType() != null && !searchFilter.getMediaType().isEmpty() ){
                //categories added
                Log.d(TAG, "resolveVskDeepLink: mediatype  "+searchFilter.getMediaType());

                searchMeta.put(
                        "metadata.categories", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addValues(searchFilter.getMediaType())
                                .build()
                );
            }

            if(searchFilter.getSeason() != null && !searchFilter.getSeason().isEmpty() ){

                Log.d(TAG, "resolveVskDeepLink: season  "+searchFilter.getSeason());

                //season added
                searchMeta.put(
                        "seasonnumber", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addValues(searchFilter.getSeason())
                                .build()
                );
            }

            if(searchFilter.getEpisode() != null && !searchFilter.getEpisode().isEmpty() ){
                Log.d(TAG, "resolveVskDeepLink: episode  "+searchFilter.getEpisode());

                //episode added
                searchMeta.put(
                        "episodenumber", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addValues(searchFilter.getEpisode())
                                .build()
                );
            }


            if(searchFilter.getActors() != null && !searchFilter.getActors().isEmpty() ){
                //cast added
                Log.d(TAG, "resolveVskDeepLink: actors  "+searchFilter.getActors());

                searchMeta.put(
                        "metadata.cast", CDEServiceOuterClass.FilterKey
                                .newBuilder()
                                .addAllValues(searchFilter.getActors())
                                .build()
                );
            }

            String query  = "";
            if(searchFilter.getTitles() != null && !searchFilter.getTitles().isEmpty()){
                Log.d(TAG, "resolveVskDeepLink: title  "+searchFilter.getTitles());
                query = searchFilter.getTitles().get(0);
            }else if(searchFilter.getCollections() != null && !searchFilter.getCollections().isEmpty()){
                query = String.valueOf(searchFilter.getCollections().get(0));
            }

            CDEServiceOuterClass.SearchQuery searchQuery = CDEServiceOuterClass.SearchQuery.newBuilder()
                    .setQuery(query)
                    .putAllSearchMeta(searchMeta)
                    .build();

            Log.d(TAG, "resolveDeepLink: got uri data ");

            if (query != null) {
                Log.d(TAG, "resolveDeepLink: got query = " + query);
                searchText = query;
                serverCall(searchQuery);
                if(edtSearchView != null)
                    edtSearchView.setText(searchText);

                Log.d(TAG, "resolveDeepLink: got mode = " + cmd);
                if(Objects.equals(cmd, "searchAndPlay")){
                    isSearchNPlay = true;
                }
                isFromDeepLink = true;
            }
        }
    }




    public String getSearchText(){
        return searchText;
    }

    private void init() {
//        managedChannel = ManagedChannelBuilder.forAddress("192.168.0.111", 7771).usePlaintext().build();
        managedChannel = ManagedChannelBuilder.forTarget("node43754-search-service.cloudjiffy.net:11056").usePlaintext().build();
        cdeServiceStub = CDEServiceGrpc.newStub(managedChannel);
        rvKeyboardKeys = findViewById(R.id.rvKeyboardKeys);
        tvSearchText = findViewById(R.id.tvSearchText);
        loadingProgressBar = findViewById(R.id.loadingProgressbar);


        tvSerachMessage = findViewById(R.id.tvSerachMessage);
        edtSearchView = findViewById(R.id.edtSearchView);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
        btnSpace = findViewById(R.id.btnSpace);
        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.searchFragment);
        btnDelete.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnSpace.setOnClickListener(this);

        String[] kayboard_data = {"A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R",
                "S", "T", "U", "V", "W", "X",
                "Y", "Z", "0", "1", "2", "3",
                "4", "5", "6", "7", "8", "9"
        };

        rvKeyboardKeys.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        KeyboardDataAdapter mkeyboardDataAdapter = new KeyboardDataAdapter(this, kayboard_data);
        rvKeyboardKeys.setAdapter(mkeyboardDataAdapter);
        rvKeyboardKeys.requestFocus();

        mkeyboardDataAdapter.setClickListener(new KeyboardDataAdapter.ItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(View view, int position, String mDatum) {
                String s = edtSearchView.getText().toString();
                edtSearchView.setText(s + mDatum);
            }
        });

        edtSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finalCount = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                if (!edtSearchView.getText().toString().isEmpty()) {
                    edtSearchView.setText("");
                }
                searchFragment.clearData();
                break;

            case R.id.btnClear:
                clearTextOneByOne();
                break;

            case R.id.btnSpace:
                edtSearchView.getText().insert(finalCount, " ");
                break;
        }
    }

    private void clearTextOneByOne() {
        int length = edtSearchView.getText().length();
        if (length > 0) {
            edtSearchView.getText().delete(length - 1, length);
        }
    }

    public void searchMe(View view) {
        Intent analyticsIntent = new Intent("tv.cloudwalker.cde.action.SEARCH");
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", searchText);
        bundle.putLong("timeStamp", System.currentTimeMillis());
        analyticsIntent.putExtra("info", bundle);
        sendBroadcast(analyticsIntent);

        CDEServiceOuterClass.SearchQuery searchQuery = CDEServiceOuterClass.SearchQuery.newBuilder()
                .setQuery(searchText)
                .build();

        serverCall(searchQuery);

    }


    private void serverCall(CDEServiceOuterClass.SearchQuery query) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        cdeServiceStub.search(query, new StreamObserver<CDEServiceOuterClass.SearchResponse>() {
            List<CDEServiceOuterClass.ContentDelivery> contents = new ArrayList<>();

            @Override
            public void onNext(CDEServiceOuterClass.SearchResponse value) {
                for(CDEServiceOuterClass.ContentDelivery delivery : value.getContentTilesList()){
                    if(delivery.getPlayList() != null && !delivery.getPlayList().isEmpty()){
                        contents.add(delivery);
                    }
                }
            }

            @Override
            public void onError(final Throwable t) {
                Log.e(TAG, "onError: "+t);
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "onNext: BEFORE ====>>> "+contents.size());
                final List<CDEServiceOuterClass.ContentDelivery> tmp = filterResults(contents);
                Log.d(TAG, "onNext:AFTER ====>>> "+tmp.size());
                loadingProgressBar.setVisibility(View.INVISIBLE);
                searchFragment.refreshData(tmp);

                if(isSearchNPlay && tmp.size() > 0){
                    searchFragment.handleTileClick(tmp.get(0), SearchActivity.this, sourceToPlay);
                }
            }
        });
    }


    protected void logClickEvent(String tileId){
        Log.d(TAG, "logClickEvent: CONTENTID "+tileId);
        cdeServiceStub.cdeClick(CDEServiceOuterClass.TileId.newBuilder().setId(tileId).build(), new StreamObserver<CDEServiceOuterClass.Resp>() {
            @Override
            public void onNext(CDEServiceOuterClass.Resp value) { Log.d(TAG, "onNext: tile clicked "+value.getResult()); }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: tile clicked",t.getCause());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: tile clicked");
            }
        });
    }



    @Override
    protected void onResume() {
        ConnectivityState connectivityState = managedChannel.getState(true);
        Log.d(TAG, "onResume: "+connectivityState.name());
        Intent analyticsIntent = new Intent("tv.cloudwalker.cde.action.OPEN");
        Bundle bundle = new Bundle();
        bundle.putString("packageName", BuildConfig.APPLICATION_ID);
        bundle.putLong("timeStamp", System.currentTimeMillis());
        analyticsIntent.putExtra("info", bundle);
        sendBroadcast(analyticsIntent);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        ConnectivityState connectivityState = managedChannel.getState(true);
        Log.d(TAG, "onPause: "+connectivityState.name());
        managedChannel.enterIdle();
        Log.d(TAG, "onPause: enter idel "+connectivityState.name());
        if (isFromDeepLink){
            onBackPressed();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        managedChannel.shutdownNow();
        super.onDestroy();
    }

    private List<CDEServiceOuterClass.ContentDelivery> filterResults(List<CDEServiceOuterClass.ContentDelivery> contentDeliverys){
        List<CDEServiceOuterClass.ContentDelivery> result = new ArrayList<>();
        for (CDEServiceOuterClass.ContentDelivery cd: contentDeliverys) {

            CDEServiceOuterClass.ContentDelivery.Builder tmp = CDEServiceOuterClass.ContentDelivery.newBuilder();

            //making source list non repeated
            List<CDEServiceOuterClass.PLAY> tmpPlaylist = new ArrayList<>();
            for (CDEServiceOuterClass.PLAY p: cd.getPlayList()) {
                if(tmpPlaylist.size() == 0) {
                    tmpPlaylist.add(p);
                }else{
                    boolean isFound = false;
                    for (CDEServiceOuterClass.PLAY pin : tmpPlaylist) {
                        if (Objects.equals(pin.getSource(), p.getSource())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound)
                        tmpPlaylist.add(p);
                }
            }

            //adding PLAY
            for (CDEServiceOuterClass.PLAY play: tmpPlaylist) {
                if(Arrays.asList(cwCanTrigger).contains(play.getSource().toLowerCase()))
                    tmp.addPlay(play);

            }
            //adding static
            tmp.setContentId(cd.getContentId());
            tmp.setEpisode(cd.getEpisode());
            tmp.setSeason(cd.getSeason());
            tmp.setTitle(cd.getTitle());
            tmp.setType(cd.getType());
            
            //adding media
            tmp.addAllPoster(cd.getPosterList());
            tmp.addAllPortriat(cd.getPortriatList());
            tmp.addAllVideo(cd.getVideoList());

            //final adding to list
            if(tmp.getPlayCount() > 0 ){
                result.add(tmp.build());
            }
        }
        return result;
    }
}

