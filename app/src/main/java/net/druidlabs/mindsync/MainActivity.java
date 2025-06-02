package net.druidlabs.mindsync;

import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notes.Notes;
import net.druidlabs.mindsync.notes.NotesArrayAdapter;
import net.druidlabs.mindsync.ui.Animations;

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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout homeDrawerLayout;

    private NavigationView homeNavigationView;

    private MaterialToolbar homeToolbar;

    private GridView notesListGridView;

    private FloatingActionButton addNoteFab; //The button with the "+" icon
    private FloatingActionButton addTextNoteFab; //The button with the pencil icon

    private List<Note> notesList;

    private NotesArrayAdapter<Note> notesArrayAdapter;

    /**
     * The state of the add note button,
     * this lets the cycle know when to start the expand animation.
     * <p>False by default
     */

    private boolean isAddNoteFabClicked;

    private Context context;

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

        context = getApplicationContext();

        homeDrawerLayout = findViewById(R.id.main);

        homeNavigationView = findViewById(R.id.home_drawer_nav_view);

        homeToolbar = findViewById(R.id.home_toolbar);

        notesListGridView = findViewById(R.id.home_notes_gridview);

        addNoteFab = findViewById(R.id.home_add_note_fab);
        addTextNoteFab = findViewById(R.id.home_add_text_note_fab);

        Note note1 = new Note("Test Note 1", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");
        Note note2 = new Note("Test Note 2", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");
        Note note3 = new Note("Test Note 3", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");

        notesList = Notes.getNotes();

        TextView devUrlText = homeNavigationView.getHeaderView(0).findViewById(R.id.menu_dev_url);
        devUrlText.setMovementMethod(LinkMovementMethod.getInstance());

        notesArrayAdapter = new NotesArrayAdapter<>(MainActivity.this, R.layout.custom_notes_tiles, notesList);

        notesListGridView.setAdapter(notesArrayAdapter);

        notesArrayAdapter.notifyDataSetChanged();

        homeNavigationView.bringToFront();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                R.string.home_drawer_open_desc, R.string.home_drawer_close_desc);
        homeDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setSupportActionBar(homeToolbar);

        addNoteFab.setOnClickListener(v -> onFabExpanded());
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
     * Set the visibility of the hidden add text note button and also make it clickable.
     *
     * @param clicked the state of button, whether it has been clicked or not.
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
     * Start animating the add text note button.
     *
     * @param clicked the state of the button, whether it has been clicked or not.
     * @since 0.5.0
     */

    private void startAnimation(boolean clicked) {
        Animation fromBottom = Animations.FROM_BOTTOM.getAnimation(context);
        Animation toBottom = Animations.TO_BOTTOM.getAnimation(context);

        Animation rotateOpen = Animations.ROTATE_90_CLOCKWISE.getAnimation(context);
        Animation rotateClose = Animations.ROTATE_90_ANTI_CLOCKWISE.getAnimation(context);

        if (!clicked) {
            addTextNoteFab.startAnimation(fromBottom);
            addNoteFab.startAnimation(rotateOpen);
        } else {
            addTextNoteFab.startAnimation(toBottom);
            addNoteFab.startAnimation(rotateClose);
        }
    }
}