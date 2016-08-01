package com.example.lcom67.demoapp.AdapterClass;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.lcom67.demoapp.Beans.Contacts;
import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.R;
import com.example.lcom67.demoapp.RecyclerViewActivity;
import com.example.lcom67.demoapp.UpdateData;

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
    Context context;
    private AdapterView.OnItemClickListener mItemClickListener;
    private DBConnection helper;


    public MyAdapter() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title,dateTitle;
        public ImageView Image,delete;
        public CardView mCardView;
        RelativeLayout rl_main;

        public MyViewHolder(View view)
        {
            super(view);
            delete = (ImageView) view.findViewById(R.id.trash);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.tTitle);
            dateTitle = (TextView) view.findViewById(R.id.dateTitle);
            Image = (ImageView) view.findViewById(R.id.displayImage);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_Main);
        }


    }

    public MyAdapter(Context context, List<Contacts> contactsList)
    {
        this.context = context;
        helper = new DBConnection(context);
        this.contactsList = contactsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_activity, parent, false);
            SwipeLayout item = (SwipeLayout) view.findViewById(R.id.swipe_item);
            item.setShowMode(SwipeLayout.ShowMode.PullOut);
            item.addDrag(SwipeLayout.DragEdge.Right, item.findViewById(R.id.bottom_wrapper_2));
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {

        final Contacts contacts = contactsList.get(position);

        holder.title.setText(contacts.getNotes_Title());

        if(contacts.getReminder_date() != null)
        {
            holder.dateTitle.setText(contacts.getReminder_date());
        }
        else if (contacts.getmImagePath() != null)
        {
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
        }
        else if (contacts.getCameraImage() != null)
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

        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, UpdateData.class);
                intent.putExtra("noteId", contactsList.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                long noteId = contactsList.get(position).getId();
                helper.deleteNote(noteId);
                contactsList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context,"Note Deleted..!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void remove(int position)
    {
        contactsList.remove(position);
        notifyItemRemoved(position);
    }

}
