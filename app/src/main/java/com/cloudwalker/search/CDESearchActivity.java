package com.cloudwalker.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import api.ApiClientCDESearch;
import api.ApiInterface;
import eu.amirs.JSON;
import models.ContentTile;
import models.SearchRequest;
import models.SearchResult;
import retrofit2.Call;
import retrofit2.Callback;
import utils.KeyboardDataAdapter;
import utils.SearchViewDataAdapter;

public class CDESearchActivity extends Activity implements View.OnClickListener {
    private ApiInterface apiInterface;
    private SearchViewDataAdapter searchViewDataAdapter;
    ArrayList<ContentTile> rowItemArrayList;
    RecyclerView rvKeyboardKeys, rvSearchResult;
    int numberOfColumns = 6;
    TextView tvSearchText, tvSerachMessage;
    EditText edtSearchView;
    ProgressBar progressBar_cyclic;
    Button btnDelete, btnSpace, btnClear;
    private int finalCount;
    private String searchText;
    GridLayoutManager manager;
    SearchRequest search;
    private static volatile Method set = null;

    private static final String TAG = "CDESearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri deeplinkUri = getIntent().getData();

        if (deeplinkUri != null) {
            try {
                String data = deeplinkUri.toString();
                data = data.replace("cwsearch://", "");
                byte[] bytedata = Base64.decode(data, Base64.DEFAULT);
                String target = new String(bytedata, StandardCharsets.UTF_8);
                JSON obj = new JSON(target);
                String action = obj.key("action").stringValue();

                try
                {
                    Log.d(TAG, "onCreate: action   ======>    " + obj.key("action").stringValue());
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    Log.d(TAG, "onCreate: data   ======>    " + obj.key("data").stringValue());
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    Log.d(TAG, "onCreate: package   ======>    " + obj.key("package").stringValue());
                }catch (Exception e){
                    e.printStackTrace();
                }


                action = action.toLowerCase();
                switch (action) {
                    case "search": {
                        setContentView(R.layout.activity_cde_search);
                        init();
                        edtSearchView.setText(obj.key("data").stringValue());
                        searchText = obj.key("data").stringValue();
                        try {
                            new GetDataInAsync().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case "play": {
                        searchPlay(obj.key("data").stringValue());
                    }
                    break;
                    case "start": {
                        if (isPackageInstalled(obj.key("package").stringValue(), CDESearchActivity.this.getPackageManager())) {
                            Intent intent = CDESearchActivity.this.getPackageManager().getLeanbackLaunchIntentForPackage(obj.key("package").stringValue());
                            if (intent == null) {
                                intent = CDESearchActivity.this.getPackageManager().getLaunchIntentForPackage(obj.key("package").stringValue());
                            }
                            if (intent != null) {
                                try {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(CDESearchActivity.this, "Requested app Not found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(CDESearchActivity.this, "Requested app Not found.", Toast.LENGTH_SHORT).show();
                            CDESearchActivity.this.finish();
                        }
                    }
                    break;
                    default: {
                        setContentView(R.layout.activity_cde_search);
                        init();
                        try {
                            new GetDataInAsync().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setContentView(R.layout.activity_cde_search);
            init();
            try {
                new GetDataInAsync().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void searchPlay(String data) {
        searchText = data;
        if (!searchText.isEmpty()) {
            progressBar_cyclic.setVisibility(View.VISIBLE);
            rowItemArrayList.clear();

//            String searchUrl;
//            try {
//                searchUrl = "http://" + ipAddress + ":7772/search";
//                Log.d(TAG, "searchPlay: ");
//                search = new SearchRequest();
//                search.setQuery(data);
//                Call<SearchResult> call = apiInterface.getSearchResult(searchUrl, search);
//                call.enqueue(new Callback<SearchResult>() {
//                    @Override
//                    public void onResponse(Call<SearchResult> call, retrofit2.Response<SearchResult> response) {
//                        if (response.isSuccessful()) {
//                            if (response.code() == 200 && response.body() != null) {
//                                try {
//                                    if (!response.body().getContentTile().isEmpty() || response.body().getContentTile() != null &&
//                                            (response.body().getContentTile().size() > 0 && response.body().getContentTile().get(0).getPlay().size() > 0)) {
//                                        String[] deepLinkInfo = {response.body().getContentTile().get(0).getPackageName(), response.body().getContentTile().get(0).getTarget().get(0)};
//                                        handleSearchDeepLink(deepLinkInfo, CDESearchActivity.this);
//                                    } else {
//                                        Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
//                                        CDESearchActivity.this.finish();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
//                                CDESearchActivity.this.finish();
//                            }
//                        } else {
//                            Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
//                            CDESearchActivity.this.finish();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SearchResult> call, Throwable t) {
//                        progressBar_cyclic.setVisibility(View.GONE);
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else {

            try {

                getSearchResultServerCall("");
                rvSearchResult.setVisibility(View.GONE);
                Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //  search handler
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void handleSearchDeepLink(String[] deepLinkInfo, Context context) {
//        Intent intent = new Intent();
//        if (isPackageInstalled(deepLinkInfo[0], context.getPackageManager())) {
//            if (deepLinkInfo[0].contains("youtube")) {
//                intent = YouTubeIntents.createPlayVideoIntentWithOptions(context, deepLinkInfo[1], true, true);
//                intent.setPackage("com.google.android.youtube.tv");
//                context.startActivity(intent);
//            } else {
//                if ((deepLinkInfo.length == 1) || (deepLinkInfo[1] == null) || (deepLinkInfo[1].isEmpty())) {
//                    intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(deepLinkInfo[0]);
//                    if (intent == null) {
//                        intent = context.getPackageManager().getLaunchIntentForPackage(deepLinkInfo[0]);
//                    }
//                    if (intent != null) {
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                    }
//                } else if (deepLinkInfo[0].equalsIgnoreCase("in.startv.hotstar")) {
//                    try {
//                        intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(deepLinkInfo[0]);
//                    } catch (ActivityNotFoundException e) {
//                        intent = context.getPackageManager().getLaunchIntentForPackage(deepLinkInfo[0]);
//                    }
//                    if (intent != null) {
//                        if (deepLinkInfo[1].contains("https")) {
//                            intent.setData(Uri.parse(deepLinkInfo[1].replace("https://www.hotstar.com", "hotstar://content")));
//                        } else {
//                            intent.setData(Uri.parse(deepLinkInfo[1].replace("http://www.hotstar.com", "hotstar://content")));
//                        }
//                        context.startActivity(intent);
//                    }
//                } else {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkInfo[1]));
//                    intent.setPackage(deepLinkInfo[0]);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        context.startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        Toast.makeText(context.getApplicationContext(), "Could not load the application", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        } else {
//            if (isPackageInstalled("com.stark.store", context.getPackageManager())) {
//                String uri = "appstore://appDetail?package=" + deepLinkInfo[0];
//                intent.setData(Uri.parse(uri));
//                intent.setPackage("com.stark.store");
//                intent.setClassName("com.stark.store", "com.stark.store.ui.detail.AppDetailActivity");
//            } else {
//                intent.setData(Uri.parse("market://details?id=" + deepLinkInfo[0]));
//                // find all applications handle our Deeplink
//                final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(intent, 0);
//                for (ResolveInfo otherApp : otherApps) {
//                    ActivityInfo otherAppActivity = otherApp.activityInfo;
//                    ComponentName componentName = new ComponentName(otherAppActivity.applicationInfo.packageName, otherAppActivity.name);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.setComponent(componentName);
//                    break;
//                }
//            }
//            try {
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(context, "App Store not found", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void init() {
        apiInterface = ApiClientCDESearch.getClient(CDESearchActivity.this).create(ApiInterface.class);
        search = new SearchRequest();
        rowItemArrayList = new ArrayList<>();
        rvKeyboardKeys = findViewById(R.id.rvKeyboardKeys);
        rvSearchResult = findViewById(R.id.rvSearchResult);
        tvSearchText = findViewById(R.id.tvSearchText);
        tvSerachMessage = findViewById(R.id.tvSerachMessage);
        edtSearchView = findViewById(R.id.edtSearchView);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
        btnSpace = findViewById(R.id.btnSpace);
        progressBar_cyclic = findViewById(R.id.progressBar_cyclic);

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
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:

                if (!edtSearchView.getText().toString().isEmpty()) {
                    edtSearchView.setText("");

                }


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
        getSearchResultServerCall(searchText);
    }


    @SuppressLint("StaticFieldLeak")
    private class GetDataInAsync extends AsyncTask<Void, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... params) {
            edtSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return actionId == EditorInfo.IME_ACTION_SEARCH;
                }
            });

            edtSearchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchText = s.toString();
                    if (!searchText.isEmpty()) {
                        rowItemArrayList.clear();
                    } else {
                        try {
                            rvSearchResult.setVisibility(View.GONE);
                            Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return null;
        }
    }

    private void getSearchResultServerCall(String query) {
        progressBar_cyclic.setVisibility(View.VISIBLE);
        try {
            search = new SearchRequest();
            search.setQuery(query);
            Call<SearchResult> call = apiInterface.getSearchResult(search);

            call.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, retrofit2.Response<SearchResult> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: "+response.code());
                        if (response.code() == 200 && response.body() != null) {
                            progressBar_cyclic.setVisibility(View.GONE);

                            try {
                                if (!response.body().getContentTile().isEmpty() || response.body().getContentTile() != null) {
                                    Log.d(TAG, "onResponse: "+response.code());
                                    progressBar_cyclic.setVisibility(View.GONE);

                                    rowItemArrayList.clear();

                                    rvSearchResult.setVisibility(View.VISIBLE);
                                    manager = new GridLayoutManager(CDESearchActivity.this, 4, GridLayoutManager.VERTICAL, false);
                                    rvSearchResult.setLayoutManager(manager);

                                    Log.d(TAG, "onResponse: setting manager.");

                                    for (int i = 0; i < response.body().getContentTile().size(); i++) {
                                        rowItemArrayList.add(i, response.body().getContentTile().get(i));
                                    }

                                    searchViewDataAdapter = new SearchViewDataAdapter(CDESearchActivity.this, rowItemArrayList);
                                    rvSearchResult.setAdapter(searchViewDataAdapter);
                                    searchViewDataAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(CDESearchActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                                    progressBar_cyclic.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressBar_cyclic.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar_cyclic.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Log.e(TAG, "onFailure: ",t);
                    progressBar_cyclic.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getSystemProperty(String key) {
        String value = null;
        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    public static void setSystemProperty(String prop, String value) {
        try {
            if (null == set) {
                synchronized (CDESearchActivity.class) {
                    if (null == set) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        set = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
                    }
                }
            }
            set.invoke(null, new Object[]{prop, value});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
