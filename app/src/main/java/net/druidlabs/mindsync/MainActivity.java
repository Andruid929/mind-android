package net.druidlabs.mindsync;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
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

import java.util.List;

/**
 * The activity you boot into when you launch the application.
 * This activity is what starts off every single project unless opened by
 * an external application.
 *
 * @since 0.0.1
 * @version 1.0
 * @author Andrew Jones
 * */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout homeDrawerLayout;

    private NavigationView homeNavigationView;

    private MaterialToolbar homeToolbar;

    private GridView notesListGridView;

    private FloatingActionButton expandAddFab;
    private FloatingActionButton addNoteFab;

    private List<Note> notesList;

    private NotesArrayAdapter<Note> notesArrayAdapter;

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

        homeDrawerLayout = findViewById(R.id.main);

        homeNavigationView = findViewById(R.id.home_drawer_nav_view);

        homeToolbar = findViewById(R.id.home_toolbar);

        notesListGridView = findViewById(R.id.home_notes_gridview);

        expandAddFab = findViewById(R.id.home_expand_add_fab);
        addNoteFab = findViewById(R.id.home_add_note_fab);

        Note note1 = new Note("Test Note 1", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");
        Note note2 = new Note("Test Note 2", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");
        Note note3 = new Note("Test Note 3", "This is a test note added to test the UI for aesthetic cohesion if that makes sense");

        notesList = Notes.getNotes();

        TextView devUrlText = homeNavigationView.getHeaderView(0).findViewById(R.id.menu_dev_url);
        devUrlText.setMovementMethod(LinkMovementMethod.getInstance());

        notesArrayAdapter = new NotesArrayAdapter<>(MainActivity.this, R.layout.custom_notes_tiles, notesList);

        notesListGridView.setAdapter(notesArrayAdapter);

        homeNavigationView.bringToFront();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                R.string.home_drawer_open_desc, R.string.home_drawer_close_desc);
        homeDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setSupportActionBar(homeToolbar);

        notesArrayAdapter.notifyDataSetChanged();
    }
}