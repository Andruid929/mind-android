package net.druidlabs.mindsync.notes;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for getting {@link Note} collections.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.1.0
 */

public final class Notes {

    /**
     * At its core, this app is powered by a {@link LinkedList}.
     */

    static final List<Note> notes = new LinkedList<>();

    private Notes() {
    }

    /**
     * Default way the notes are handled, sorted following insertion order.
     *
     * @return all initialised notes that aren't test notes.
     */

    public static List<Note> getNotes() {
        return notes;
    }

    /**
     * If insertion order is not important,
     * this method returns all notes not following any specific order.
     *
     * @return all initialised notes unsorted.
     */

    public static Set<Note> getNotesSet() {
        return new HashSet<>(notes);
    }

    /**
     * Populate an existing {@link Collection Collection&lt;Note&gt;} and return it.
     *
     * @param collection an object of a class that implements a {@link Collection} of type {@code Note}.
     * @return the specified {@code collection} populated with all initialised notes.
     */

    public static <N extends Collection<Note>> N getNoteCollection(N collection) {
        collection.addAll(notes);
        return collection;
    }
}
