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

package me.porcelli.nio.jgit.impl.daemon.git;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import me.porcelli.nio.jgit.impl.ExecutorServiceProducer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DaemonTest {

    @Test
    public void testShutdownByStop() throws Exception {
        final ExecutorService executorService = new ExecutorServiceProducer().produceUnmanagedExecutorService();

        Daemon d = new Daemon(null,
                              executorService);
        d.start();
        assertTrue(d.isRunning());

        d.stop();

        assertFalse(d.isRunning());
    }

    @Test
    public void testShutdownByThreadPoolTermination() throws Exception {
        final ExecutorService execService = new ExecutorServiceProducer().produceUnmanagedExecutorService();
        Daemon d = new Daemon(null,
                              execService);
        d.start();
        assertTrue(d.isRunning());

        execService.shutdownNow();
        execService.awaitTermination(10,
                                     TimeUnit.SECONDS);

        assertFalse(d.isRunning());
    }
}