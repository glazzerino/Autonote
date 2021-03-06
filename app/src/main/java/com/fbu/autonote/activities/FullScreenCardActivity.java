package com.fbu.autonote.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fbu.autonote.R;
import com.fbu.autonote.fragments.ConfirmDeleteFragment;
import com.fbu.autonote.fragments.FullCardNoteFragment;
import com.fbu.autonote.fragments.TextNoteFragment;
import com.fbu.autonote.models.Note;
import com.fbu.autonote.utilities.Favorites;
import com.fbu.autonote.utilities.RecentNotesManager;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class FullScreenCardActivity extends AppCompatActivity implements ConfirmDeleteFragment.DialogClickListener {
    ImageButton btnDeleteNote;
    ImageButton btnFavNote;
    FragmentManager fragmentManager;
    Fragment fragment;
    MaterialButtonToggleGroup toggleGroup;
    DatabaseReference databaseReference;
    Note note;
    Context context;

    public final static String TAG = "FullScreenCardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimations();
        setContentView(R.layout.activity_full_screen_card);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(userId);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnFavNote = findViewById(R.id.btnFavNote);
        toggleGroup = findViewById(R.id.toggleGroupFullCard);
        fragmentManager = getSupportFragmentManager();
        context = this;
        note = getIntent().getParcelableExtra("note");
        autoToggleFavBtn();
        if (note == null) {
            Log.e(TAG, "Error: Note not found within intent");
        }
        fragment = FullCardNoteFragment.newInstance(note.getImageURL());
        startFragment();
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.btnTogglePicture:
                        fragment = FullCardNoteFragment.newInstance(note.getImageURL());
                        break;
                    case R.id.btnToggleText:
                        fragment = TextNoteFragment.newInstance(note.getTextContent());
                        break;
                }
                startFragment();
            }
        });

        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDeleteFragment confirmDeleteFragment = new ConfirmDeleteFragment();
                confirmDeleteFragment.show(fragmentManager, "confirmDelete");
            }
        });

        btnFavNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorites favorites = Favorites.getInstance();
                if (favorites.checkIfFavorite(note))
                    favorites.remove(note);
                else
                    favorites.addFav(note);
                autoToggleFavBtn();
            }
        });
    }

    private void startFragment() {
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flCardFragmentContainer, fragment)
                    .commit();
        }
    }

    //Manage click events for deletion confirmation dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        DatabaseReference noteReference = databaseReference.child(note.getTopic());
        noteReference.child(note.getDate()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                RecentNotesManager.getInstance().deleteNote(note);
                Favorites.getInstance().remove(note);
                Toasty.info(context, "Note deleted", Toasty.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setAnimations() {
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setExitTransition(new Slide(Gravity.LEFT));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "Note deletion aborted");
    }

    private void autoToggleFavBtn() {
        Drawable drawable = getDrawable(R.drawable.ic_baseline_star_outline_24);
        if (Favorites.getInstance().checkIfFavorite(note)) {
            drawable = getDrawable(R.drawable.ic_baseline_star_24);
        }
        btnFavNote.setImageDrawable(drawable);
    }
}