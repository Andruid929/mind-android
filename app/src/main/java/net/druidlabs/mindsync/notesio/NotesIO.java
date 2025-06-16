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
 * Class to handle the saving and retrieving of the {@link MainActivity#notesList primary notes list}.
 * <p>This class uses {@link Gson} to {@link #saveNotesToStorage(Context) serialise}
 * and {@link #readTypeFromStorage(Context) deserialise} the {@code List<Note>} that is the notes list.
 * <p>This class was created to allow saving and retrieving of notes outside the {@code MainActivity}.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-alpha.1
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
     * Serialise the {@link MainActivity#notesList notes list} to a gsonString
     * and write it to the app's {@link Context#getFilesDir() data folder}.
     *
     * @param appContext the application context.
     * @see #readTypeFromStorage(Context)
     * @since 0.9.0
     */
    public static void saveNotesToStorage(Context appContext) {
        Gson gson = new Gson();

        File dataFile = new File(appContext.getFilesDir().getPath() + '/' + DATA_FILE_NAME);

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
     * Read the saved data String (if any) and deserialise to get saved notes.
     *
     * @param appContext the application context
     * @return a list of saved notes if any are present in the data folder
     * or a new empty {@link LinkedList notes list} if any error occurs while reading
     * or the data file cannot be found.
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
