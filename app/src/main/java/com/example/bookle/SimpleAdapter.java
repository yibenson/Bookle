package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    // TODO: Hardcoded Bookle
    int[] pre_reveal_covers = new int[]{ R.drawable.mysterybook, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};
    int[] post_reveal_covers = new int[]{ R.drawable.prideprejudice, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};

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

    public SimpleAdapter(Context context, SimpleViewHolder.OnCoverClickListener onCoverClickListener, String dayZero) {
        //Find the number of Bookles that exist
        LocalDate today = LocalDate.now();
        LocalDate firstDay = LocalDate.parse(dayZero);
        int count = Period.between(firstDay, today).getDays();

        mContext = context;
        mItems = new ArrayList<Integer>(count);
        for (int i = 0; i < count; i++) {
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
        // TODO: Hardcoded Bookle
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