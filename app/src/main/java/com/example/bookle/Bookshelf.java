package com.example.bookle;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookle.databinding.BookshelfBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bookshelf extends AppCompatActivity implements SimpleAdapter.SimpleViewHolder.OnCoverClickListener {
    private final String DAY_ZERO = "2022-05-01";
    private final int DAYS_IN_WEEK = 7;

    BookshelfBinding binding;
    SimpleAdapter mAdapter;
    LocalDate today;
    int numBooks;

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

        today = LocalDate.now();

        //Number of Bookles so far
        numBooks = mAdapter.getItemCount();

        makeSections(mAdapter, mRecyclerView);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onCoverClick(int position) {
        int offset = numBooks % 7;
        int index;
        if (position - 1 < offset && position != 0) {
            index = position - 1;
        } else if ((position - offset - 1) % 8 != 0) {
            index = position - (Math.floorDiv(position - offset, 8)) - 2;
        } else {
            return;
        }

        //If today's Bookle was clicked, and is unrevealed, open the reader
        if (index == 0 && !Utils.isRevealed(this)) {
            Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
            readerIntent.putExtra(getString(R.string.DAY), today.toString());
            startActivity(readerIntent);
        }
        //Otherwise, open a book dialog for this Bookle
        else {
            Intent intent = new Intent(getApplicationContext(), BookDialog.class);
            intent.putExtra(getString(R.string.DAY), index);
            startActivity(intent);
        }
    }

    private void makeSections(SimpleAdapter mAdapter, RecyclerView mRecyclerView) {
        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<>();
        //Add a section for each week with at least one released Bookle
        sections.add(new SectionedGridRecyclerViewAdapter
                .Section(0, getString(R.string.first_bookshelf_section)));

        // Sun = 0 ... Sat = 6, dayOfWeek is number of days since Sunday
        int dayOfWeek = today.getDayOfWeek().getValue() % DAYS_IN_WEEK;
        LocalDate thisSunday = today.minusDays(dayOfWeek);
        // numUnreleased is number of Bookles this week that aren't out yet,
        // aka the number of days until this Saturday.
        int numUnreleased = DAYS_IN_WEEK - 1 - dayOfWeek;

        //This line is a trick to ceiling divide: numBooks / DAYS_IN_WEEK
        int numWeeks = ((numBooks + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK);
        for (int w = 1; w < numWeeks; w++) {
            LocalDate sun = thisSunday.minusDays(w * DAYS_IN_WEEK);
            LocalDate sat = sun.plusDays(DAYS_IN_WEEK - 1);
            String title = String.format(getString(R.string.bookshelf_section),
                    sun.format(Utils.dateFormatToUser), sat.format(Utils.dateFormatToUser));

            sections.add(new SectionedGridRecyclerViewAdapter
                    .Section((w * DAYS_IN_WEEK) - numUnreleased, title));
        }

        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                SectionedGridRecyclerViewAdapter(this,R.layout.section_header, R.id.header_title,mRecyclerView,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }
}
