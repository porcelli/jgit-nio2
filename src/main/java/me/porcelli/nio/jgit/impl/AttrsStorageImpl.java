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

import java.nio.file.attribute.AttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.HashMap;
import java.util.Map;

public class AttrsStorageImpl implements AttrsStorage {

    final Properties content = new Properties();
    final Map<String, AttributeView> viewsNameIndex = new HashMap<String, AttributeView>();
    final Map<Class<?>, AttributeView> viewsTypeIndex = new HashMap<Class<?>, AttributeView>();

    @Override
    public AttrsStorage getAttrStorage() {
        return this;
    }

    @Override
    public <V extends AttributeView> void addAttrView(final V view) {
        viewsNameIndex.put(view.name(),
                           view);
        if (view instanceof ExtendedAttributeView) {
            final ExtendedAttributeView extendedView = (ExtendedAttributeView) view;
            for (Class<? extends BasicFileAttributeView> type : extendedView.viewTypes()) {
                viewsTypeIndex.put(type,
                                   view);
            }
        } else {
            viewsTypeIndex.put(view.getClass(),
                               view);
        }
    }

    @Override
    public <V extends AttributeView> V getAttrView(final Class<V> type) {
        return (V) viewsTypeIndex.get(type);
    }

    @Override
    public <V extends AttributeView> V getAttrView(final String name) {
        return (V) viewsNameIndex.get(name);
    }

    @Override
    public void clear() {
        viewsNameIndex.clear();
        viewsTypeIndex.clear();
        content.clear();
    }
}

