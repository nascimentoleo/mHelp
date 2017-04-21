package com.ifma.appmhelp.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.AnexoController;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.TipoAnexo;
import com.ifma.appmhelp.models.Anexo;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;


public class AnexoActivity extends AppCompatActivity {

    private ImageViewTouch imgFullScreen;
    private VideoView videoFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anexo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        carregaComponentes();
     }

    @Override
    protected void onStart() {
        super.onStart();

        Anexo anexo = (Anexo) getIntent().getSerializableExtra(GenericBundleKeys.ANEXO.toString());
        exibeAnexo(anexo);

    }

    @Override
    protected void onStop() {
        if (this.videoFullScreen.isPlaying())
            this.videoFullScreen.pause();

        super.onStop();

    }

    private void carregaComponentes(){
        imgFullScreen   = (ImageViewTouch) findViewById(R.id.imgFullScreen);
        videoFullScreen = (VideoView) findViewById(R.id.videoFullScreen);

    }

    private void exibeAnexo(Anexo anexo){
        AnexoController anexoController = new AnexoController(this);
        File fileAnexo = anexoController.carregaAnexo(anexo.getPath());

        if (fileAnexo.exists()){
            if (anexo.getTipoAnexo() == TipoAnexo.IMAGEM)
                this.exibeImagem(fileAnexo);
            else
                this.exibeVideo(fileAnexo);
        }else{
            Toast.makeText(this,"Anexo não encontrado",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void exibeImagem(File file){
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        this.imgFullScreen.setImageBitmap(bitmap);
        this.imgFullScreen.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        this.imgFullScreen.setVisibility(View.VISIBLE);
    }

    private void exibeVideo(final File file){
        MediaController mediaControls = new MediaController(this);
        this.videoFullScreen.setMediaController(mediaControls);
        this.videoFullScreen.setVisibility(View.VISIBLE);
        this.videoFullScreen.setVideoURI(Uri.fromFile(file));

        this.videoFullScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoFullScreen.start();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
