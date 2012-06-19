/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

package org.wso2.carbon.eventbridge.agent.thrift.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.eventbridge.agent.thrift.Agent;
import org.wso2.carbon.eventbridge.agent.thrift.conf.AgentConfiguration;
import org.wso2.carbon.eventbridge.agent.thrift.internal.utils.AgentBuilder;

/**
 * @scr.component name="agentservice.component" immediate="true"
 */
public class AgentDS {
    private static Log log = LogFactory.getLog(AgentDS.class);
    private Agent agent;
    private ServiceRegistration serviceRegistration;

    /**
     * initialize the agent here.
     *
     * @param context
     */
    protected void activate(ComponentContext context) {
        if (agent == null) {
            AgentConfiguration agentConfiguration = new AgentConfiguration();
            agent = new Agent(AgentBuilder.loadAgentConfiguration(agentConfiguration));
            serviceRegistration = context.getBundleContext().
                    registerService(Agent.class.getName(), agent, null);
            log.info("Successfully deployed Agent Client");
        }
    }


    protected void deactivate(ComponentContext context) {
        context.getBundleContext().ungetService(serviceRegistration.getReference());
        agent.shutdown();
        if (log.isDebugEnabled()) {
            log.debug("Successfully stopped agent");
        }
    }

}