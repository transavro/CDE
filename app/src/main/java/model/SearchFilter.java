
package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter implements Parcelable
{

    @SerializedName("titles")
    @Expose
    private List<String> titles = new ArrayList<String>();
    @SerializedName("collections")
    @Expose
    private List<Object> collections = new ArrayList<Object>();
    @SerializedName("genres")
    @Expose
    private List<String> genres = new ArrayList<String>();
    @SerializedName("actors")
    @Expose
    private List<String> actors = new ArrayList<String>();
    @SerializedName("roles")
    @Expose
    private List<String> roles = new ArrayList<String>();
    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("season")
    @Expose
    private String season;
    @SerializedName("episode")
    @Expose
    private String episode;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("imdbID")
    @Expose
    private List<String> imdbID = new ArrayList<String>();
    public final static Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        public SearchFilter[] newArray(int size) {
            return (new SearchFilter[size]);
        }

    }
    ;

    protected SearchFilter(Parcel in) {
        in.readList(this.titles, (String.class.getClassLoader()));
        in.readList(this.collections, (Object.class.getClassLoader()));
        in.readList(this.genres, (String.class.getClassLoader()));
        in.readList(this.actors, (String.class.getClassLoader()));
        in.readList(this.roles, (String.class.getClassLoader()));
        this.mediaType = ((String) in.readValue((String.class.getClassLoader())));
        this.season = ((String) in.readValue((String.class.getClassLoader())));
        this.episode = ((String) in.readValue((String.class.getClassLoader())));
        this.appName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.imdbID, (String.class.getClassLoader()));
    }

    public SearchFilter() {}

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public SearchFilter withTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public List<Object> getCollections() {
        return collections;
    }

    public void setCollections(List<Object> collections) {
        this.collections = collections;
    }

    public SearchFilter withCollections(List<Object> collections) {
        this.collections = collections;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public SearchFilter withGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public SearchFilter withActors(List<String> actors) {
        this.actors = actors;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public SearchFilter withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public SearchFilter withMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public SearchFilter withSeason(String season) {
        this.season = season;
        return this;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public SearchFilter withEpisode(String episode) {
        this.episode = episode;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public SearchFilter withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public List<String> getImdbID() {
        return imdbID;
    }

    public void setImdbID(List<String> imdbID) {
        this.imdbID = imdbID;
    }

    public SearchFilter withImdbID(List<String> imdbID) {
        this.imdbID = imdbID;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(titles);
        dest.writeList(collections);
        dest.writeList(genres);
        dest.writeList(actors);
        dest.writeList(roles);
        dest.writeValue(mediaType);
        dest.writeValue(season);
        dest.writeValue(episode);
        dest.writeValue(appName);
        dest.writeList(imdbID);
    }

    public int describeContents() {
        return  0;
    }

}
