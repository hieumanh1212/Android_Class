package com.example.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    public static final String TableName = "ContactTable";
    public static final String Id = "Id";
    public static final String Name = "Fullname";
    public static final String Phone = "Phonenumber";
    public static final String Image = "Image";

    public MyDB(@Nullable Context context,
                @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory,
                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Tạo câu SQL để tạo bảng TableContact
        String sqlCreate = " Create table if not exists " + TableName + "("
                + Id + " Integer Primary key, "
                + Image + " Text, "
                + Name + " Text, "
                + Phone + " Text)";
        //Chạy câu truy vấn SQL để tạo bảng
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    //Hàm này chạy khi cập nhật cơ sở dữ liệu update
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Xóa bảng TableContact đã có
        sqLiteDatabase.execSQL("Drop table if exists " + TableName);
        //Tạo lại
        onCreate(sqLiteDatabase);
    }

    //Lấy tất cả các dòng của bảng TableContact trả về dạnh ArrayList
    public ArrayList<Contact> getAllContact() {
        ArrayList<Contact> list = new ArrayList<>();
        //Câu truy vấn
        String sql = "Select * from " + TableName;
        //Lấy đối tượng CSDL SQLITE
        SQLiteDatabase db = this.getReadableDatabase();
        //Chạy câu truy vấn trả về dạng Cursor
        Cursor cursor = db.rawQuery(sql, null);
        //Tạo ArrayList<Contact> để trả về
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Contact contact = new Contact(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                list.add(contact);
            }
        }
        return list;
    }

    //Hàm thêm một contact vào bảng TableContact
    public void addContact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Id, contact.getId());
        value.put(Name, contact.getName());
        value.put(Image, contact.getImages());
        value.put(Phone, contact.getPhone());
        db.insert(TableName, null, value);
        db.close();
    }
    public void updateContact(int id, Contact contact)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        //value.put(Id, contact.getId());
        value.put(Name, contact.getName());
        value.put(Image, contact.getImages());
        value.put(Phone, contact.getPhone());

        db.update(TableName, value, "id = " + String.valueOf(id), null);
        db.close();
    }

    public void deleteContact(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName, "id = " + String.valueOf(id), null);
    }
}
