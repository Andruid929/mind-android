package net.druidlabs.mindsync.notesio;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.druidlabs.mindsync.MainActivity;
import net.druidlabs.mindsync.notes.Note;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to handle the saving and retrieving of notes.
 */

public final class NotesIO {

    /**
     * Logcat tag for note saving and retrieving.
     */
    private static final String NOTES_IO_TAG = "Notes I/O";

    /**
     * File name of the file where the app saves the notes list.
     */

    public static final String DATA_FILE_NAME = "MNDDTR.mnd";

    private NotesIO() {
    }

    /**
     * Save the given {@code data} to the documents folder.
     * This uses Google {@link Gson} to serialise the data to a {@code} String and writes it
     * to the file whose name is specified by {@code fileName}.
     *
     * @see #readTypeFromStorage(Context)
     * @since 0.9.0
     */
    public static void saveNotesToStorage(Context appContext) {
        Gson gson = new Gson();

        File dataFile = new File(appContext.getFilesDir().getPath() + '/' + DATA_FILE_NAME);

        Log.d("Storage path", dataFile.getAbsolutePath());

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

            gson.toJson(MainActivity.notesList, new TypeToken<List<Note>>() {
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
     * @see #saveNotesToStorage(Context)
     * @since 0.9.0
     */

    public static List<Note> readTypeFromStorage(Context appContext) {
        Gson gson = new Gson();

        File dataFile = new File(appContext.getFilesDir().getPath() + '/' + DATA_FILE_NAME);

        if (!dataFile.exists()) {
            return new LinkedList<>();
        }

        try (FileReader fileReader = new FileReader(dataFile)) {
            BufferedReader reader = new BufferedReader(fileReader);

            return gson.fromJson(reader, new TypeToken<List<Note>>() {
            }.getType());

        } catch (IOException | JsonSyntaxException e) {
            return new LinkedList<>();
        }
    }

}
