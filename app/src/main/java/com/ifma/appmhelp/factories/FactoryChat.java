package com.ifma.appmhelp.factories;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;

/**
 * Created by leo on 11/14/16.
 */
public class FactoryChat {

    public static Chat novoChat(String jId, AbstractXMPPConnection conexao){
        ChatManager chatManager = ChatManager.getInstanceFor(conexao);
        return chatManager.createChat(jId + "@" + conexao.getServiceName());
    }


}
