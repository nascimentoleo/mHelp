package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.lib.JidTranslator;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;


/**
 * Created by leo on 12/20/16.
 */

public class RosterXMPPController{

    private Roster roster;

    public RosterXMPPController() {
        this.roster = Roster.getInstanceFor(ConexaoXMPP.getInstance().getConexao());
        this.roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
    }

    public void sendPresence(Usuario usuario, Presence.Type type) throws SmackException.NotConnectedException {
        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        Presence presence = new Presence(type);
        presence.setTo(jId);
        ConexaoXMPP.getInstance().getConexao().sendStanza(presence);

    }

    public void addRoster(Usuario usuario) throws SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        this.roster.createEntry(jId,usuario.getNome(),null);

    }

    public void removeRoster(Usuario usuario) throws SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        RosterEntry entry = this.roster.getEntry(jId);
        if (entry != null)
            this.roster.removeEntry(entry);
    }

    public boolean rosterIsOnline(Usuario usuario) throws SmackException.NotConnectedException {
       // this.setPresence();
        Presence presence = this.roster.getPresence(usuario.getLogin());
        return presence.getType() == Presence.Type.available;
    }

   /* public void setPresence(){
        this.roster.getEntries();
    } */



}
