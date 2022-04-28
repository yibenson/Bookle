package com.example.bookle;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookle.databinding.BookshelfBinding;

import java.util.ArrayList;
import java.util.List;

public class Bookshelf extends AppCompatActivity implements SimpleAdapter.SimpleViewHolder.OnCoverClickListener {
    BookshelfBinding binding;
    SparseArray<SectionedGridRecyclerViewAdapter.Section> mSections;
    List<Integer> mItems;
    public static int from_bookshelf = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BookshelfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView mRecyclerView = (RecyclerView) binding.recyclerview;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        int pre_reveal_covers[] = new int[]{ R.drawable.mysterybook, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};
        int post_reveal_covers[] = new int[]{ R.drawable.cover1, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};

        //Your RecyclerView.Adapter
        SimpleAdapter mAdapter = new SimpleAdapter(this);

        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections =
                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(7,"Section 2"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"Section 3"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(21,"Section 4"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(28,"Section 5"));

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

    @Override
    public void onCoverClick(int position) {
        startActivity(new Intent(getApplicationContext(), BookToday.class));
    }
}
