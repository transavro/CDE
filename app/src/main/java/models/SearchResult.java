
package models;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult implements Serializable, Parcelable
{

    @SerializedName("contentTile")
    @Expose
    private List<ContentTile> contentTile = null;
    public final static Creator<SearchResult> CREATOR = new Creator<SearchResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        public SearchResult[] newArray(int size) {
            return (new SearchResult[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4778806582055330933L;

    protected SearchResult(Parcel in) {
        in.readList(this.contentTile, (ContentTile.class.getClassLoader()));
    }

    public SearchResult() {
    }

    public List<ContentTile> getContentTile() {
        return contentTile;
    }

    public void setContentTile(List<ContentTile> contentTile) {
        this.contentTile = contentTile;
    }

    public SearchResult withContentTile(List<ContentTile> contentTile) {
        this.contentTile = contentTile;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(contentTile);
    }

    public int describeContents() {
        return  0;
    }

}
