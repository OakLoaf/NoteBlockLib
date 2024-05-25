/*
 * This file is part of NoteBlockLib - https://github.com/RaphiMC/NoteBlockLib
 * Copyright (C) 2022-2024 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.noteblocklib.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class SongView<N extends Note> implements Cloneable {

    private String title;
    private int length;
    private float speed;
    private Map<Integer, List<N>> notes;

    public SongView(final String title, final float speed, final Map<Integer, List<N>> notes) {
        this.title = title;
        this.speed = speed;
        this.notes = notes;

        this.recalculateLength();
    }

    private SongView(final String title, final float speed, final int length, final Map<Integer, List<N>> notes) {
        this.title = title;
        this.speed = speed;
        this.length = length;
        this.notes = notes;
    }

    /**
     * @return The title of the song
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return The length of the song, measured in ticks.
     */
    public int getLength() {
        return this.length;
    }

    public void recalculateLength() {
        this.length = this.notes.keySet().stream().mapToInt(i -> i).max().orElse(-1) + 1;
    }

    /**
     * @return The tempo of the song, measured in ticks per second.
     */
    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    public List<N> getNotesAtTick(final int tick) {
        return this.notes.getOrDefault(tick, Collections.emptyList());
    }

    public void setNotesAtTick(final int tick, final List<N> notes) {
        this.notes.put(tick, notes);
    }

    public Map<Integer, List<N>> getNotes() {
        return this.notes;
    }

    public void setNotes(final Map<Integer, List<N>> notes) {
        this.notes = notes;
    }

    @Override
    public SongView<N> clone() {
        final Map<Integer, List<N>> notes = this.notes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .map(n -> (N) n.clone())
                        .collect(Collectors.toList()), throwingMerger(), TreeMap::new)
                );
        return new SongView<>(this.title, this.speed, this.length, notes);
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
    }

}
