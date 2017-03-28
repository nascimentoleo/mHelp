package com.ifma.appmhelp.factories;

import android.content.Context;
import android.util.Log;

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
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.File;
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
        PingManager.getInstanceFor(xmpptcpConnection).setPingInterval(30);
        PingManager.getInstanceFor(xmpptcpConnection).registerPingFailedListener(new PingListener(ctx));

        RosterXMPPController.getInstance().init(xmpptcpConnection);


        // Create the file transfer manager
        final FileTransferManager manager = FileTransferManager.getInstanceFor(xmpptcpConnection);
        // Create the listener
        manager.addFileTransferListener(new FileTransferListener() {
            public void fileTransferRequest(FileTransferRequest request) {
                // Check to see if the request should be accepted
                //if(shouldAccept(request)) {
                    // Accept it
                    IncomingFileTransfer transfer = request.accept();
                try {
                    transfer.recieveFile(new File("shakespeare_complete_works.txt"));
                    Log.d("TRANSFER", "Recebeu");
                } catch (SmackException e) {
                    e.printStackTrace();
                    Log.d("TRANSFER", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("TRANSFER", e.getMessage());
                }
                /*} else {
                    // Reject it
                    request.reject();
                } */
            }
        });

        return xmpptcpConnection.connect();
    }
}
