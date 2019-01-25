package lab.ourteam.newlab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class localDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "user";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myTest.db";
    public localDBHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqlitedatabase){
        String sql="create table if not exists "+TABLE_NAME+"(" +
                "userId integer primary key," +
                "userName text )";
        sqlitedatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}
