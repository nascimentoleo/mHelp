package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.lib.JidTranslator;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;


/**
 * Created by leo on 12/20/16.
 */

public class RosterXMPPController{

    private static RosterXMPPController instance = null;
    private Roster roster;

    public synchronized static RosterXMPPController getInstance() {
        if(instance ==null)
            instance = new RosterXMPPController();
        return instance;
    }

    public RosterXMPPController() {

    }

    public void init(AbstractXMPPConnection conexao){
        this.roster = Roster.getInstanceFor(conexao);
        this.roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
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
        Presence presence = this.roster.getPresence(JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(), usuario.getLogin()));
        return presence.getType() == Presence.Type.available;
    }

}
