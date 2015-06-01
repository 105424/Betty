package nl.arts.mark.betty;

/**
 * Created by mark on 18/05/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_BETS = "bets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GAMBLES = "gambles";
    public static final String COLUMN_WON = "won";
    public static final String COLUMN_WINNER = "winner";
    public static final String COLUMN_LASTWINNER = "lastWinner";

    private static final String DATABASE_NAME = "betty.db";
    private static final int DATABASE_VERSION = 11;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BETS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DATE + " text, "
            + COLUMN_GAMBLES + " text, "
            + COLUMN_WON + " BOOLEAN, "
            + COLUMN_WINNER + " text, "
            + COLUMN_LASTWINNER + " text "
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BETS);
        onCreate(db);
    }



}


