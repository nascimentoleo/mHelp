package com.ifma.appmhelp.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverDisconnect extends BroadcastReceiver {

    public ReceiverDisconnect() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Finaliza o serviço e a aplicação
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ServiceNotification.ID_NOTIFICATION);
        context.stopService(new Intent(context,ConexaoXMPPService.class));
    }

}
