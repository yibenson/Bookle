package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
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
        int numBookles = Period.between(firstDay, today).getDays() + 1;

        mContext = context;
        mItems = new ArrayList<Integer>(numBookles);
        for (int i = 0; i < numBookles; i++) {
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

        boolean revealed = Utils.isRevealed(mContext);
        if ((position == 0) && !revealed) {
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.mysterybook));
        } else {
            String day = LocalDate.now().minusDays(position).toString();
            DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                    .child("Books").child(day);

            Utils.databaseMethod actionCover = (imageUri) ->
                    Picasso.get().load(imageUri).into(holder.imageView);
            Utils.doFromDatabase(databaseToday, "cover", actionCover);

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