package com.ifma.appmhelp.factories;

import android.content.Context;

import com.ifma.appmhelp.controls.RosterXMPPController;
import com.ifma.appmhelp.services.ConexaoXMPPListener;
import com.ifma.appmhelp.services.PingListener;
import com.ifma.appmhelp.services.StanzaXMPPListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;

/**
 * Created by leo on 11/3/16.
 */
public class FactoryConexaoXMPP {

    public static AbstractXMPPConnection getConexao(Context ctx, String host, int porta) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration config;

        config = XMPPTCPConnectionConfiguration.builder()
                .setCompressionEnabled(true) //Comprime as mensagens antes de enviar
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) //Desativa modo de segurança
                .setServiceName("mhelp-server-xmpp")
                .setHost(host) //Ip do servidor
                .setPort(porta) //Porta padrão 5222
                .build();
        XMPPTCPConnection xmppConnection = new XMPPTCPConnection(config);

        //Define um objeto para controlar a conexão
        xmppConnection.addConnectionListener(new ConexaoXMPPListener(ctx));

        //Define um objeto para controlar as mensagens que chegam
        xmppConnection.addAsyncStanzaListener(new StanzaXMPPListener(ctx), new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                return true;
            }
        });

        xmppConnection.setPacketReplyTimeout(10000); //Tempo de reconexão
        //Registra o ping para habilitar reconexão
        PingManager.getInstanceFor(xmppConnection).setPingInterval(30);
        PingManager.getInstanceFor(xmppConnection).registerPingFailedListener(new PingListener(ctx));

        RosterXMPPController.getInstance().init(xmppConnection);

        return xmppConnection.connect();
    }
}
