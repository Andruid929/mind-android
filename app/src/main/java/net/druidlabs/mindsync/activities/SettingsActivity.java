package net.druidlabs.mindsync.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.preferences.SettingsFragment;
import net.druidlabs.mindsync.util.UiUtil;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UiUtil.setBlackMode(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_layout, new SettingsFragment())
                .commit();

        MaterialToolbar settingsToolbar = findViewById(R.id.settings_toolbar);

        settingsToolbar.setNavigationOnClickListener(v -> finish());
    }
}