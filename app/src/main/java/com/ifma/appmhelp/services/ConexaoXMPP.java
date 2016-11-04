package com.ifma.appmhelp.services;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by leo on 11/3/16.
 */
public class ConexaoXMPP {

    public AbstractXMPPConnection conectar() throws IOException, XMPPException, SmackException {

        // Create a connection to the jabber.org server on a specific port.
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("usuario", "senha")
                .setServiceName("mHelp-api")
                .setHost("192.168.0.4")
                .setPort(5222)
                .build();

        AbstractXMPPConnection conn = new XMPPTCPConnection(config);
        conn.connect();

        return conn;


    }

}
