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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.porcelli.nio.jgit.impl.daemon.filter.HiddenBranchRefFilter;
import org.eclipse.jgit.lib.Ref;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class HiddenBranchRefFilterTest {

    private HiddenBranchRefFilter filter;

    @Mock
    private Ref ref;
    private Map<String, Ref> refs;

    @Before
    public void setUp() {

        refs = new HashMap<>();
        refs.put("master",
                 ref);
        refs.put("develop",
                 ref);
        refs.put("PR--from/develop-master",
                 ref);
        refs.put("PR-1--master",
                 ref);
        refs.put("PR-master",
                 ref);
        refs.put("PR-1-from/develop-master",
                 ref);

        filter = new HiddenBranchRefFilter();
    }

    @Test
    public void testHiddenBranchsFiltering() {
        final Map<String, Ref> filteredRefs = filter.filter(refs);
        final Set<Map.Entry<String, Ref>> set = filteredRefs.entrySet();
        assertEquals(5,
                     set.size());
        assertFalse(set.stream().anyMatch(entry -> entry.getKey().equals("PR-1-from/develop-master")));
    }
}
