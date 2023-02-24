package com.example.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Khai báo đối tượng lưu trữ danh sách các contact
    private ArrayList<Contact> ContactList;
    private Adapter ListAdapter;
    private EditText etSearch;
    private ListView lstContact;
    private FloatingActionButton btnAdd;
    private int SelectedItemId;

    private Contact c;

    private MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Thiết lập dữ liệu mẫu
        ContactList = new ArrayList<>();
//        ContactList.add(new Contact(1, "img1", "Trần Văn An", "56789056"));
//        ContactList.add(new Contact(2, "img2", "Nguyễn Thế Hiền", "45678520"));
//        ContactList.add(new Contact(3, "img3", "Bùi Phương Linh", "69553114"));

        //Tạo mới CSDL
        db = new MyDB(this, "ContactDB", null, 1);

        //Thêm dữ liệu lần đầu vào db
//        db.addContact(new Contact(1, "img1", "Trần Văn An", "56789056"));
//        db.addContact(new Contact(2, "img2", "Nguyễn Thế Hiền", "45678520"));
//        db.addContact(new Contact(3, "img3", "Bùi Phương Linh", "69553114"));

        ContactList = db.getAllContact();

        lstContact = findViewById(R.id.lstContact);
        ListAdapter = new Adapter(ContactList, this);
        lstContact.setAdapter(ListAdapter);

        etSearch = findViewById(R.id.etSearch);

        //Gắn MenuPopup
        registerForContextMenu(lstContact);

        btnAdd = findViewById(R.id.btnAdd);
        //Click vào button Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. Tạo intent để mở subactivity
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //2. Truyền dữ liệu sang subactivity bằng bundle nếu cần
                //3. Mở subactivity bằng cách gọi hàm startactivity hoặc startactivityforresult
                startActivityForResult(intent, 100);
            }
        });

        //Tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                ListAdapter.getFilter().filter(charSequence.toString());
                ListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Long click
        lstContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedItemId = position;
                return false;
            }
        });
    }
    //Hết onCreate



    //Tạo menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = new MenuInflater((this));
        inflater.inflate(R.menu.actionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Hàm lấy tên
    public static String splitName(String fullname) {
        fullname = fullname.trim();
        String name = fullname.substring(fullname.lastIndexOf(" ")+1, fullname.length());
        return name;
    }

    //Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnuSortName:
                //Sap xep Arraylist<Contact> theo Name
                Collections.sort(ContactList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact o1, Contact o2) {
                        return (splitName(o1.getName()).compareTo(splitName(o2.getName())));
                    }
                });

                break;
            case R.id.mnuSortPhone:
                //Sap xep ArrayList<Contact> theo Phone
                Collections.sort(ContactList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact o1, Contact o2) {
                        //Sắp xếp theo Phone tăng dần
//                        if((Integer.parseInt(o1.getPhone()) < Integer.parseInt(o2.getPhone())))
//                            return -1;
//                        else
//                            return 1;
                        return (Integer.parseInt(o1.getPhone()) - Integer.parseInt(o2.getPhone()));
                    }
                });
                break;
        }
        ListAdapter = new Adapter(ContactList, this);
        lstContact.setAdapter(ListAdapter);
        return super.onOptionsItemSelected(item);
    }

    //Tạo MenuPupup
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupopup, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Contact con = ContactList.get(SelectedItemId);
        switch(item.getItemId())
        {
            case R.id.mnuEdit:
                //1. Tạo intent để mở subactivity
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //2. Truyền dữ liệu sang subactivity bằng bundle nếu cần
                Contact c = ContactList.get(SelectedItemId);
                Bundle b = new Bundle();
                b.putInt("Id", c.getId());
                b.putString("Image", c.getImages());
                b.putString("Name", c.getName());
                b.putString("Phone", c.getPhone());
                intent.putExtras(b);
                //3. Mở subactivity bằng cách gọi hàm startactivity hoặc startactivityforresult
                startActivityForResult(intent, 200);
                break;
            case R.id.mnuDelete:
                //Tạo Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn muốn xóa không ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteContact(con.getId());
                        ContactList = db.getAllContact();
                        //ContactList.remove(SelectedItemId);
                        ListAdapter = new Adapter(ContactList, MainActivity.this);
                        lstContact.setAdapter(ListAdapter);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;
            case R.id.mnuCall:
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + con.getPhone()));
                startActivity(intentCall);
                break;
            case R.id.mnuSMS:
                Intent inSms = new Intent(Intent.ACTION_SEND);
                inSms.setType("text/plain");
                inSms.putExtra(Intent.EXTRA_TEXT , con.getPhone());
                startActivity(inSms);
                break;
            case R.id.mnuEmail:
                Intent inEmail = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/jjlj"+con.getPhone()));
                startActivity(inEmail);
                break;
            case R.id.mnuLink:
                Intent intentMess = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com"));
                startActivity(intentMess);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        Contact newcontact = new Contact(id, "Image", name, phone);
        if(requestCode == 100 && resultCode == 150)
        {
            //Truong hop them
            db.addContact(newcontact);
            ContactList = db.getAllContact();
            ListAdapter = new Adapter(ContactList, this);
            lstContact.setAdapter(ListAdapter);

        }
        else if(requestCode == 200 && resultCode == 150)
        {
            //Truong hop sua
//            for(Contact c: ContactList)
//            {
//                if(c.getId() == id)
//                {
//                    c.setName(name);
//                    c.setPhone(phone);
//                }
//            }
            for(Contact c: ContactList)
            {
                if(c.getId() == id)
                {
                    c.setName(name);
                    c.setPhone(phone);
                    db.updateContact(id, c);
                }
            }

            ListAdapter = new Adapter(ContactList, this);
            lstContact.setAdapter(ListAdapter);
        }
    }


}
