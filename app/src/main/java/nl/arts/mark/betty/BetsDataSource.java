package nl.arts.mark.betty;

/**
 * Created by mark on 18/05/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.SerializableEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BetsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_DATE,
            MySQLiteHelper.COLUMN_GAMBLES,
            MySQLiteHelper.COLUMN_WON,
            MySQLiteHelper.COLUMN_WINNER
    };

    public BetsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Bet createBet(String name, Date date, ArrayList<Gamble> gambles) {
        ContentValues values = new ContentValues();

        Gson g = new Gson();

        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_DATE, date.toString());
        values.put(MySQLiteHelper.COLUMN_GAMBLES, g.toJson(gambles));
        values.put(MySQLiteHelper.COLUMN_WINNER, "");
        values.put(MySQLiteHelper.COLUMN_WON, false);

        long insertId = database.insert(MySQLiteHelper.TABLE_BETS, null,
                values);

        return getBet(insertId);
    }

    public void deleteBet(Bet bet) {
        long id = bet.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_BETS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public Bet getBet(long id){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BETS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Bet bet = cursorToBet(cursor);
        cursor.close();
        return bet;
    }

    public void saveBet(Bet b){
        ContentValues values = new ContentValues();

        Gson g = new Gson();
        values.put(MySQLiteHelper.COLUMN_NAME, b.getName());
        values.put(MySQLiteHelper.COLUMN_DATE, b.getDate().toString());
        values.put(MySQLiteHelper.COLUMN_GAMBLES, g.toJson(b.getGambles()));
        values.put(MySQLiteHelper.COLUMN_WON, b.getWon());
        values.put(MySQLiteHelper.COLUMN_WINNER, b.getWinner());

        database.update(MySQLiteHelper.TABLE_BETS, values, MySQLiteHelper.COLUMN_ID + " = "+b.getId(), null);
    }

    public List<Bet> getAllBets() {
        List<Bet> bets = new ArrayList<Bet>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BETS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Bet bet = cursorToBet(cursor);
            bets.add(bet);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return bets;
    }

    private Bet cursorToBet(Cursor cursor) {
        Bet bet = new Bet();
        bet.setId(cursor.getLong(0));
        bet.setName(cursor.getString(1));
        bet.setDate(cursor.getString(2));

        ArrayList<Gamble> gambles = new Gson().fromJson(cursor.getString(3), new TypeToken<ArrayList<Gamble>>() {
        }.getType());
        bet.setGambles(gambles);

        bet.setWon(cursor.getInt(4)>0);
        bet.setWinner(cursor.getString(5));

        return bet;
    }
}

