import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "GPTMobile.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER primary key, username TEXT NOT NULL, email TEXT unique NOT NULL, senha TEXT NOT NULL)");
        ContentValues CV = new ContentValues();
        CV.put("id", 0);
        CV.put("username", "admin");
        CV.put("email", "admin");
        CV.put("senha", "Umasenh@de@dm1n");
        MyDB.insert("users", null, CV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public boolean insertData(String username, String email, String senha){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", (Integer) null);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("senha", senha);

        long result = MyDB.insert("users", null, contentValues);
        return result != -1;

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

    public boolean checkSenha(String mail, String senha) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
            Cursor cursor = MyDB.rawQuery("select * from users where email = ? and senha = ?", new String[]{mail, senha});
            return cursor.getCount() > 0;
    }
}


