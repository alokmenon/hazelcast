/*
 * Copyright (c) 2008-2012, Hazel Bilisim Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.impl;

import com.hazelcast.impl.ClientHandlerService.ClientOperationHandler;
import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Connection;
import com.hazelcast.nio.Packet;

import javax.security.auth.Subject;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;

public class ClientPacketRequestHandler extends ClientRequestHandler {
    private final Packet packet;
    private final ClientHandlerService.ClientOperationHandler clientOperationHandler;

    public ClientPacketRequestHandler(Node node, Packet packet, CallContext callContext,
                                      ClientOperationHandler clientOperationHandler, Subject subject, Connection connection) {

        super(callContext, subject, node, connection);
        this.packet = packet;
        this.clientOperationHandler = clientOperationHandler;
    }

    protected PrivilegedExceptionAction<Void> createAction() {
        return new PrivilegedExceptionAction<Void>() {
                    public Void run() {
                        clientOperationHandler.handle(node, packet);
                        node.clientHandlerService.getClientEndpoint(connection).removeRequest(ClientPacketRequestHandler.this);
                        return null;
                    }
                };
    }
}
