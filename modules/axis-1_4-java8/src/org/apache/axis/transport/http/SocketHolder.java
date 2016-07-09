/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.apache.axis.transport.http;

import java.net.Socket;

/**
 * hold a Socket.
 * @author Davanum Srinivas (dims@yahoo.com)
 */
public class SocketHolder {
    /** Field value           */
    private Socket value = null;

    public SocketHolder(Socket value) {
        this.value = value;
    }

    public Socket getSocket() {
        return value;
    }

    public void setSocket(Socket value) {
        this.value = value;
    }
}
