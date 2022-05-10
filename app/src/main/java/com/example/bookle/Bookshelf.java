package com.example.bookle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookle.databinding.BookshelfBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Bookshelf extends AppCompatActivity implements SimpleAdapter.SimpleViewHolder.OnCoverClickListener {
    // FIXME: Maybe add date to firebase instead of hardcode so we can change end of bookshelf?
    private final String DAY_ZERO = "2022-05-03";
    private final int DAYS_IN_WEEK = 7;

    BookshelfBinding binding;
    SimpleAdapter mAdapter;
    LocalDate today;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BookshelfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView mRecyclerView = (RecyclerView) binding.recyclerview;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        //Your RecyclerView.Adapter
        mAdapter = new SimpleAdapter(this, this::onCoverClick, DAY_ZERO);

        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections =
                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        //Count is number of Bookles so far
        today = LocalDate.now();
        LocalDate dayZero = LocalDate.parse(DAY_ZERO);
        int count = Period.between(dayZero, today).getDays() + 1;

        //This line is a trick to ceiling divide
        int numberOfSections = ((count + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK);

        //Add a section for each week with at least one Bookle
        String title;
        for (int i = 0; i < numberOfSections; i++) {
            title = String.format("Week %d", numberOfSections - i);
            sections.add(new SectionedGridRecyclerViewAdapter
                    .Section(i * DAYS_IN_WEEK, title));
        }

        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                SectionedGridRecyclerViewAdapter(this,R.layout.section_header, R.id.header_title,mRecyclerView,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCoverClick(int position) {
        if (position % 8 != 0) {
            int index = mAdapter.mItems.get(position - (Math.floorDiv(position, 8) + 1));
            if (index == 0) {
                boolean revealed = Utils.isRevealed(this);
                if (!revealed ) {
                    //If today's unrevealed Bookle is clicked, open the reader
                    Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
                    String today = LocalDate.now().toString();
                    readerIntent.putExtra("DAY", today);
                    readerIntent.putExtra("SOURCE", "BOOKSHELF");
                    startActivity(readerIntent);
                    return;
                }
            }
            //Otherwise, open a book dialog for this Bookle
            Intent intent = new Intent(getApplicationContext(), BookDialog.class);
            intent.putExtra("BOOK", index);
            intent.putExtra("SOURCE", "BOOKSHELF");
            startActivity(intent);
        }
    }
}
