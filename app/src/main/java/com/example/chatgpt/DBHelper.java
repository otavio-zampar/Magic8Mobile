package com.example.chatgpt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
        MyDB.execSQL("create Table conversas(id INTEGER primary key AUTOINCREMENT, user INTEGER NOT NULL, nome TEXT NOT NULL)");
        MyDB.execSQL("create Table msgs(id INTEGER primary key AUTOINCREMENT, IDconversa TEXT NOT NULL, prompt TEXT NOT NULL, answer TEXT NOT NULL)");
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
        MyDB.execSQL("drop Table if exists msgs");
        MyDB.close();

    }

    public boolean insertData(String username, String email, String senha){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("senha", senha);

        long result = MyDB.insert("users", null, contentValues);
        MyDB.close();
        return result != -1;

    }

    public boolean insertConversas(int user, String nome){ // user nome conversa date
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("nome", nome);
        long result = MyDB.insert("conversas", null, contentValues);
        MyDB.close();
        return result != -1;
    }

    @SuppressLint("Range")
    public int getConvID(String nome){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select id from conversas where nome = ?", new String[]{nome});
        int id = -1;

        cursor.moveToFirst();
        if (cursor.getColumnIndex("nome") != -1) {
            id = cursor.getInt(cursor.getColumnIndex("nome"));
        }
        cursor.close();
        MyDB.close();
        return id;
    }

    public boolean insertMsgs(int ConvID, String prompt, String answer){ // user nome conversa date
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDconversa", ConvID);
        contentValues.put("prompt", prompt);
        contentValues.put("answer", answer);
        long result = MyDB.insert("msgs", null, contentValues);
        MyDB.close();
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
        cursor.close();
        MyDB.close();
        return username;
    }

    @SuppressLint("Range")
    public String getEmail(int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select email from users where id = ?", new String[]{String.valueOf(id)});
        String email = "Nao achou o email";

        cursor.moveToFirst();
        if (cursor.getColumnIndex("email") != -1) {
            email = cursor.getString(cursor.getColumnIndex("email"));
        }
        cursor.close();
        MyDB.close();
        return email;
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

        cursor.close();
        MyDB.close();
        return id;
    }

    public int getConversaRows(int user){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from conversas where user = ?", new String[]{String.valueOf(user)});
        int a = cursor.getCount();
        cursor.close();
        MyDB.close();
        return a;
    }

    public int getMsgsRows(int IDconv){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from msgs where IDconversa = ?", new String[]{String.valueOf(IDconv)});
        int a = cursor.getCount();
        cursor.close();
        MyDB.close();
        return a;
    }

    @SuppressLint("Range")
    public String getPrompt(int IDConv, int t){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select prompt from msgs where IDconversa = ?", new String[]{String.valueOf(IDConv)});
        String prompt;

        cursor.move(t);
        if (cursor.moveToFirst()) {
            cursor.move(t);
            prompt = cursor.getString(0);
        } else {
            prompt = "Nao achou o prompt";
        }
        cursor.close();
        MyDB.close();
        return prompt;
    }

    @SuppressLint("Range")
    public String getAnswer(int IDConv, int t){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select answer from msgs where IDconversa = ?", new String[]{String.valueOf(IDConv)});
        String answer;

        cursor.move(t);
        if (cursor.moveToFirst()) {
            cursor.move(t);
            answer = cursor.getString(0);
        } else {
            answer = "Nao achou a resposta";
        }
        cursor.close();
        MyDB.close();
        return answer;
    }

    @SuppressLint("Range")
    public String getCvsName(int UML, int t){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select nome from conversas where user = ?", new String[]{String.valueOf(UML)});
        String name;
        if (cursor != null && cursor.moveToFirst()){
            cursor.move(t);
            name = cursor.getString(0);

        } else {
            name = "Nao achou o nome";
        }
        assert cursor != null;
        cursor.close();
        MyDB.close();
        return name;
    }

    @SuppressLint("Range")
    public int getCvsID(int UML, int t){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select id from conversas where user = ?", new String[]{String.valueOf(UML)});
        int id;
        if (cursor != null && cursor.moveToFirst()){
            cursor.move(t);
            id = cursor.getInt(0);

        } else {
            id = -1;
        }
        assert cursor != null;
        cursor.close();
        MyDB.close();
        return id;
    }

    public Bitmap getBitmap(int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select image from users where id = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        @SuppressLint("Range") byte[] bitmapdata = cursor.getBlob(cursor.getColumnIndex("image"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        cursor.close();
        MyDB.close();
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
            boolean a = cursor.getCount() > 0;
            cursor.close();
            MyDB.close();
            return a;
        }
        return  false;
    }

    public boolean checkLogin(String mail, String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where email = ? and senha = ?", new String[]{mail, senha});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        MyDB.close();
        return it;
    }

    public boolean checkSenha(String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where senha = ?", new String[]{senha});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        MyDB.close();
        return it;
    }

    public boolean editSenha(int id, String Senha){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("UPDATE users SET senha = ? WHERE id = ?", new String[]{Senha, String.valueOf(id)});
        MyDB.close();
        return true;
    }

    public boolean editCVSname(int id, String nome) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("UPDATE conversas SET nome = 'carlosAlberto' WHERE id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
        return true;
    }



    public boolean editUN(int id, String Nome){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("UPDATE users SET username = ? WHERE id = ?", new String[]{Nome, String.valueOf(id)});
        MyDB.close();
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
                MyDB.execSQL("DELETE FROM msgs");
                break;
            case 3:
                MyDB.execSQL("DELETE FROM conversas");
                MyDB.execSQL("DELETE FROM msgs");
                break;
            case 4:
                MyDB.execSQL("DELETE FROM users");
                MyDB.execSQL("DELETE FROM conversas");
                MyDB.execSQL("DELETE FROM msgs");
                break;
            default:
                break;
        }
        MyDB.close();
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
        MyDB.close();
        return result != -1;
    }


    public boolean checkBitmap(int userID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where id = ?", new String[]{String.valueOf(userID)});
        boolean it = cursor.getCount() > 0;
        cursor.close();
        MyDB.close();
        return it;
    }
}


