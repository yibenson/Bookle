package com.example.bookle;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private int COUNT = 14;
    int[] pre_reveal_covers = new int[]{ R.drawable.mysterybook, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};
    int[] post_reveal_covers = new int[]{ R.drawable.cover1, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};


    private final Context mContext;
    public final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private SimpleViewHolder.OnCoverClickListener onCoverClickListener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        private OnCoverClickListener onCoverClickListener;
        public int number;

        public SimpleViewHolder(View view, Context context, OnCoverClickListener onCoverClickListener) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            this.onCoverClickListener = onCoverClickListener;
            imageView.setOnClickListener(this);
            // title = (TextView) view.findViewById(R.id.item_text);
        }

        @Override
        public void onClick(View view) {
            onCoverClickListener.onCoverClick(getAdapterPosition());
        }

        public interface OnCoverClickListener {
            void onCoverClick(int position);
        }


    }

    public SimpleAdapter(Context context, SimpleViewHolder.OnCoverClickListener onCoverClickListener) {
        mContext = context;
        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
        this.onCoverClickListener = onCoverClickListener;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new SimpleViewHolder(view, mContext, onCoverClickListener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        // holder.title.setText(mItems.get(position).toString());
        holder.number = mItems.get(position);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(mContext.getString(R.string.reveal), false)) {
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(mContext, post_reveal_covers[position]));
        } else {
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(mContext, pre_reveal_covers[position]));
        }
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}