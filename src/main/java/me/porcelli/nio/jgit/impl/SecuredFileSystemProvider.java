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

import java.nio.file.spi.FileSystemProvider;

import me.porcelli.nio.jgit.security.AuthenticationService;
import me.porcelli.nio.jgit.security.FileSystemAuthorization;
import me.porcelli.nio.jgit.security.PublicKeyAuthenticator;

/**
 * Specialization of {@link FileSystemProvider} for file systems that require username/password authentication and
 * support authorization of certain actions.
 */
public abstract class SecuredFileSystemProvider extends FileSystemProvider {

    /**
     * Sets the authenticator that decides which username/password pairs are valid for the file systems managed by this
     * provider.
     * @param authenticator The authenticator to use. Must not be null.
     */
    public abstract void setJAASAuthenticator(final AuthenticationService authenticator);

    public abstract void setHTTPAuthenticator(final AuthenticationService authenticator);

    public abstract void setSSHAuthenticator(final PublicKeyAuthenticator authenticator);

    public abstract void setAuthorizer(final FileSystemAuthorization authorizer);
}
