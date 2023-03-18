package com.example.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class SubActivity extends AppCompatActivity {
    private EditText etId;
    private EditText etFullname;
    private EditText etPhone;
    private ImageView ivImage;
    private Button btnOk, btnCancel;
    private String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        etId = findViewById(R.id.etId);
        etFullname = findViewById(R.id.etFullname);
        etPhone = findViewById(R.id.etPhone);
        ivImage = findViewById(R.id.ivImage);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        //Lấy intent từ MainActivity chuyển sang
        Intent intent = getIntent();
        //Lấy bundle
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            int id = bundle.getInt("Id");
            String image = bundle.getString("Image");
            String name = bundle.getString("Name");
            String phone = bundle.getString("Phone");
            etId.setText(String.valueOf(id));
            etFullname.setText(name);
            etPhone.setText(phone);

            btnOk.setText("Edit");
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu và gửi về cho MainActivity
                int id = Integer.parseInt(etId.getText().toString());
                String name = etFullname.getText().toString();
                String phone = etPhone.getText().toString();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putInt("Id", id);
                b.putString("Image", image);
                b.putString("Name", name);
                b.putString("Phone", phone);
                intent.putExtras(b);
                setResult(150, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Click vào Image
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,400);
            }
        });

    }

    //Hết onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==400) {
            ivImage.setImageURI(data.getData());
            image = data.getData().toString();
        }
    }
}