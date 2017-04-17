package com.ifma.appmhelp.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

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

    private void carregaComponentes(){
        imgFullScreen = (ImageViewTouch) findViewById(R.id.imgFullScreen);

    }

    private void exibeAnexo(Anexo anexo){
        AnexoController anexoController = new AnexoController(this);
        File fileAnexo = anexoController.carregaAnexo(anexo.getPath());

        if (fileAnexo.exists()){
            if (anexo.getTipoAnexo() == TipoAnexo.IMAGEM){
                Bitmap bitmap = BitmapFactory.decodeFile(fileAnexo.getAbsolutePath());
                this.imgFullScreen.setImageBitmap(bitmap);
                this.imgFullScreen.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
            }
        }else{
            Toast.makeText(this,"Anexo não encontrado",Toast.LENGTH_SHORT).show();
            finish();
        }
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
