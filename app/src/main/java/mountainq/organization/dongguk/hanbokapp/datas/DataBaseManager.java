package mountainq.organization.dongguk.hanbokapp.datas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import static mountainq.organization.dongguk.hanbokapp.datas.DataBaseManager.DataEntry.TABLE_NAME;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String TAG = "SQL_test";
    public static final String DB_NAME = "Hanbok.db";
    public static final int DB_VERSION = 5;

    public static class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "bookmark";
        public static final String _ID = "_id";
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
                    DataEntry.COLUMN_LOCATION_NAME + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_LON + TEXT_TYPE + COMMA_SEP +
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

    public boolean insert(String primeKey, String locationName, String lon, String lat) {
        Log.d(TAG, "insert primeKey : " + primeKey + "  locationName : " + locationName + "  lon : " + lon + "  lat : " + lat);
        if (find(primeKey) != null) return false;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_PRIMEKEY, primeKey);
        values.put(DataEntry.COLUMN_LOCATION_NAME, locationName);
        values.put(DataEntry.COLUMN_LON, lon);
        values.put(DataEntry.COLUMN_LAT, lat);
        long newRowId = db.insert(DataEntry.TABLE_NAME, null, values);
        Log.d(TAG, "inserted item ===> newRowId :" + newRowId);
        db.close();
//        db.execSQL("INSERT INTO "+TABLE_NAME+" values(null, '"+primeKey+"', '"+locationName+"');");
//        db.close();
        return true;
    }

    public void delete(String primeKey) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = DataEntry.COLUMN_PRIMEKEY + " LIKE ?";
        String[] selectionArgs = {primeKey};
        db.delete(DataEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
//        db.execSQL("");
//        db.close();
    }

    public BookMark find(String primeKey) {
        BookMark item = null;
        Log.d(TAG, "find about primKey : " + primeKey);
        SQLiteDatabase db = SQLiteDatabase.create(null);
        try {
            db = getReadableDatabase();
        } catch (NullPointerException e) {
            Log.e(TAG, "find err : there is no readable Database || message : " + e.getMessage());
            onCreate(db);
            return null;
        }

        String[] projection = {
                DataEntry._ID,
                DataEntry.COLUMN_PRIMEKEY,
                DataEntry.COLUMN_LOCATION_NAME,
                DataEntry.COLUMN_LON,
                DataEntry.COLUMN_LAT
        };

        String selection = DataEntry.COLUMN_PRIMEKEY + " = ?";
        String[] selectionArgs = {primeKey};

        try {
            Cursor c = db.query(DataEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            c.moveToFirst();
            item = new BookMark(c.getInt(1), c.getString(2), c.getDouble(3), c.getDouble(4));
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Can not get exist item err message : " + e.getMessage());
        }

        db.close();

        return item;
    }

    public ArrayList<BookMark> printList() {
        ArrayList<BookMark> items = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.create(null);
        try {
            db = getReadableDatabase();
        } catch (NullPointerException e) {
            Log.e(TAG, "print err message : " + e.getMessage());
            onCreate(db);
            return items;
        }

        String[] projection = {
                DataEntry._ID,
                DataEntry.COLUMN_PRIMEKEY,
                DataEntry.COLUMN_LOCATION_NAME,
                DataEntry.COLUMN_LON,
                DataEntry.COLUMN_LAT
        };
        try {
            Cursor c = db.query(DataEntry.TABLE_NAME, projection, null, null, null, null, null);
            c.moveToFirst();

            //인덱스가 하나밖에 없는 경우
            BookMark item = new BookMark(c.getInt(1), c.getString(2), c.getDouble(3), c.getDouble(4));
            items.add(item);

            //이외에 다수인 경우 반복문
            while (c.moveToNext()) {
                item = new BookMark(c.getInt(1), c.getString(2), c.getDouble(3), c.getDouble(4));
                items.add(item);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        Log.d(TAG, "print ArrayList : " + items.toString());
//        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
//        while(cursor.moveToNext()){
//            BookMark item = new BookMark(cursor.getInt(1), cursor.getString(2));
//            items.add(item);
//        }
//        cursor.close();
        return items;
    }
}
