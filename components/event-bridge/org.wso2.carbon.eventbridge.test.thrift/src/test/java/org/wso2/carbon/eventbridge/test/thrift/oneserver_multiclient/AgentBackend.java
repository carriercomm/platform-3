/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.eventbridge.test.thrift.oneserver_multiclient;

import org.wso2.carbon.eventbridge.commons.Credentials;
import org.wso2.carbon.eventbridge.commons.Event;
import org.wso2.carbon.eventbridge.commons.EventStreamDefinition;
import org.wso2.carbon.eventbridge.core.AgentCallback;
import org.wso2.carbon.eventbridge.core.EventBridge;
import org.wso2.carbon.eventbridge.core.definitionstore.InMemoryStreamDefinitionStore;
import org.wso2.carbon.eventbridge.core.exception.EventBridgeException;
import org.wso2.carbon.eventbridge.core.internal.authentication.AuthenticationHandler;
import org.wso2.carbon.eventbridge.receiver.thrift.conf.ThriftEventReceiverConfiguration;
import org.wso2.carbon.eventbridge.receiver.thrift.internal.ThriftEventReceiver;
import org.wso2.carbon.eventbridge.test.thrift.KeyStoreUtil;

import java.util.List;


/**
 * Server of multiple client single server test
 */
public class AgentBackend {
    ThriftEventReceiver thriftEventReceiver;
    static int NO_OF_EVENTS = 100000;
    static int STABLE = 1000000;
    int offset = 0;

    public static void main(String[] args)
            throws EventBridgeException, InterruptedException {

        if (0 != args.length && args[0] != null) {
            NO_OF_EVENTS = Integer.parseInt(args[0]);
        }

        System.out.println("Event no=" + NO_OF_EVENTS);

        KeyStoreUtil.setKeyStoreParams();

        AgentBackend server = new AgentBackend(0);
        server.start();
    }

    public AgentBackend(int offset) {
        this.offset = offset;
    }

    public void start() throws EventBridgeException, InterruptedException {

        ThriftEventReceiverConfiguration thriftEventReceiverConfiguration = generateServerConf(offset);
        EventBridge eventBridge =new EventBridge(new AuthenticationHandler() {
            @Override
            public boolean authenticate(String userName,
                                        String password) {
                return true;// allays authenticate to true

            }
        }, new InMemoryStreamDefinitionStore());
        thriftEventReceiver = new ThriftEventReceiver(thriftEventReceiverConfiguration, eventBridge);
        eventBridge.subscribe(assignAgentCallback());
        thriftEventReceiver.start("localhost");

    }

    private AgentCallback assignAgentCallback() {

        return new AgentCallback() {
            long startTime = -1;
            int size = 0;
            private EventStreamDefinition eventStreamDefinition;

            public void definedEventStream(EventStreamDefinition eventStreamDefinition,
                                           Credentials credentials) {
                this.eventStreamDefinition = eventStreamDefinition;//not used here
            }

            @Override
            public void receive(List<Event> eventList, Credentials credentials) {
                addCount(eventList);
                if (size <= STABLE && size > STABLE - 200) {
                    startTime = System.currentTimeMillis();
                }
                if (NO_OF_EVENTS <= (size - STABLE)) {
                    if (startTime != -1) {
                        System.out.println("Total time in ms=" + (System.currentTimeMillis() - startTime));
                    } else {
                        System.out.println("Start time not set ");
                    }
                }
            }

            private synchronized void addCount(List<Event> eventList) {
                size += eventList.size();
            }
        };
    }

    private ThriftEventReceiverConfiguration generateServerConf(int offset) {
        return new ThriftEventReceiverConfiguration(7711 + offset, 7611 + offset);
    }

    public void stop() {
        thriftEventReceiver.stop();
    }
}