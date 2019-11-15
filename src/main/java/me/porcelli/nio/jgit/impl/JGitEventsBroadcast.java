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

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import me.porcelli.nio.jgit.cluster.ClusterMessageService;

public class JGitEventsBroadcast {

    public static final String DEFAULT_TOPIC = "default-niogit-topic";

    private String nodeId = UUID.randomUUID().toString();
    private Consumer<WatchEventsWrapper> eventsPublisher;
    private final ClusterMessageService clusterMessageService;

    public JGitEventsBroadcast(ClusterMessageService clusterMessageService,
                               Consumer<WatchEventsWrapper> eventsPublisher) {
        this.clusterMessageService = clusterMessageService;
        this.eventsPublisher = eventsPublisher;
        setupJMSConnection();
    }

    private void setupJMSConnection() {
        clusterMessageService.connect();
    }

    public void createWatchService(String topicName) {
        clusterMessageService.createConsumer(
                ClusterMessageService.DestinationType.PubSub,
                getChannelName(topicName),
                WatchEventsWrapper.class,
                (we) -> {
                    if (!we.getNodeId().equals(nodeId)) {
                        eventsPublisher.accept(we);
                    }
                });
    }

    public synchronized void broadcast(String fsName,
                                       Path watchable,
                                       List<WatchEvent<?>> events) {
        clusterMessageService.broadcast(ClusterMessageService.DestinationType.PubSub,
                                        getChannelName(fsName),
                                        new WatchEventsWrapper(nodeId,
                                                               fsName,
                                                               watchable,
                                                               events));
    }

    private String getChannelName(String fsName) {
        String channelName = DEFAULT_TOPIC;
        if (fsName.contains("/")) {
            channelName = fsName.substring(0,
                                           fsName.indexOf("/"));
        }
        return channelName;
    }

    public void close() {
        clusterMessageService.close();
    }
}
