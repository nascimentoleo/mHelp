package com.ifma.appmhelp.factories;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by leo on 11/3/16.
 */
public class FactoryConexaoXMPP {

    public static AbstractXMPPConnection getConexao(String host, int porta) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setDebuggerEnabled(true)
                .setCompressionEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
                .setServiceName("mhelp-server-xmpp")
                .setHost(host)
                .setPort(porta)
                .build();
        return new XMPPTCPConnection(config).connect();
    }
}
