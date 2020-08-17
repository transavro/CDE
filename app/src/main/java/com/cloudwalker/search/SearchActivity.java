package com.cloudwalker.search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cloudwalker.CDEServiceGrpc;
import cloudwalker.CDEServiceOuterClass;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import utils.KeyboardDataAdapter;

public class SearchActivity extends FragmentActivity implements View.OnClickListener {

    RecyclerView rvKeyboardKeys;
    int numberOfColumns = 6;
    TextView tvSearchText, tvSerachMessage;
    EditText edtSearchView;
    Button btnDelete, btnSpace, btnClear;
    private int finalCount;
    private String searchText = "";
    private static final String TAG = "SearchActivity";
    CDEServiceGrpc.CDEServiceStub cdeServiceStub;
    private SearchFragment searchFragment;
    private ManagedChannel managedChannel;
    private boolean isSearchNPlay = false;
    private boolean isFromDeepLink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        resolveDeepLink();
    }

    private void resolveDeepLink() {
        Log.d(TAG, "resolveDeepLink: ");
        Uri data = getIntent().getData();
        if (data != null) {

            Log.d(TAG, "resolveDeepLink: got uri data ");

            String query = data.getQueryParameter("query");

            if (query != null) {
                Log.d(TAG, "resolveDeepLink: got query = " + query);
                searchText = query;
                serverCall(query);
                if(edtSearchView != null)
                    edtSearchView.setText(searchText);

                String mode = data.getQueryParameter("mode");
                Log.d(TAG, "resolveDeepLink: got mode = " + mode);
                if(Objects.equals(mode, "SearchPlay")){
                    isSearchNPlay = true;
                }
                String name = data.getQueryParameter("name");
                Log.d(TAG, "resolveDeepLink: got app Name = " + name);

                isFromDeepLink = true;
            }
        }
    }

    public String getSearchText(){
        return searchText;
    }

    private void init() {
//        managedChannel = ManagedChannelBuilder.forAddress("192.168.0.106", 7771).usePlaintext().build();
        managedChannel = ManagedChannelBuilder.forTarget("node43754-search-service.cloudjiffy.net:11056").usePlaintext().build();
        cdeServiceStub = CDEServiceGrpc.newStub(managedChannel);
        rvKeyboardKeys = findViewById(R.id.rvKeyboardKeys);
        tvSearchText = findViewById(R.id.tvSearchText);

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
        serverCall(searchText);
    }


    private void serverCall(String query) {
        cdeServiceStub.search(CDEServiceOuterClass.SearchQuery.newBuilder().setQuery(query).build(), new StreamObserver<CDEServiceOuterClass.SearchResponse>() {
            List<CDEServiceOuterClass.Optimus> contents = new ArrayList<>();

            @Override
            public void onNext(CDEServiceOuterClass.SearchResponse value) {
                contents = value.getContentTileList();
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
                searchFragment.refreshData(contents);
                if(isSearchNPlay){
                    for(CDEServiceOuterClass.Optimus c : contents){
                        if(c.getMetadata().getTitle().toLowerCase().equals(searchText.toLowerCase())){
//                            searchFragment.handleTileClick(c, SearchActivity.this);
                            break;
                        }
                    }
                }
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
        ConnectivityState connectivityState = managedChannel.getState(true);
        Log.d(TAG, "onPause: "+connectivityState.name());
        managedChannel.enterIdle();
        Log.d(TAG, "onPause: enter idel "+connectivityState.name());
        super.onPause();
        if (isFromDeepLink){
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        managedChannel.shutdownNow();
        super.onDestroy();
    }
}

