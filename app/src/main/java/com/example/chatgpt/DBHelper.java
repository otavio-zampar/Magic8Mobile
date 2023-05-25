package com.example.chatgpt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "GPTMobile.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER primary key AUTOINCREMENT, username TEXT NOT NULL, email TEXT unique NOT NULL, senha TEXT NOT NULL, image BLOB)");
        MyDB.execSQL("create Table conversas(id INTEGER primary key AUTOINCREMENT, user INTEGER NOT NULL, nome TEXT NOT NULL, conversa TEXT NOT NULL)");
        initTableUser(MyDB);
    }

    public void initTableUser(SQLiteDatabase MyDB){
        ContentValues CV = new ContentValues();
        CV.put("id", 0);
        CV.put("username", "NONO");
        CV.put("email", "admin");
        CV.put("senha", "123");
        MyDB.insert("users", null, CV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists conversas");

    }

    public boolean insertData(String username, String email, String senha){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("senha", senha);

        long result = MyDB.insert("users", null, contentValues);

        return result != -1;

    }

    public boolean insertConversas(int user, String nome, String conversa){ // user nome conversa date
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("nome", nome);
        contentValues.put("conversa", conversa);

        long result = MyDB.insert("conversas", null, contentValues);
        return result != -1;
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @SuppressLint("Range")
    public String getUsername(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select username from users where email = ?", new String[]{email});
        String username = "Nao achou o username";

        cursor.moveToFirst();
        if (cursor.getColumnIndex("username") != -1) {
            username = cursor.getString(cursor.getColumnIndex("username"));
        }
        return username;
    }

    @SuppressLint("Range")
    public int getID(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select id from users where email = ?", new String[]{email});
        int id = -1;
        cursor.moveToFirst();
        do {
            if (cursor.getColumnIndex("id") != -1) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }while (cursor.moveToNext());


        return id;
    }

    public int getConversaRows(int user){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from conversas where user = ?", new String[]{String.valueOf(user)});
        return cursor.getCount();
    }

    @SuppressLint("Range")
    public String getCvsName(int UML, int t){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select nome from conversas where user = ?", new String[]{String.valueOf(UML)});
//        Cursor cursor = MyDB.rawQuery("select conversas.nome from conversas join users on conversas.user = users.id where users.email = ?", new String[]{UML});
        String name;
        if (cursor != null && cursor.moveToFirst()){
            cursor.move(t);
//            name = cursor.getColumnName(0);
            name = cursor.getString(0);

        } else {
            name = "Nao achou o nome";
        }
        cursor.close();
        return name;
    }

    public Bitmap getBitmap(int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select image from users where id = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        @SuppressLint("Range") byte[] bitmapdata = cursor.getBlob(cursor.getColumnIndex("image"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        return bitmap;
    }

    public boolean checkMail(String email, String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Pattern Scase = Pattern.compile("[a-zA-z]");
        Matcher hasScase = Scase.matcher(senha);
        Pattern num = Pattern.compile("[0-9]");
        Matcher hasNum = num.matcher(senha);
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(senha);

        if (hasNum.find() && hasSpecial.find() && hasScase.find()) {
            Cursor cursor = MyDB.rawQuery("select * from users where email = ?", new String[]{email});
            return cursor.getCount() > 0;
        }
        return  false;
    }

    public boolean checkLogin(String mail, String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where email = ? and senha = ?", new String[]{mail, senha});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        return it;
    }

    public boolean checkSenha(String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where senha = ?", new String[]{senha});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        return it;
    }

    public boolean editSenha(int id, String Senha){
        SQLiteDatabase MyDN = this.getWritableDatabase();
        MyDN.execSQL("UPDATE users SET senha = ? WHERE id = ?", new String[]{Senha, String.valueOf(id)});
        return true;
    }

    public boolean editUN(int id, String Nome){
        SQLiteDatabase MyDN = this.getWritableDatabase();
        MyDN.execSQL("UPDATE users SET username = ? WHERE id = ?", new String[]{Nome, String.valueOf(id)});
        return true;
    }

    public boolean nuke(int amount){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        switch (amount){
            case 0:
                MyDB.execSQL("DELETE FROM users");
                break;
            case 1:
                MyDB.execSQL("DELETE FROM conversas");
                break;
            case 2:
                MyDB.execSQL("DELETE FROM users");
                MyDB.execSQL("DELETE FROM conversas");
                break;
            default:
                break;
        }
        return true;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public boolean addEntry(byte[] image) throws SQLiteException {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put("image", image);
        long result = MyDB.insert("users", null, cv);

        return result != -1;
    }


    public boolean checkBitmap(int userID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where id = ?", new String[]{String.valueOf(userID)});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        return it;
    }
}


