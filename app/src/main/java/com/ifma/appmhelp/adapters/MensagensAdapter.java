package com.ifma.appmhelp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.AnexoController;
import com.ifma.appmhelp.models.Anexo;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by leo on 1/3/17.
 */

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.RecycleMensagemViewHolder>{

    private Context ctx;
    private List<Mensagem> listaDeMensagens;

    public MensagensAdapter(Context ctx, List<Mensagem> listaDeMensagens) {
        this.listaDeMensagens = listaDeMensagens;
        this.ctx = ctx;
    }

    @Override
    public RecycleMensagemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(ctx).inflate(R.layout.item_list_mensagens, null);
        return new RecycleMensagemViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(RecycleMensagemViewHolder holder, int position) {
        Mensagem mensagem = this.listaDeMensagens.get(position);

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

        if (mensagem.getAnexo() != null)
            this.adicionaAnexo(holder,mensagem.getAnexo());

     }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listaDeMensagens.size();
    }


    private void adicionarMensagemEsquerda(RecycleMensagemViewHolder holder, Mensagem mensagem){
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

    private void adicionarMensagemDireita(RecycleMensagemViewHolder holder, Mensagem mensagem){
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

    private void adicionaAnexo(RecycleMensagemViewHolder holder, Anexo anexo){
        File file = new AnexoController(ctx).carregaAnexo(anexo.getPath());

        if (file != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3; //Reduz o tamanho da imagem em 1/3
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);

            holder.imgAnexo.setImageBitmap(bitmap);
        }else
            holder.imgAnexo.setImageBitmap(null);


    }

    public static class RecycleMensagemViewHolder extends RecyclerView.ViewHolder  {

        private TextView txtMessage;
        private TextView txtInfo;
        private LinearLayout content;
        private LinearLayout contentWithBG;
        private ImageView imgAnexo;

        public RecycleMensagemViewHolder(View itemView) {
            super(itemView);

            txtMessage    = (TextView) itemView.findViewById(R.id.txtMessage);
            content       = (LinearLayout) itemView.findViewById(R.id.content);
            contentWithBG = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
            txtInfo       = (TextView) itemView.findViewById(R.id.txtInfo);
            imgAnexo      = (ImageView) itemView.findViewById(R.id.imgAnexo);
        }
    }



}
