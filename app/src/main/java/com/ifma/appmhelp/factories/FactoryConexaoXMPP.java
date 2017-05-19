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
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setDebuggerEnabled(true) //Ativa debug das mensagens
                .setCompressionEnabled(true) //Comprime as mensagens antes de enviar
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) //Desativa modo de segurança
                .setServiceName("mhelp-server-xmpp")
                .setHost(host) //Ip do servidor
                .setPort(porta) //Porta padrão 5222
                .build();
        XMPPTCPConnection xmpptcpConnection = new XMPPTCPConnection(config);

        //Define um objeto para controlar a conexão
        xmpptcpConnection.addConnectionListener(new ConexaoXMPPListener(ctx));

        //Define um objeto para controlar as mensagens que chegam
        xmpptcpConnection.addAsyncStanzaListener(new StanzaXMPPListener(ctx), new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                return true;
            }
        });

        xmpptcpConnection.setPacketReplyTimeout(10000); //Tempo de reconexão
        //Registra o ping para habilitar reconexão
        PingManager.getInstanceFor(xmpptcpConnection).setPingInterval(30);
        PingManager.getInstanceFor(xmpptcpConnection).registerPingFailedListener(new PingListener(ctx));

        RosterXMPPController.getInstance().init(xmpptcpConnection);

        return xmpptcpConnection.connect();
    }
}
