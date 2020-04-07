
package models;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Play implements Serializable, Parcelable
{

    @SerializedName("monetize")
    @Expose
    private Integer monetize;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<Play> CREATOR = new Creator<Play>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Play createFromParcel(Parcel in) {
            return new Play(in);
        }

        public Play[] newArray(int size) {
            return (new Play[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7976136319731856957L;

    protected Play(Parcel in) {
        this.monetize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.target = ((String) in.readValue((String.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this._package = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Play() {
    }

    public Integer getMonetize() {
        return monetize;
    }

    public void setMonetize(Integer monetize) {
        this.monetize = monetize;
    }

    public Play withMonetize(Integer monetize) {
        this.monetize = monetize;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Play withTarget(String target) {
        this.target = target;
        return this;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Play withSource(String source) {
        this.source = source;
        return this;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public Play withPackage(String _package) {
        this._package = _package;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Play withType(String type) {
        this.type = type;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(monetize);
        dest.writeValue(target);
        dest.writeValue(source);
        dest.writeValue(_package);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
