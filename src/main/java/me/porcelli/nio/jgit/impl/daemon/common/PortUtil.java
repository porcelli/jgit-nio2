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

package me.porcelli.nio.jgit.impl.daemon.common;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortUtil {

    private static final String ERROR_MESSAGE = "Error trying to find a free port.";
    private static final Logger LOG = LoggerFactory.getLogger(PortUtil.class);

    public static int validateOrGetNew(int preferredPort) {
        if (preferredPort == 0 || isPortInUse(preferredPort)) {
            if (preferredPort != 0) {
                LOG.warn("Port {} already in use, system will automatically look for a new one.", preferredPort);
            }
            try (ServerSocket ss = new ServerSocket(0)) {
                return ss.getLocalPort();
            } catch (IOException e) {
                LOG.error(ERROR_MESSAGE, e);
                throw new RuntimeException(ERROR_MESSAGE);
            }
        }
        return preferredPort;
    }

    private static boolean isPortInUse(int port) {
        try (final ServerSocket ss = new ServerSocket(port)) {
            return false;
        } catch (Exception e) {
        }
        return true;
    }
}
