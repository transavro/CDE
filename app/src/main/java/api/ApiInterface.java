package api;

import models.SearchRequest;
import models.SearchResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/search")
    Call<SearchResult> getSearchResult(@Body SearchRequest search);
}

