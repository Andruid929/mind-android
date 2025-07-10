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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import net.druidlabs.mindsync.activities.NoteEditorActivity;
import net.druidlabs.mindsync.activities.SettingsActivity;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notes.NoteClickListener;
import net.druidlabs.mindsync.notes.NotesRecyclerAdapter;
import net.druidlabs.mindsync.notesio.NotesIO;
import net.druidlabs.mindsync.ui.Animations;
import net.druidlabs.mindsync.util.AppResources;
import net.druidlabs.mindsync.util.GridSpacing;

import java.util.List;

/**
 * The activity you boot into when you launch the application.
 * This activity is what starts off every single project unless opened by
 * an external application.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.0.1
 */

public class MainActivity extends AppCompatActivity implements NoteClickListener {

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

    private NotesRecyclerAdapter notesAdapter;

    /**
     * The state of the add_note button,
     * this lets the cycle know when to start the "expand" animation.
     * <p>False by default
     */

    private boolean isAddNoteFabClicked;

    private Context appContext;

    private Context uiContext;

    private ActivityResultLauncher<Intent> newNoteResultLauncher;
    private ActivityResultLauncher<Intent> editNoteResultLauncher;

    private DrawerLayout homeDrawerLayout;

    private NavigationView homeNavigationView;

    private MaterialToolbar homeToolbar;

    private RecyclerView notesListRecyclerView;

    MenuItem openSettingsActivity;
    MenuItem viewSrcCodeItem;

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

        uiContext = MainActivity.this;

        setSupportActionBar(homeToolbar);

        notesList = NotesIO.readTypeFromStorage(appContext);

        setupUI();

        setupNotesAdapter();

        initialiseListeners();
        
