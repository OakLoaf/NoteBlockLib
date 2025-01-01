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
package net.raphimc.noteblocklib;

import com.google.common.io.ByteStreams;
import net.raphimc.noteblocklib.format.SongFormat;
import net.raphimc.noteblocklib.format.future.FutureParser;
import net.raphimc.noteblocklib.format.mcsp.McSpParser;
import net.raphimc.noteblocklib.format.midi.MidiParser;
import net.raphimc.noteblocklib.format.nbs.NbsParser;
import net.raphimc.noteblocklib.format.nbs.NbsSong;
import net.raphimc.noteblocklib.format.nbs.model.NbsData;
import net.raphimc.noteblocklib.format.nbs.model.NbsHeader;
import net.raphimc.noteblocklib.format.txt.TxtParser;
import net.raphimc.noteblocklib.format.txt.TxtSong;
import net.raphimc.noteblocklib.model.Song;
import net.raphimc.noteblocklib.model.SongView;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.io.Files.getFileExtension;

public class NoteBlockLib {

    public static Song<?, ?, ?> readSong(final File file) throws Exception {
        return readSong(file.toPath());
    }

    public static Song<?, ?, ?> readSong(final Path path) throws Exception {
        return readSong(path, getFormat(path));
    }

    public static Song<?, ?, ?> readSong(final Path path, final SongFormat format) throws Exception {
        return readSong(Files.readAllBytes(path), format, path.getFileName().toString());
    }

    public static Song<?, ?, ?> readSong(final InputStream is, final SongFormat format) throws Exception {
        return readSong(ByteStreams.toByteArray(is), format, null);
    }

    public static Song<?, ?, ?> readSong(final byte[] bytes, final SongFormat format) throws Exception {
        return readSong(bytes, format, null);
    }

    public static Song<?, ?, ?> readSong(final byte[] bytes, final SongFormat format, final String fileName) throws Exception {
        try {
            if (format == null) throw new IllegalArgumentException("Unknown format");

            switch (format) {
                case NBS:
                    return NbsParser.read(bytes, fileName);
                case MCSP:
                    return McSpParser.read(bytes, fileName);
                case TXT:
                    return TxtParser.read(bytes, fileName);
                case FUTURE:
                    return FutureParser.read(bytes, fileName);
                case MIDI:
                    return MidiParser.read(bytes, fileName);
                default:
                    throw new IllegalStateException("Unknown format");
            }
        } catch (Throwable e) {
            throw new Exception("Failed to read song", e);
        }
    }

    public static void writeSong(final Song<?, ?, ?> song, final File file) throws Exception {
        writeSong(song, file.toPath());
    }

    public static void writeSong(final Song<?, ?, ?> song, final Path path) throws Exception {
        Files.write(path, writeSong(song));
    }

    public static void writeSong(final Song<?, ?, ?> song, final OutputStream os) throws Exception {
        os.write(writeSong(song));
    }

    public static byte[] writeSong(final Song<?, ?, ?> song) throws Exception {
        byte[] bytes = null;
        try {
            if (song instanceof NbsSong) {
                bytes = NbsParser.write((NbsSong) song);
            } else if (song instanceof TxtSong) {
                bytes = TxtParser.write((TxtSong) song);
            }
        } catch (Throwable e) {
            throw new Exception("Failed to write song", e);
        }

        if (bytes == null) {
            throw new Exception("Unsupported song type for writing: " + song.getClass().getSimpleName());
        }

        return bytes;
    }

    public static Song<?, ?, ?> createSongFromView(final SongView<?> songView, final SongFormat format) {
        if (format != SongFormat.NBS) {
            throw new IllegalArgumentException("Only NBS is supported for creating songs from views");
        }

        return new NbsSong(null, new NbsHeader(songView), new NbsData(songView));
    }

    public static SongFormat getFormat(final Path path) {
        return SongFormat.getByExtension(getFileExtension(path.getFileName().toString()));
    }

}
