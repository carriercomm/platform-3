/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.bpel.ui.bpel2svg.latest.wso2.adapter;

import org.apache.axis2.context.ConfigurationContext;

public final class AuthenticationManager {
    private static String cookie = null;

    private static String backendServerURL = null;

    private static ConfigurationContext configContext = null;

    private AuthenticationManager() {
    }

    public static void init(String backendServerURL, String cookie, ConfigurationContext configContext) {
        setBackendServerURL(backendServerURL);
        setCookie(cookie);
        setConfigContext(configContext);
    }

    private static void setConfigContext(ConfigurationContext configContext) {
        AuthenticationManager.configContext = configContext;
    }

    public static ConfigurationContext getConfigContext() {
        return AuthenticationManager.configContext;
    }

    public static String getBackendServerURL() {
        return AuthenticationManager.backendServerURL;
    }

    private static void setBackendServerURL(String backendServerURL) {
        AuthenticationManager.backendServerURL = backendServerURL;
    }

    private static void setCookie(String cookie) {
        AuthenticationManager.cookie = cookie;
    }

    public static String getCookie() {
        return AuthenticationManager.cookie;
    }
}
