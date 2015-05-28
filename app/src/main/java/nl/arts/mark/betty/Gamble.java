package nl.arts.mark.betty;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mark on 18/05/15.
 */
public class Gamble implements Serializable {
    private String personName;
    private Date date;

    public Gamble(){
        personName = "";
        date = new Date();
    }

    public String getPersonName(){ return personName; }
    public void setPersonName(String personName){ this.personName = personName;}
    public Date getDate(){ return date; }
    public void setDate(Date date){ this.date = date;}

    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd-mm-yyyy");
        return this.personName + ": " + f.format(this.date);
    }
}
