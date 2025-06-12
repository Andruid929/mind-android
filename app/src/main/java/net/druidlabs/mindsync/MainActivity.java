package net.druidlabs.mindsync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.druidlabs.mindsync.activities.NoteEditorActivity;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notes.NotesArrayAdapter;
import net.druidlabs.mindsync.ui.Animations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
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
     * Logcat tag for note saving and retrieving.
     */
    private static final String NOTES_IO_TAG = "Notes I/O";

    private FloatingActionButton addNoteFab; //The button with the "+" icon
    private FloatingActionButton addTextNoteFab; //The button with the pencil icon

    public static List<Note> notesList;

    private NotesArrayAdapter<Note> notesArrayAdapter;

    public static final String DATA_FILE_NAME = "MNDDTR.mnd";

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

        notesList = readTypeFromStorage();

        addNoteFab = findViewById(R.id.home_add_note_fab);
        addTextNoteFab = findViewById(R.id.home_add_text_note_fab);

        TextView devUrlText = homeNavigationView.getHeaderView(0).findViewById(R.id.menu_dev_url);
        devUrlText.setMovementMethod(LinkMovementMethod.getInstance());

        notesArrayAdapter = new NotesArrayAdapter<>(MainActivity.this, R.layout.custom_notes_tiles, notesList);

        notesListGridView.setAdapter(notesArrayAdapter);

        notesArrayAdapter.notifyDataSetChanged();

        homeNavigationView.bringToFront();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout, homeToolbar,
                R.string.home_drawer_open_desc, R.string.home_drawer_close_desc);
        homeDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        addNoteFab.setOnClickListener(v -> onFabExpanded());

        addTextNoteFab.setOnClickListener(v -> addNoteDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();

        notesArrayAdapter.notifyDataSetChanged();

        saveNotesToStorage();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveNotesToStorage();
    }

    /**
     * Save the given {@code data} to the documents folder.
     * This uses Google {@link Gson} to serialise the data to a {@code} String and writes it
     * to the file whose name is specified by {@code fileName}.
     *
     * @see #readTypeFromStorage()
     * @since 0.9.0
     */
    private void saveNotesToStorage() {
        Gson gson = new Gson();

        File dataFile = new File(getFilesDir().getPath() + DATA_FILE_NAME);

        try {
            if (!dataFile.exists()) {
                Log.d(NOTES_IO_TAG, "Data non-existent: " + dataFile.createNewFile());
            }
        } catch (IOException e) {
            Log.d(NOTES_IO_TAG, "Unable to create save file, aborting save!");
            e.printStackTrace(System.err);
        }

        try (FileWriter fileWriter = new FileWriter(dataFile);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            gson.toJson(notesList, new TypeToken<List<Note>>() {
            }.getType(), writer);
            Log.d(NOTES_IO_TAG, "Save successful");
        } catch (JsonIOException | IOException e) {
            Log.d(NOTES_IO_TAG, "Unable to write to save file, aborting save!");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Retrieve saved data from the storage under the {@code fileName} specified.
     *
     * @see #saveNotesToStorage()
     * @since 0.9.0
     */

    private List<Note> readTypeFromStorage() {
        Gson gson = new Gson();

        File dataFile = new File(appContext.getFilesDir().getPath() + DATA_FILE_NAME);

        if (!dataFile.exists()) {
            return new LinkedList<>();
        }

        try (FileReader fileReader = new FileReader(dataFile);
             BufferedReader reader = new BufferedReader(fileReader)) {

            return gson.fromJson(reader, new TypeToken<List<Note>>() {}.getType());

        } catch (IOException | JsonSyntaxException e) {
            return new LinkedList<>();
        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoteDialogTheme);

        builder.setView(noteDialogView);
        builder.setPositiveButton(R.string.add_note_dialog_pos_btn, (dialog, which) -> {
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
        builder.setNegativeButton(R.string.add_note_dialog_neg_btn, ((dialog, which) -> dialog.dismiss()));
        builder.create();

        builder.show();
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