        homeNavigationView.bringToFront();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout, homeToolbar,
                R.string.home_drawer_open_desc, R.string.home_drawer_close_desc);
        homeDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        newNoteResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), newNoteResultCallback());

        editNoteResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), editedNoteResultCallback());
    }

    @Override
    protected void onPause() {
        super.onPause();

        NotesIO.saveNotesToStorage(appContext);
    }

    @Override
    public void onNoteClick(int position) {
        Intent noteEditorIntent = new Intent(uiContext, NoteEditorActivity.class);
        noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position); //Send the clicked note's index to the NoteEditorActivity

        editNoteResultLauncher.launch(noteEditorIntent);
    }

    @Override
    public void onNoteLongClick(int position) {
        LayoutInflater viewInflater = LayoutInflater.from(uiContext);

        View dialogView = viewInflater.inflate(R.layout.note_options_dialog, null);

        View confirmDialogView = viewInflater.inflate(R.layout.delete_confirmation_dialog, null);

        Note heldNote = notesList.get(position);

        AlertDialog noteOptionsDialog = createOptionsDialog(position, dialogView, confirmDialogView, heldNote.getHeading()).create();

        TextView dialogNoteHeader = dialogView.findViewById(R.id.note_options_preview_header);
        TextView dialogNoteBody = dialogView.findViewById(R.id.note_options_preview_body);

        dialogNoteHeader.setText(heldNote.getHeading());
        dialogNoteBody.setText(heldNote.getBody());

        noteOptionsDialog.show();
    }

    /**
     * Instantiate UI variables.
     *
     * @since 1.2.0-beta.2
     */

    private void setupUI() {
        homeDrawerLayout = findViewById(R.id.main);

        homeNavigationView = findViewById(R.id.home_drawer_nav_view);

        homeToolbar = findViewById(R.id.home_toolbar);

        notesListRecyclerView = findViewById(R.id.home_notes_gridview);

        addNoteFab = findViewById(R.id.home_add_note_fab);
        addTextNoteFab = findViewById(R.id.home_add_text_note_fab);

        Menu homeDrawerMenu = homeNavigationView.getMenu();

        openSettingsActivity = homeDrawerMenu.findItem(R.id.settings_menu_item);

        viewSrcCodeItem = homeDrawerMenu.findItem(R.id.view_src_code_menu_item);
    }

    /**
     * Set event listeners.
     *
     * @since 1.2.0-beta.2
     */

    private void initialiseListeners() {

        //Open this app's official GitHub repository
        viewSrcCodeItem.setOnMenuItemClickListener(item -> {
            Intent openGitHubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Andruid929/mind-android/"));
            startActivity(openGitHubIntent);
            return true;
        });

        openSettingsActivity.setOnMenuItemClickListener(item -> {
            Intent openSettigsIntent = new Intent(appContext, SettingsActivity.class);
            startActivity(openSettigsIntent);
            return true;
        });

        addNoteFab.setOnClickListener(v -> onFabExpanded());

        addTextNoteFab.setOnClickListener(v -> {
            Intent addNoteInEditorIntent = new Intent(uiContext, NoteEditorActivity.class);

            newNoteResultLauncher.launch(addNoteInEditorIntent);
        });

    }

    /**
     * Setup and customise notes adapter.
     *
     * @since 1.2.0-beta.2
     */

    private void setupNotesAdapter() {
        notesAdapter = new NotesRecyclerAdapter(notesList, this);

        RecyclerView.LayoutManager notesLayoutManager = new GridLayoutManager(uiContext, 2);

        notesListRecyclerView.setLayoutManager(notesLayoutManager);
        notesListRecyclerView.setAdapter(notesAdapter);
        notesListRecyclerView.addItemDecoration(new GridSpacing(2, -25));
    }

    /**
     * Check if new note was added to the {@link #notesList notes list}.
     * <p>When a new note is added in the {@link NoteEditorActivity note editor},
     * a "new note added" result is set which this method attempts to retrieve.
     * If a result is received and returns true, the adapter is notified of the added
     * note at the last index.
     *
     * @since 1.1.0-beta.3
     */

    private ActivityResultCallback<ActivityResult> newNoteResultCallback() {
        return result -> {
            if (result.getResultCode() != RESULT_OK) {
                return;
            }

            Intent data = result.getData();

            if (data == null) {
                return;
            }

            boolean newNoteAdded = data.getBooleanExtra(NoteEditorActivity.NEW_NOTE_ADDED_EXTRA, false);

            if (newNoteAdded) {
                notesAdapter.notifyItemInserted(notesList.size() - 1);
            }
        };
    }

    /**
     * Check if a note in the {@link #notesList notes list} was edited
     * and refresh the {@link #notesAdapter  notes adapter} if a note was changed.
     *
     * @since 1.1.0-beta.3
     */

    private ActivityResultCallback<ActivityResult> editedNoteResultCallback() {
        return result -> {
            if (result.getResultCode() != RESULT_OK) {
                return;
            }

            Intent data = result.getData();

            if (data == null) {
                return;
            }

            int editedNoteIndex = data.getIntExtra(NoteEditorActivity.EDITED_NOTE_INDEX_EXTRA, -1);

            if (editedNoteIndex == -1) {
                return;
            }

            notesAdapter.notifyItemChanged(editedNoteIndex);
        };
    }

    /**
     * Create a new note options dialog when a note is held.
     *
     * @param position      the position/index of the note held.
     * @param dialogView    the inflated dialog layout resource
     * @param confirmDialog the inflated confirmation dialog.
     * @param noteHeading   the heading the note held.
     * @return a new dialog builder with two buttons.
     * @since 0.10.0
     */

    private @NonNull AlertDialog.Builder createOptionsDialog(int position, View dialogView,
                                                             View confirmDialog, String noteHeading) {
        return new AlertDialog.Builder(uiContext, R.style.NoteDialogTheme)

                .setView(dialogView)
                //Open the note editor to edit the note
                .setPositiveButton(R.string.note_options_btn_pos, (dialog, which) -> {
                    Intent noteEditorIntent = new Intent(uiContext, NoteEditorActivity.class);

                    //Send the clicked note's index to the NoteEditorActivity
                    noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position);

                    editNoteResultLauncher.launch(noteEditorIntent);
                })
                //Delete the note
                .setNegativeButton(R.string.note_options_btn_neg, ((dialog, which) -> {
                    TextView confirmDeleteTextView = confirmDialog.findViewById(R.id.note_delete_confirm_dialog_textview);

                    //Note heading in quotes before the delete confirmation text
                    String confirmText = "\"" + noteHeading + "\" " +
                            AppResources.getStringResource(R.string.note_del_confirm_text, uiContext);

                    confirmDeleteTextView.setText(confirmText);

                    AlertDialog deleteConfirmDialog = confirmDeleteDialog(position, confirmDialog, noteHeading).create();

                    deleteConfirmDialog.show();
                }));

    }

    /**
     * Create a confirmation dialog when a note is being deleted.
     *
     * @param position    the position/index of the note held.
     * @param dialogView  the inflated dialog layout resource
     * @param noteHeading the heading of the note being deleted.
     * @return a new confirmation dialog builder with two buttons.
     * @since 1.1.0-beta.2
     */

    private @NonNull AlertDialog.Builder confirmDeleteDialog(int position, View dialogView, String noteHeading) {
        return new AlertDialog.Builder(uiContext, R.style.NoteDialogTheme)
                .setView(dialogView)
                //If the user cancels
                .setPositiveButton(R.string.note_del_confirm_pos_btn, ((dialog, which) -> dialog.dismiss()))
                //If the user clicks delete
                .setNegativeButton(R.string.note_del_confirm_neg_btn, ((dialog, which) -> {
                    notesList.remove(position);

                    //Andruid929: I use this as a last resort,
                    // notifyItemRemoved(int) would crash the app after deleting multiple notes.
                    notesAdapter.notifyDataSetChanged();

                    NotesIO.saveNotesToStorage(uiContext);

                    String noteDeletionConfirmationText = "\"" + noteHeading + "\" " +
                            AppResources.getStringResource(R.string.note_deleted_toast, uiContext);

                    Toast.makeText(uiContext, noteDeletionConfirmationText, Toast.LENGTH_SHORT).show();
                }));
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