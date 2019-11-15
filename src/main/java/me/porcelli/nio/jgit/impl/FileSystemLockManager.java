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

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FileSystemLockManager {

    final Map<String, FileSystemLock> fileSystemsLocks = new ConcurrentHashMap<>();

    private static class LazyHolder {

        static final FileSystemLockManager INSTANCE = new FileSystemLockManager();
    }

    public static FileSystemLockManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public FileSystemLock getFileSystemLock(File directory,
                                            String lockName,
                                            TimeUnit lastAccessTimeUnit,
                                            long lastAccessThreshold) {

        return fileSystemsLocks.computeIfAbsent(directory.getAbsolutePath(),
                                                key -> new FileSystemLock(directory,
                                                                          lockName,
                                                                          lastAccessTimeUnit,
                                                                          lastAccessThreshold));
    }
}
