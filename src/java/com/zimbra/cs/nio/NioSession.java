/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.nio;

import javax.security.sasl.SaslServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public interface NioSession {
    NioServer getServer();
    InetSocketAddress getRemoteAddress();
    OutputStream getOutputStream();
    void startTls() throws IOException;
    void setMaxIdleSeconds(int secs);
    void startSasl(SaslServer sasl) throws IOException;
    void send(ByteBuffer bb) throws IOException;
    boolean drainWriteQueue(int threshold, long timeout);
    int getScheduledWriteBytes();
    void close();
    boolean isClosed();
}
