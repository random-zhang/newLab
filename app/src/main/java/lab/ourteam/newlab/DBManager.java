package lab.ourteam.newlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
   操作数据库
 */
public class DBManager {
   private localDBHelper helper;
   private SQLiteDatabase db;
   public DBManager(Context context){
       helper=new localDBHelper(context);
       db=helper.getWritableDatabase();
   }
    public void setUserName(String userName){
        ContentValues cv=new ContentValues();
        cv.put("userName",userName);
        String sql;
        if(!getUserName().equals(" ")) //是否空
             if (getUserName().equals(userName))//判断userName是否相等
                 return;
             else
                 db.update("user",cv,null,null);
        else {
             sql = String.format("insert into user (userName) values ('%s')", userName);
             db.execSQL(sql);
        }
    }
    public String getUserName(){
        String[] strs={"userName"};
        Cursor cursor= db.query("user",strs,null,null,null,null,null);
        String result=" ";
        while(cursor.moveToNext()){
            result=cursor.getString(cursor.getColumnIndex("userName"));
        }
        return result;
    }
    public  void closeDB(){
       db.close();
    }
}
