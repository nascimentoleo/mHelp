package com.ifma.appmhelp.factories;

import android.content.Context;

import com.ifma.appmhelp.services.ConexaoXMPPListener;
import com.ifma.appmhelp.services.StanzaXMPPListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by leo on 11/3/16.
 */
public class FactoryConexaoXMPP {

    public static AbstractXMPPConnection getConexao(Context ctx, String host, int porta) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setDebuggerEnabled(true)
                .setCompressionEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                //.setSocketFactory(new DummySSLSocketFactory())
                .setServiceName("mhelp-server-xmpp")
                .setHost(host)
                .setPort(porta)
                .build();
        XMPPTCPConnection xmpptcpConnection = new XMPPTCPConnection(config);
        xmpptcpConnection.setPacketReplyTimeout(10000);
        xmpptcpConnection.addConnectionListener(new ConexaoXMPPListener(ctx));
        xmpptcpConnection.addAsyncStanzaListener(new StanzaXMPPListener(ctx), new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                return true;
            }
        });

        //Registra o ping para habilitar reconexão
       // PingManager.getInstanceFor(xmpptcpConnection);
       // PingManager.getInstanceFor(xmpptcpConnection).setPingInterval(600);

        return xmpptcpConnection.connect();
    }
}
