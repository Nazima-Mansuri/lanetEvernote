package com.example.lcom67.demoapp.AdapterClass;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcom67.demoapp.Beans.Contacts;
import com.example.lcom67.demoapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by lcom67 on 5/7/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private List<Contacts> contactsList;
    Contacts contacts;
    Context context;
    private AdapterView.OnItemClickListener mItemClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView title;
        public ImageView Image;
        public CardView mCardView;
        public TextView mTextViewTitle;

        public MyViewHolder(View view)
        {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.tTitle);
            Image = (ImageView) view.findViewById(R.id.displayImage);

            title.setOnClickListener(this);
            Image.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {

        }

    }

    public MyAdapter(Context context, List<Contacts> contactsList)
    {
        this.context = context;
        this.contactsList = contactsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_activity, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        Contacts contacts = contactsList.get(position);

        holder.title.setText(contacts.getNotes_Title());

        if (contacts.getmImagePath() != null) {
            holder.title.setText(" Thumb_" + contacts.getmImagePath());
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            try {
                File f = new File(directory, contacts.getmImagePath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.Image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (contacts.getCameraImage() != null)
        {
            holder.title.setText("SnapShot");
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            try {
                File f = new File(directory, contacts.getCameraImage());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.Image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
