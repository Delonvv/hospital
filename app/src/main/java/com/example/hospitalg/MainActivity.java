package com.example.hospitalg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    ConnDB conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conn = new ConnDB(this);
    }

    @SuppressLint("Range")
    public void registration(View view){

        String login = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        String passConf = ((EditText) findViewById(R.id.editTextConfirmPassword)).getText().toString();

        if(password.equals(passConf)){
            SQLiteDatabase db = conn.getWritableDatabase();

            Cursor c = db.query("users", null, "login =" + login, null, null, null, null);

            if(c.moveToFirst()){

//                int idColIndex = c.getColumnIndex("id");
//                int loginColIndex = c.getColumnIndex("login");
//                int passColIndex = c.getColumnIndex("password");
//
//                do{
//                    Log.d("Message", "ID = " + c.getInt(idColIndex) +
//                            ", login = " + c.getString(loginColIndex) +
//                            ", password = " + c.getString(passColIndex));
//                } while (c.moveToNext());
                AlertDialog("Регистрация", "Юзер с таким же логином уже находится в бд!!");
                return;
            }

            Log.d("Message", "Insert in table");

            ContentValues cv = new ContentValues();
            cv.put("login", login);
            cv.put("password", password);
            long rowID = db.insert("users", null, cv);
            Log.d("Message", "row inserted, ID = " + rowID);
            AlertDialog("Регистрация", "Вы были зарегистрированы!");
            return;
        }
        AlertDialog("Регистрация", "Пароли не совпадают!");
    }

    public void AlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onAuthorization(View view){
        setContentView(R.layout.login);
    }


}