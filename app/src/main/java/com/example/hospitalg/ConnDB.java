package com.example.hospitalg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnDB extends SQLiteOpenHelper {

    public ConnDB(Context context){
        super(context, "hospitalDB", null, 1);
        //context.deleteDatabase("hospitalDB");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(" +
                "id integer primary key autoincrement," +
                "login text," +
                "password text)");

        db.execSQL("create table doctors(" +
                "id integer primary key autoincrement," +
                "fio text," +
                "proffession text," +
                "date text," +
                "userId INTEGER," +
                "FOREIGN KEY(userId) REFERENCES users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    private static final String LOG = "DEBUG";
//    private static String ip = "127.0.0.1";
//    //private static String ip = "HOME-PC\\Николай";
//    private static String port = "1433";
//    private static String classs = "net.sourceforge.jtds.jdbc.Driver";
//    private static String db = "hospitalDB";
//    private static String un = "user";
//    private static String password = "user";
//    public static Connection connection() {
//        Connection conn = null;
//        String ConnURL = null;
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        try {
//            Class.forName(classs);
//            ConnURL = "jdbc:jtds:sqlserver://" + ip +":"+port+";"
//                    + "databaseName=" + db + ";user=" + un + ";password="
//                    + password + ";";
//            //ConnURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName" + db + ";integratedSecurity=true";
//            conn = DriverManager.getConnection(ConnURL);
//        } catch (SQLException e) {
//            Log.d(LOG, e.getMessage());
//        } catch (ClassNotFoundException e) {
//            Log.d(LOG, e.getMessage());
//        }
//        return conn;
//    }



}
