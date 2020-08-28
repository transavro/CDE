
package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VskModel implements Parcelable
{

    @SerializedName("commandType")
    @Expose
    private String commandType;
    @SerializedName("searchFilter")
    @Expose
    private SearchFilter searchFilter;
    public final static Creator<VskModel> CREATOR = new Creator<VskModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public VskModel createFromParcel(Parcel in) {
            return new VskModel(in);
        }

        public VskModel[] newArray(int size) {
            return (new VskModel[size]);
        }

    }
    ;

    protected VskModel(Parcel in) {
        this.commandType = ((String) in.readValue((String.class.getClassLoader())));
        this.searchFilter = ((SearchFilter) in.readValue((SearchFilter.class.getClassLoader())));
    }

    public VskModel() {
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public VskModel withCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    public VskModel withSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(commandType);
        dest.writeValue(searchFilter);
    }

    public int describeContents() {
        return  0;
    }

}
