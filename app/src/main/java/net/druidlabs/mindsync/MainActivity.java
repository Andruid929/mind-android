package net.druidlabs.mindsync;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
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

        notesList = Notes.getNotes();
    }
}