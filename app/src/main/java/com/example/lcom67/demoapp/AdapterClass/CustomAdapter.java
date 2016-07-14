package com.example.lcom67.demoapp.AdapterClass;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcom67.demoapp.Beans.Contacts;
import com.example.lcom67.demoapp.R;

import java.util.List;

/**
 * Created by lcom67 on 1/7/16.
 */

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private List<Contacts> data;
    private LayoutInflater inflater = null;
    public Resources res;
    Contacts tempValues = null;
    int i = 0;
    private String[] filepath;

    public CustomAdapter(Activity activity, List<Contacts> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return i;
    }

    public static class ViewHolder {

        public TextView text_title;
        public ImageView Image;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;
        if (view == null) {
            vi = inflater.inflate(R.layout.tabitem, null);
            holder = new ViewHolder();
            holder.text_title = (TextView) vi.findViewById(R.id.tTitle);
            holder.Image = (ImageView) vi.findViewById(R.id.displayImage);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.text_title.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (Contacts) data.get(i);

            /************  Set Model values in Holder elements ***********/

            holder.text_title.setText(tempValues.getNotes_Title());
            Bitmap bmp = BitmapFactory.decodeFile(filepath[i]);
            holder.Image.setImageBitmap(bmp);

        }
        return vi;
    }

}
