package net.druidlabs.mindsync;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import net.druidlabs.mindsync.activities.NoteEditorActivity;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notes.NotesArrayAdapter;
import net.druidlabs.mindsync.notesio.NotesIO;
import net.druidlabs.mindsync.ui.Animations;

import java.util.List;
import java.util.Objects;

/**
 * The activity you boot into when you launch the application.
 * This activity is what starts off every single project unless opened by
 * an external application.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.0.1
 */

public class MainActivity extends AppCompatActivity {

    /**
     * The button with the "+" icon.
     */

    private FloatingActionButton addNoteFab;

    /**
     * The button with the pencil icon.
     */

    private FloatingActionButton addTextNoteFab;

    /**
     * The primary notes list.
     */

    public static List<Note> notesList;

    /**
     * Primary {@code ArrayAdapter} for the {@link #notesList}.
     */

    private NotesArrayAdapter<Note> notesArrayAdapter;

    /**
     * The state of the add_note button,
     * this lets the cycle know when to start the expand animation.
     * <p>False by default
     */

    private boolean isAddNoteFabClicked;

    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isAddNoteFabClicked = false;

        appContext = getApplicationContext();

        DrawerLayout homeDrawerLayout = findViewById(R.id.main);

        NavigationView homeNavigationView = findViewById(R.id.home_drawer_nav_view);

        MaterialToolbar homeToolbar = findViewById(R.id.home_toolbar);

        GridView notesListGridView = findViewById(R.id.home_notes_gridview);

        setSupportActionBar(homeToolbar);

        notesList = NotesIO.readTypeFromStorage(appContext);

        addNoteFab = findViewById(R.id.home_add_note_fab);
        addTextNoteFab = findViewById(R.id.home_add_text_note_fab);

        notesArrayAdapter = new NotesArrayAdapter<>(MainActivity.this, R.layout.custom_notes_tiles, notesList);

        notesListGridView.setAdapter(notesArrayAdapter);

        notesArrayAdapter.notifyDataSetChanged();

        homeNavigationView.bringToFront();

        Menu homeDrawerMenu = homeNavigationView.getMenu();

        MenuItem viewSrcCodeItem = homeDrawerMenu.findItem(R.id.view_src_code_menu_item);
        //Open this app's official GitHub repository
        viewSrcCodeItem.setOnMenuItemClickListener(item -> {
            Intent openGitHubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Andruid929/mind-android/"));
            startActivity(openGitHubIntent);
            return true;
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout, homeToolbar,
                R.string.home_drawer_open_desc, R.string.home_drawer_close_desc);
        homeDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        addNoteFab.setOnClickListener(v -> onFabExpanded());

        addTextNoteFab.setOnClickListener(v -> {
            Intent addNoteInEditorIntent = new Intent(MainActivity.this, NoteEditorActivity.class);
            startActivity(addNoteInEditorIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        notesArrayAdapter.notifyDataSetChanged();

        NotesIO.saveNotesToStorage(appContext);
    }

    @Override
    protected void onPause() {
        super.onPause();

        NotesIO.saveNotesToStorage(appContext);
    }



    /**
     * Invoke a dialog where the user can input a new note's header.
     * If the input is not blank, the note editor activity will be opened and the new note registered.
     *
     * @since 0.8.0
     */

    private void addNoteDialog() {
        LayoutInflater viewInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View noteDialogView = viewInflater.inflate(R.layout.note_create_dialog, null, false);

        TextInputEditText dialogBox = noteDialogView.findViewById(R.id.add_note_dialog_textinput_edittext);

        AlertDialog.Builder addNoteDialogBuilder = new AlertDialog.Builder(this, R.style.NoteDialogTheme);

        addNoteDialogBuilder.setView(noteDialogView);
        addNoteDialogBuilder.setPositiveButton(R.string.add_note_dialog_pos_btn, (dialog, which) -> {
            String newNoteHeading = Objects.requireNonNull(dialogBox.getText()).toString();

            if (newNoteHeading.isBlank()) {
                dialog.dismiss();
                Toast.makeText(appContext, R.string.blank_note_warning, Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();

            new Note(newNoteHeading, "");

            notesArrayAdapter.notifyDataSetChanged();

            Intent noteEditorIntent = new Intent(appContext, NoteEditorActivity.class);
            noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, notesList.size() - 1);
            startActivity(noteEditorIntent);
        });
        addNoteDialogBuilder.setNegativeButton(R.string.add_note_dialog_neg_btn, ((dialog, which) -> dialog.dismiss()));
        addNoteDialogBuilder.create();

        addNoteDialogBuilder.show();
    }

    /**
     * This method gets called when the add note button is clicked.
     *
     * @since 0.5.0
     */

    private void onFabExpanded() {

        setVisibility(isAddNoteFabClicked);
        startAnimation(isAddNoteFabClicked);

        isAddNoteFabClicked = !isAddNoteFabClicked;
    }

    /**
     * Set the visibility of the hidden add_text note button and also make it clickable.
     *
     * @param clicked the state of the button, whether it has been clicked or not.
     * @since 0.5.0
     */

    private void setVisibility(boolean clicked) {
        if (!clicked) { //Since the button is not clicked by default, we negate the state when the button is clicked.
            addTextNoteFab.setVisibility(View.VISIBLE);
            addTextNoteFab.setClickable(true);
        } else {
            addTextNoteFab.setVisibility(View.INVISIBLE);
            addTextNoteFab.setClickable(false);
        }
    }

    /**
     * Start animating the add_text note button.
     *
     * @param clicked the state of the button, whether it has been clicked or not.
     * @since 0.5.0
     */

    private void startAnimation(boolean clicked) {
        Animation fromBottom = Animations.FROM_BOTTOM.getAnimation(appContext);
        Animation toBottom = Animations.TO_BOTTOM.getAnimation(appContext);

        Animation rotateOpen = Animations.ROTATE_90_CLOCKWISE.getAnimation(appContext);
        Animation rotateClose = Animations.ROTATE_90_ANTI_CLOCKWISE.getAnimation(appContext);

        if (!clicked) {
            addTextNoteFab.startAnimation(fromBottom);
            addNoteFab.startAnimation(rotateOpen);
        } else {
            addTextNoteFab.startAnimation(toBottom);
            addNoteFab.startAnimation(rotateClose);
        }
    }
}