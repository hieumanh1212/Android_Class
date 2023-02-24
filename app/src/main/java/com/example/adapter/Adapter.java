package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter implements Filterable {
    //Nguồn dữ liệu cho adapter
    private ArrayList<Contact> data;
    private ArrayList<Contact> databackup;
    //Ngữ cảnh ứng dụng
    private Activity context;
    //Đối tượng phân tích layout
    private LayoutInflater inflater;

    public Adapter(){

    }

    public Adapter(ArrayList<Contact> data, Activity activity) {
        this.data = data;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Contact> getData() {
        return data;
    }

    public void setData(ArrayList<Contact> data) {
        this.data = data;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null)
            v = inflater.inflate(R.layout.contactitem, null);
        ImageView imgprofile = v.findViewById(R.id.imageView);
        TextView tvname = v.findViewById(R.id.tvName);
        tvname.setText(data.get(i).getName());
        TextView tvphone = v.findViewById(R.id.tvPhone);
        tvphone.setText(data.get(i).getPhone());
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                //Backup dữ liệu: lưu tạm data vào databackup
                if(databackup == null)
                    databackup = new ArrayList<>(data);
                //Nếu chuỗi để filter là rỗng thì khôi phục dữ liệu
                if(charSequence == null || charSequence.length() == 0)
                {
                    fr.count = databackup.size();
                    fr.values = databackup;
                }
                //CÒn nếu rỗng thì thực hiện filter
                else
                {
                    ArrayList<Contact> newdata = new ArrayList<>();
                    for(Contact c:databackup)
                        if(c.getName().toLowerCase().contains(
                                charSequence.toString().toLowerCase()))
                            newdata.add(c);
                    fr.count = newdata.size();
                    fr.values = newdata;
                }
                return fr;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                data = new ArrayList<Contact>();
                ArrayList<Contact> tmp = (ArrayList<Contact>)filterResults.values;
                for(Contact c:tmp)
                    data.add(c);
                notifyDataSetChanged();
            }
        };
        return f;
    }
}
