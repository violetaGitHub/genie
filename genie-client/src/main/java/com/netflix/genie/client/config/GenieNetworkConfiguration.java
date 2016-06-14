/*
 *
 *  Copyright 2016 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.client.config;

import lombok.Getter;
import lombok.Setter;

/**
 * An object that encapsulates network configurations for Genie client HTTP requests.
 *
 * @author amsharma
 * @since 3.0.0
 */
@Getter
@Setter
public class GenieNetworkConfiguration {

    // The default read timeout for new connections.
    private long readTimeout;

    // The default write timeout for new connections.
    private long writeTimeout;

    // Default connection timeout in milliseconds for new connections.
    private long connectTimeout;

    // Decides if genie should retry or not when a connectivity problem is encountered.
    private boolean retryOnConnectionFailure;
}