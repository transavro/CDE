package com.cloudwalker.search;

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

import CDEService.CDEServiceGrpc;
import CDEService.CDEServiceOuterClass;
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
    private String searchText;
    private static final String TAG = "SearchActivity";
    CDEServiceGrpc.CDEServiceStub cdeServiceStub;
    private SearchFragment searchFragment;
    private ManagedChannel managedChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        managedChannel = ManagedChannelBuilder.forAddress("192.168.0.106", 7771).usePlaintext().build();
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
        serverCall(searchText);
    }


    private void serverCall(String query) {
        cdeServiceStub.searchStream(CDEServiceOuterClass.SearchQuery.newBuilder().setQuery(query).build(), new StreamObserver<CDEServiceOuterClass.Content>() {
            ArrayList<CDEServiceOuterClass.Content> contents = new ArrayList<>();
            @Override
            public void onNext(final CDEServiceOuterClass.Content value) {
              contents.add(value);
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
            }
        });
    }



    @Override
    protected void onResume() {
        ConnectivityState connectivityState = managedChannel.getState(true);
        Log.d(TAG, "onResume: "+connectivityState.name());
        super.onResume();
    }

    @Override
    protected void onPause() {
        ConnectivityState connectivityState = managedChannel.getState(true);
        Log.d(TAG, "onPause: "+connectivityState.name());
        managedChannel.enterIdle();
        Log.d(TAG, "onPause: enter idel "+connectivityState.name());
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        managedChannel.shutdown();
        super.onDestroy();
    }

    //
//    @SuppressLint("StaticFieldLeak")
//    private class GetDataInAsync extends AsyncTask<Void, Void, String> {
//        @SuppressLint("WrongThread")
//        @Override
//        protected String doInBackground(Void... params) {
//            edtSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    return actionId == EditorInfo.IME_ACTION_SEARCH;
//                }
//            });
//
//            edtSearchView.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    searchText = s.toString();
//                    if (!searchText.isEmpty()) {
//                        rowItemArrayList.clear();
//                    } else {
//                        try {
//                            rvSearchResult.setVisibility(View.GONE);
//                            Toast.makeText(SearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            return null;
//        }
//    }
}
