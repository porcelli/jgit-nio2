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

package me.porcelli.nio.jgit.security;

/**
 * AuthenticationService service for authenticating users and getting their roles.
 * @author edewit@redhat.com
 */
public interface AuthenticationService {

    User login(String username, String password);

    /**
     * @return True iff the user is currently logged in.
     */
    boolean isLoggedIn();

    /**
     * Log out the currently authenticated user. Has no effect if there is no current user.
     */
    void logout();

    User getUser();
}
