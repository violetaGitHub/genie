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
package com.netflix.genie.web.security.oauth2.pingfederate;

import com.netflix.genie.GenieWeb;
import com.netflix.genie.test.categories.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for PingFederateConfig.
 *
 * @author tgianos
 * @since 3.0.0
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GenieWeb.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles({"oauth2", "integration"})
public class PingFederateConfigIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.mvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    /**
     * Make sure we can get root.
     *
     * @throws Exception on any error
     */
    @Test
    public void canGetRoot() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Make sure we can't get anything under API if not authenticated.
     *
     * @throws Exception on any error
     */
    @Test
    public void cantGetAPIIfUnauthenticated() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.get("/api/v3/applications"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     * Make sure we can get anything under API if authenticated.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser
    public void canGetRegularAPI() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.get("/api/v3/applications"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Make sure we can't get anything under admin control.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser
    public void canGetAdminAPIAsRegularUser() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.get("/api/v3/applications"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Make sure we can't delete anything under admin control.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser
    public void cantDeleteAdminAPIAsRegularUser() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.delete("/api/v3/applications"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Make sure we get get anything under admin control if we're an admin.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void canCallDeleteAPIAsAdminUser() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.delete("/api/v3/applications"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Make sure we can't delete anything under admin control.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser
    public void cantGetActuatorAPIAsRegularUser() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.get("/actuator/beans"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Make sure we get get anything under admin control if we're an admin.
     *
     * @throws Exception on any error
     */
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void canGetActuatorAPIAsAdminUser() throws Exception {
        this.mvc
            .perform(MockMvcRequestBuilders.get("/actuator/beans"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
