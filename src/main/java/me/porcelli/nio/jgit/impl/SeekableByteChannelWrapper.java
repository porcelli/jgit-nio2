/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.porcelli.nio.jgit.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotNull;

/**
 *
 */
public class SeekableByteChannelWrapper implements SeekableByteChannel {

    private final SeekableByteChannel channel;

    public SeekableByteChannelWrapper(final SeekableByteChannel channel) {
        this.channel = checkNotNull("channel",
                                    channel);
    }

    @Override
    public long position() throws IOException {
        return channel.position();
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        return channel.position(newPosition);
    }

    @Override
    public long size() throws IOException {
        return channel.size();
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        return channel.truncate(size);
    }

    @Override
    public int read(final ByteBuffer dst) throws java.io.IOException {
        return channel.read(dst);
    }

    @Override
    public int write(final ByteBuffer src) throws java.io.IOException {
        return channel.write(src);
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws java.io.IOException {
        channel.close();
    }
}
