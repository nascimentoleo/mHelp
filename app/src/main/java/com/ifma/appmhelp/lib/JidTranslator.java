package com.ifma.appmhelp.lib;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by leo on 12/20/16.
 */

public class JidTranslator {

    public static String getjId(AbstractXMPPConnection conexao, String login){
        return login + "@" + conexao.getServiceName();
    }

    public static String getLogin(String jId){
        return jId.substring(0, jId.indexOf("@"));
    }

}
