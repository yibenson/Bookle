package com.example.bookle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookle.databinding.BookDialogBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;

public class BookDialog extends AppCompatActivity {

    BookDialogBinding bookDialogBinding;

    private String amazonLink;
    private String title;
    private String author;

    LocalDate localDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDialogBinding = BookDialogBinding.inflate(getLayoutInflater());
        setContentView(bookDialogBinding.getRoot());

        int index = getIntent().getIntExtra(getString(R.string.DAY), 0);
        localDate = LocalDate.now().minusDays(index);

        getDatabaseValues();

        bookDialogBinding.bookleMsg.setText(getString(R.string.bookle_was, localDate.format(Utils.dateFormatToUser)));
        bookDialogBinding.bookleMsg.bringToFront();

        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        readerIntent.putExtra(getString(R.string.DAY), localDate.format(Utils.dateFormatInternal));
        bookDialogBinding.bookDialogCover.setOnClickListener(view -> startActivity(readerIntent));

        bookDialogBinding.amazonButton.setOnClickListener(view -> open_link());
        bookDialogBinding.backButton.setOnClickListener(view -> finish());
        bookDialogBinding.shareButton.setOnClickListener(view -> clipboard());

        Utils.setDarkMode(this);
    }

    private void getDatabaseValues() {
        String day = localDate.format(Utils.dateFormatInternal);
        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.BOOKS)).child(day);

        Utils.doFromDatabase(databaseToday, "title", ti -> {
            title = ti;
            bookDialogBinding.bookDialogTitle.setText(title);
        });

        Utils.doFromDatabase(databaseToday, "author", au -> {
            author = au;
            bookDialogBinding.bookDialogAuthor.setText(getString(R.string.written_by, author));
        });

        Utils.doFromDatabase(databaseToday, "cover", imageUri -> {
            Picasso.get().load(imageUri).into(bookDialogBinding.bookDialogCover);
        });

        Utils.doFromDatabase(databaseToday, "buy", buy -> {
            amazonLink = buy;
        });
    }

    private void open_link() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonLink));
        startActivity(browserIntent);
    }

    private void clipboard() {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        String text = getString(R.string.share_prior, localDate.format(Utils.dateFormatToUser),
                title, author);

        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

}

