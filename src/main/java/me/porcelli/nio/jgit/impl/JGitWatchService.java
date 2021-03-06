
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
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class JGitWatchService implements WatchService {

    private boolean wsClose = false;

    private final Queue<WatchKey> events = new ConcurrentLinkedQueue<>();
    private final String fsName;
    private Consumer<JGitWatchService> notifyClose;

    public JGitWatchService(String fsName,
                            Consumer<JGitWatchService> notifyClose) {

        this.fsName = fsName;
        this.notifyClose = notifyClose;
    }

    @Override
    public WatchKey poll() throws ClosedWatchServiceException {
        return events.poll();
    }

    @Override
    public WatchKey poll(long timeout,
                         TimeUnit unit) throws ClosedWatchServiceException, InterruptedException {
        return events.poll();
    }

    @Override
    public synchronized WatchKey take() throws ClosedWatchServiceException, InterruptedException {
        while (true) {
            if (wsClose) {
                throw new ClosedWatchServiceException();
            } else if (events.size() > 0) {
                return events.poll();
            } else {
                try {
                    this.wait();
                } catch (final java.lang.InterruptedException e) {
                }
            }
        }
    }

    public boolean isClose() {
        return wsClose;
    }

    @Override
    public synchronized void close() throws IOException {
        wsClose = true;
        notifyAll();
        notifyClose.accept(this);
    }

    synchronized void closeWithoutNotifyParent() {
        wsClose = true;
        notifyAll();
    }

    @Override
    public String toString() {
        return "WatchService{" +
                "FileSystem=" + fsName +
                '}';
    }

    public void publish(WatchKey wk) {
        events.add(wk);
    }
}