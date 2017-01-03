package mountainq.organization.dongguk.hanbokapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import static mountainq.organization.dongguk.hanbokapp.datas.DataBaseManager.DataEntry.TABLE_NAME;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class DataBaseManager extends SQLiteOpenHelper{

    public static final String DB_NAME = "Hanbok.db";
    public static final int DB_VERSION = 1;

    public  static class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "bookmark";
        public static final String _ID = "pk";
        public static final String COLUMN_PRIMEKEY = "primeKey";
        public static final String COLUMN_LOCATION_NAME = "locationName";
        public static final String COLUMN_LON = "longitude";
        public static final String COLUMN_LAT = "latitude";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_BOOKMARK =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    DataEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, " +
                    DataEntry.COLUMN_PRIMEKEY + INTEGER_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_LOCATION_NAME + TEXT_TYPE +
                    DataEntry.COLUMN_LON + TEXT_TYPE +
                    DataEntry.COLUMN_LAT + TEXT_TYPE + " )";
    private static final String SQL_DELETE_BOOKMARK =
            "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME;


    public DataBaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BOOKMARK);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(String primeKey, String locationName, String lon, String lat){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_PRIMEKEY, primeKey);
        values.put(DataEntry.COLUMN_LOCATION_NAME, locationName);
        values.put(DataEntry.COLUMN_LON, lon);
        values.put(DataEntry.COLUMN_LAT, lat);

        long newRowId = db.insert(DataEntry.TABLE_NAME, null, values);
//        db.execSQL("INSERT INTO "+TABLE_NAME+" values(null, '"+primeKey+"', '"+locationName+"');");
//        db.close();
    }

    public void delete(String primeKey){
        SQLiteDatabase db = getWritableDatabase();
        String selection = DataEntry.COLUMN_PRIMEKEY + " LIKE ?";
        String[] selectionArgs = { primeKey };
        db.delete(DataEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
//        db.execSQL("");
//        db.close();
    }

    public ArrayList<BookMark> printList(){
        ArrayList<BookMark> items = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.create(null);
        try {
            db = getReadableDatabase();
        }catch (NullPointerException e){
            e.printStackTrace();
            onCreate(db);
        }

        String[] projection = {
                DataEntry._ID,
                DataEntry.COLUMN_PRIMEKEY,
                DataEntry.COLUMN_LOCATION_NAME
        };

        Cursor c = db.query(DataEntry.TABLE_NAME, projection, null, null, null, null, null);
        c.moveToFirst();
        while(c.moveToNext()){
            BookMark item = new BookMark(c.getInt(1), c.getString(2), c.getDouble(3), c.getDouble(4));
            items.add(item);
        }
        c.close();
//        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
//        while(cursor.moveToNext()){
//            BookMark item = new BookMark(cursor.getInt(1), cursor.getString(2));
//            items.add(item);
//        }
//        cursor.close();
        return items;
    }
}
