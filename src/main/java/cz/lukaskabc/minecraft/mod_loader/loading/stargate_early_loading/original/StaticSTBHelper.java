/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 * Moved for execution on different class loader.
 * All rights belong to the original authors.
 * Changes:
 * - Magnification filter to nearest
 */
package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.opengl.GL32C.*;

public class StaticSTBHelper {
    private static final Logger LOG = LogManager.getLogger();

    private StaticSTBHelper() {
        throw new AssertionError();
    }

    public static ByteBuffer readToBuffer(final InputStream inputStream, int initialCapacity) {
        ByteBuffer buf;
        try (var channel = Channels.newChannel(inputStream)) {
            buf = BufferUtils.createByteBuffer(initialCapacity + 1);
            while (true) {
                var readbytes = channel.read(buf);
                if (readbytes == -1) break;
                if (buf.remaining() == 0) { // extend the buffer by 50%
                    var newBuf = BufferUtils.createByteBuffer(buf.capacity() * 3 / 2);
                    buf.flip();
                    newBuf.put(buf);
                    buf = newBuf;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        buf.flip();
        return MemoryUtil.memSlice(buf); // we trim the final buffer to the size of the content
    }

    /**
     * @return textureId
     */
    public static int resolveAndBindTexture(String file, int[] textureWidth, int[] textureHeight) throws FileNotFoundException {
        int[] lc = new int[1];
        int[] fileSize = new int[]{300000}; // default texture size 30kB

        final InputStream inputStream = loadImageFromClasspath(file, fileSize);
        ByteBuffer buf = readToBuffer(inputStream, fileSize[0]);
        final ByteBuffer img = STBImage.stbi_load_from_memory(buf, textureWidth, textureHeight, lc, 4);

        int texid = glGenTextures();
        bindTexture(img, texid, textureWidth, textureHeight);

        return texid;
    }

    private static void safeLongToInt(int @Nullable [] size, long value) {
        if (size == null) {
            return;
        }
        try {
            size[0] = Math.toIntExact(value);
        } catch (ArithmeticException e) {
            LOG.error("File size is too big: {}. Max integer size is {}", value, Integer.MAX_VALUE);
            throw new InitializationException(e);
        }
    }

    private static void fileSize(int @Nullable [] size, byte[] data) {
        safeLongToInt(size, data.length);
    }

    public static InputStream loadImageFromClasspath(String file, int[] size) throws FileNotFoundException {
        try (InputStream is = ConfigLoader.resolveFile(Paths.get(file))) {
            final byte[] data = Objects.requireNonNull(is).readAllBytes();
            fileSize(size, data);
            return new ByteArrayInputStream(data);
        } catch (IOException | NullPointerException e) {
            LOG.error("Failed to resolve file: {}", file);
            throw new FileNotFoundException("File not found: " + file);
        }
    }

    /**
     * The byte buffer will be freed.
     */
    public static void bindTexture(@Nullable ByteBuffer textureData, int textureId, int[] width, int[] height) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, textureData);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
        MemoryUtil.memFree(textureData);
    }
}
