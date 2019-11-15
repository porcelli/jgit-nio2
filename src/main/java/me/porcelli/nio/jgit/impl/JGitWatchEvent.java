
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

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import me.porcelli.nio.jgit.fs.WatchContext;
import org.eclipse.jgit.diff.DiffEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JGitWatchEvent implements WatchEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JGitWatchEvent.class);
    public static final Kind<Path> ENTRY_RENAME = new Kind<Path>() {
        @Override
        public String name() {
            return "ENTRY_RENAME";
        }

        @Override
        public Class<Path> type() {
            return Path.class;
        }
    };

    private final URI oldPath;
    private final URI newPath;
    private final String sessionId;
    private final String userName;
    private final String message;
    private final String changeType;

    public JGitWatchEvent(String sessionId,
                          String userName,
                          String message,
                          String changeType,
                          Path oldPath,
                          Path newPath) {

        this.sessionId = sessionId;
        this.userName = userName;
        this.message = message;
        this.changeType = changeType;
        this.oldPath = oldPath != null ? oldPath.toUri() : null;
        this.newPath = newPath != null ? newPath.toUri() : null;
    }

    @Override
    public WatchEvent.Kind kind() {
        DiffEntry.ChangeType changeType = DiffEntry.ChangeType.valueOf(this.changeType);
        switch (changeType) {
            case ADD:
            case COPY:
                return StandardWatchEventKinds.ENTRY_CREATE;
            case DELETE:
                return StandardWatchEventKinds.ENTRY_DELETE;
            case MODIFY:
                return StandardWatchEventKinds.ENTRY_MODIFY;
            case RENAME:
                return ENTRY_RENAME;
            default:
                throw new RuntimeException("Unsupported change type: " + changeType);
        }
    }

    @Override
    public int count() {
        return 1;
    }

    @Override
    public Object context() {
        return new WatchContext() {

            @Override
            public Path getPath() {
                return newPath != null ? lookup(newPath) : null;
            }

            @Override
            public Path getOldPath() {
                return oldPath != null ? lookup(oldPath) : null;
            }

            private Path lookup(URI uri) {
                Path path = null;
                try {
                    path = Paths.get(uri);
                } catch (Exception e) {
                    LOGGER.error("Error trying to translate to path uri: " + uri);
                }
                return path;
            }

            @Override
            public String getSessionId() {
                return sessionId;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public String getUser() {
                return userName;
            }
        };
    }

    @Override
    public String toString() {
        return "WatchEvent{" +
                "newPath=" + newPath +
                ", oldPath=" + oldPath +
                ", sessionId='" + sessionId + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", changeType=" + changeType +
                '}';
    }
}
