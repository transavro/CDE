package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchRequest
{
  /*  @SerializedName("appId")
    @Expose
    private String appId;

    @SerializedName("cde")
    @Expose
    private String cde;*/

    @SerializedName("query")
    @Expose
    private String query;


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}

