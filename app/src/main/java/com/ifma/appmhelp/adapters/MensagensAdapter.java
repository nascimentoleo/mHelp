package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class MensagensAdapter extends BaseAdapter{

    private Context ctx;
    private List<Mensagem> listaDeMensagens;

    public MensagensAdapter(Context ctx, List<Mensagem> listaDeMensagens) {
        this.listaDeMensagens = listaDeMensagens;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listaDeMensagens.size();
    }

    @Override
    public Mensagem getItem(int position) {
        return listaDeMensagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mensagem mensagem = this.listaDeMensagens.get(position);

        View itemLista = LayoutInflater.from(ctx).inflate(
                R.layout.item_list_mensagens, null);
        MensagemViewHolder holder = createViewHolder(itemLista);

        if (mensagem.getUsuario().equals(UsuarioLogado.getInstance().getUsuario()))
            adicionarMensagemDireita(holder, mensagem);
        else
            adicionarMensagemEsquerda(holder, mensagem);

        holder.txtMessage.setText(mensagem.getMsg());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        if (mensagem.getData() != null)
            holder.txtInfo.setText(sdf.format(mensagem.getData()));
        else
            holder.txtInfo.setText(sdf.format(new Date()));

        return itemLista;
    }


    private void adicionarMensagemEsquerda(MensagemViewHolder holder, Mensagem mensagem){
        holder.contentWithBG.setBackgroundResource(R.drawable.msg_in);

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
        layoutParams.gravity = Gravity.LEFT;
        holder.contentWithBG.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        holder.content.setLayoutParams(lp);
        layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
        layoutParams.gravity = Gravity.LEFT;
        holder.txtMessage.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
        layoutParams.gravity = Gravity.LEFT;
        holder.txtInfo.setLayoutParams(layoutParams);
    }

    private void adicionarMensagemDireita(MensagemViewHolder holder, Mensagem mensagem){
        holder.contentWithBG.setBackgroundResource(R.drawable.msg_out);

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;
        holder.contentWithBG.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.content.setLayoutParams(lp);
        layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;

        holder.txtMessage.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;
        holder.txtInfo.setLayoutParams(layoutParams);

    }


    private MensagemViewHolder createViewHolder(View v) {
        MensagemViewHolder holder = new MensagemViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }

    private static class MensagemViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }



}
