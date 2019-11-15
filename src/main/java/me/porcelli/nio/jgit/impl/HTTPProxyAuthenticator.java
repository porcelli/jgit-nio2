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

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class HTTPProxyAuthenticator extends Authenticator {

    private final String httpProxyUser;
    private final String httpProxyPassword;
    private final String httpsProxyUser;
    private final String httpsProxyPassword;

    public HTTPProxyAuthenticator(final String httpProxyUser,
                                  final String httpProxyPassword,
                                  final String httpsProxyUser,
                                  final String httpsProxyPassword) {
        this.httpProxyUser = httpProxyUser;
        this.httpProxyPassword = httpProxyPassword;
        this.httpsProxyUser = httpsProxyUser;
        this.httpsProxyPassword = httpsProxyPassword;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType() == RequestorType.PROXY) {
            final String protocol = getRequestingProtocol();

            if (protocol.equalsIgnoreCase("http") && (httpProxyUser != null && httpProxyPassword != null)) {
                return new PasswordAuthentication(httpProxyUser,
                                                  httpProxyPassword.toCharArray());
            } else if (protocol.equalsIgnoreCase("https") && (httpsProxyUser != null && httpsProxyPassword != null)) {
                return new PasswordAuthentication(httpsProxyUser,
                                                  httpsProxyPassword.toCharArray());
            }
        }
        return super.getPasswordAuthentication();
    }
}
