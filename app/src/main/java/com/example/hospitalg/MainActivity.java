package com.example.hospitalg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    ConnDB conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conn = new ConnDB(this);

        SQLiteDatabase db = conn.getWritableDatabase();

        Cursor c = db.query("users", null, null, null, null, null, null);
        if(c.moveToFirst()){

            int idColIndex = c.getColumnIndex("id");
            int loginColIndex = c.getColumnIndex("login");
            int passColIndex = c.getColumnIndex("password");

            do{
                Log.d("Users data", "ID = " + c.getInt(idColIndex) +
                        ", login = " + c.getString(loginColIndex) +
                        ", password = " + c.getString(passColIndex));
            } while (c.moveToNext());
//
//            ContentValues cv = new ContentValues();
//            cv.put("fio", "Полозов Николай Владимирович");
//            cv.put("proffession", "Хирург");
//            long rowID = db.insert("doctors", null, cv);
//            Log.d("Message", "row inserted in doctor table, ID = " + rowID);
        }
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
                AlertDialog("Регистрация", "Юзер с таким же логином уже находится в бд!!");
                return;
            }

            Log.d("Message", "Insert in table users");

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


    String Date;
    int UserId;
    int DoctorId;
    private List<Button> buttonList = new ArrayList<Button>();


    public void authorization(View view){
        String login = ((EditText) findViewById(R.id.editTextUsernameAuth)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPasswordAuth)).getText().toString();

        SQLiteDatabase db = conn.getWritableDatabase();

        Cursor c = db.query("users", null, "login = " + login + " and password = " + password, null, null, null, null);
        if(c.moveToFirst()){

            int idUserIndex = c.getColumnIndex("id");
            UserId = c.getInt(idUserIndex);
            setContentView(R.layout.doctors);

            Cursor c2 = db.query("doctors", null, null, null, null, null, null);
            if(c2.moveToFirst()){
                int idColIndex = c2.getColumnIndex("id");
                int fioColIndex = c2.getColumnIndex("fio");
                int proffessionColIndex = c2.getColumnIndex("proffession");

                RelativeLayout rlMain = (RelativeLayout) findViewById(R.id.mainLayout);
                int mtop = 0;
                do{
                    RelativeLayout rl = new RelativeLayout(this);
                    LinearLayout.LayoutParams rlParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    rlParams.setMargins(0, mtop, 0, 16);
                    rl.setLayoutParams(rlParams);
                    rl.setBackground(getDrawable(R.color.gray));

                    TextView tv = new TextView(this);
                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(tvParams);
                    tv.setText(c2.getString(fioColIndex) + ", " + c2.getString(proffessionColIndex));
                    tv.setTextSize(24);

                    Button bt = new Button(this);
                    bt.setId(c2.getInt(idColIndex));
                    LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    btParams.setMargins(0, 150, 0, 0);
                    bt.setLayoutParams(btParams);
                    bt.setText("Записаться");
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog();
                            DoctorId = v.getId();  //  Получаем id (индекс в списке)
//                            Button button = buttonList.get(position);  //  Получаем кнопку
//                            DoctorId = button.getId();
                        }
                    });

                    buttonList.add(bt);  //  Добавляем в список

                    rl.addView(tv);
                    rl.addView(bt);


                    rlMain.addView(rl);

                    mtop += 300;

                } while (c2.moveToNext());
            }
            else AlertDialog("Врачи", "Врачи отсутствуют");

        }
        else AlertDialog("Авторизация", "Данного юзера не существует!");
    }

    public void addRecord(View v){
        if(UserId != 0 && DoctorId != 0 && !Date.equals("")){

            SQLiteDatabase db = conn.getWritableDatabase();
            ContentValues cv = new ContentValues();

            Cursor c2 = db.query("doctors", null, "id = " + DoctorId, null, null, null, null);
            if(c2.moveToFirst()){
                int idColIndex = c2.getColumnIndex("id");
                int fioColIndex = c2.getColumnIndex("fio");
                int proffessionColIndex = c2.getColumnIndex("proffession");

                do{
                    cv.put("fio", c2.getString(fioColIndex));
                    cv.put("proffession", c2.getString(proffessionColIndex));
                    cv.put("userId", UserId);
                    cv.put("date", Date);
                } while (c2.moveToNext());


                int updCount = db.update("doctors", cv, "id = ?",
                        new String[] { getString(DoctorId) });
                Log.d("LOG_TAG", "updated rows count = " + updCount);

                setContentView(R.layout.profile);


            }
        }
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Handle date selection
                    String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    // You can do something with the selected date, such as displaying it in a TextView
                    // For demonstration, I'll just print the selected date
                    Log.d("Date", "Selected date: " + selectedDate);
                    Date = selectedDate;
                },
                year,
                month,
                dayOfMonth);

        // Show the date picker dialog
        datePickerDialog.show();
    }


}