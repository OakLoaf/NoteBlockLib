/*
 * This file is part of NoteBlockLib - https://github.com/RaphiMC/NoteBlockLib
 * Copyright (C) 2022-2025 RK_01/RaphiMC and contributors
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

import net.raphimc.noteblocklib.util.Instrument;

import java.util.Objects;

public abstract class Note implements Cloneable {

    protected Instrument instrument;
    protected byte key;

    public Note(final Instrument instrument, final byte key) {
        this.instrument = instrument;
        this.key = key;
    }

    /**
     * @return The instrument of the note. Null if the note uses a custom instrument.
     */
    public Instrument getInstrument() {
        return this.instrument;
    }

    /**
     * @param instrument The instrument of the note. Null if the note uses a custom instrument.
     */
    public void setInstrument(final Instrument instrument) {
        this.instrument = instrument;
    }

    /**
     * @return The key of the note, from 0-87, where 0 is A0 and 87 is C8. 33-57 is within the 2-octave limit.
     */
    public byte getKey() {
        return this.key;
    }

    /**
     * @param key The key of the note, from 0-87, where 0 is A0 and 87 is C8. 33-57 is within the 2-octave limit.
     */
    public void setKey(final byte key) {
        this.key = key;
    }

    public byte getRawKey() {
        return this.key;
    }

    public abstract Note clone();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return key == note.key && instrument == note.instrument;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, key);
    }

}
