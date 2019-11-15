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

import java.net.ServerSocket;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

public class PortUtilTest {

    @Test
    public void testValidateOrGetNewWithPreferredNotAvailable() {
        int result1 = PortUtil.validateOrGetNew(0);

        try (ServerSocket ss = new ServerSocket(result1)) {
            int result = PortUtil.validateOrGetNew(result1);
            assertThat(result).isNotEqualTo(result1);
        } catch (Exception x) {
            fail("Port allocation should have work!");
        }
    }

    @Test
    public void testValidateOrGetNewWithZero() {
        int result1 = PortUtil.validateOrGetNew(0);

        assertThat(result1).isNotEqualTo(0);
    }

    @Test
    public void testValidateOrGetNewWithAvailablePreferredPort() {
        int result = PortUtil.validateOrGetNew(0);
        int result2 = PortUtil.validateOrGetNew(result);

        assertThat(result2).isEqualTo(result);
    }
}
