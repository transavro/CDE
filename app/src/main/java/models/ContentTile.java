
package models;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentTile implements Serializable, Parcelable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster")
    @Expose
    private List<String> poster = null;
    @SerializedName("portriat")
    @Expose
    private List<String> portriat = null;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("isDetailPage")
    @Expose
    private Boolean isDetailPage;
    @SerializedName("contentId")
    @Expose
    private String contentId;
    @SerializedName("play")
    @Expose
    private List<Play> play = null;
    @SerializedName("video")
    @Expose
    private List<String> video = null;
    public final static Creator<ContentTile> CREATOR = new Creator<ContentTile>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ContentTile createFromParcel(Parcel in) {
            return new ContentTile(in);
        }

        public ContentTile[] newArray(int size) {
            return (new ContentTile[size]);
        }

    }
    ;
    private final static long serialVersionUID = 38736229470297581L;

    protected ContentTile(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.poster, (String.class.getClassLoader()));
        in.readList(this.portriat, (String.class.getClassLoader()));
        this.type = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isDetailPage = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.contentId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.play, (Play.class.getClassLoader()));
        in.readList(this.video, (String.class.getClassLoader()));
    }

    public ContentTile() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContentTile withTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getPoster() {
        return poster;
    }

    public void setPoster(List<String> poster) {
        this.poster = poster;
    }

    public ContentTile withPoster(List<String> poster) {
        this.poster = poster;
        return this;
    }

    public List<String> getPortriat() {
        return portriat;
    }

    public void setPortriat(List<String> portriat) {
        this.portriat = portriat;
    }

    public ContentTile withPortriat(List<String> portriat) {
        this.portriat = portriat;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ContentTile withType(Integer type) {
        this.type = type;
        return this;
    }

    public Boolean getIsDetailPage() {
        return isDetailPage;
    }

    public void setIsDetailPage(Boolean isDetailPage) {
        this.isDetailPage = isDetailPage;
    }

    public ContentTile withIsDetailPage(Boolean isDetailPage) {
        this.isDetailPage = isDetailPage;
        return this;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ContentTile withContentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public List<Play> getPlay() {
        return play;
    }

    public void setPlay(List<Play> play) {
        this.play = play;
    }

    public ContentTile withPlay(List<Play> play) {
        this.play = play;
        return this;
    }

    public List<String> getVideo() {
        return video;
    }

    public void setVideo(List<String> video) {
        this.video = video;
    }

    public ContentTile withVideo(List<String> video) {
        this.video = video;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeList(poster);
        dest.writeList(portriat);
        dest.writeValue(type);
        dest.writeValue(isDetailPage);
        dest.writeValue(contentId);
        dest.writeList(play);
        dest.writeList(video);
    }

    public int describeContents() {
        return  0;
    }

}
