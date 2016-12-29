package com.ifma.appmhelp.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPacientesActivity extends AppCompatActivity implements ListPacientesFragment.OnPatientSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pacientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.inicializaAdapter();
    }

    private void inicializaAdapter(){
        ArrayList<Paciente> listaDePacientes = (ArrayList<Paciente>) this.carregaPacientes();
        if(listaDePacientes != null){
            getSupportFragmentManager().beginTransaction().add(R.id.container_list_pacientes,ListPacientesFragment.newInstance(listaDePacientes)).commit();
        }else {
            Toast.makeText(this, "Este médico não possui pacientes adicionados", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private List<Paciente> carregaPacientes(){
        Medico medico = (Medico) UsuarioLogado.getInstance().getModelo();
        try {
            return new MedicoPacienteDao(this).getPacientesByMedico(medico);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar pacientes: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onPatientSelected(Paciente paciente) {
        Paciente novo = paciente;
    }

    @Override
    public void onPatientLongSelected(final Paciente paciente) {
        //Exibe o menu com as opções para o paciente
        final CharSequence opcoes [] = new CharSequence[] {"Remover"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(opcoes[which].equals("Remover"))
                   confirmarRemocaoDePaciente(paciente);
            }
        });

        builder.show();
    }

    private void confirmarRemocaoDePaciente(final Paciente paciente){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setMessage("Deseja excluir o paciente " + paciente.getUsuario().getNome() + " ?");
        builder.setNegativeButton(R.string.resposta_nao,null);
        builder.setPositiveButton(R.string.resposta_sim, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                removerPaciente(paciente);
            }});

        builder.show();
    }

    private void removerPaciente(Paciente paciente){
        try {
            if (SolicitacoesController.removerUsuario(this, paciente)) {
                //Aviso que paciente foi excluido
                Medico medico = (Medico) UsuarioLogado.getInstance().getModelo();
                SolicitacaoRoster solicitacaoRoster = new SolicitacaoRoster(medico.getUsuario(), StatusSolicitacaoRoster.REMOVIDA);
                Mensagem mensagem = new Mensagem(solicitacaoRoster.toJson(), TipoDeMensagem.SOLICITACAO_ROSTER);
                MensagemController.enviaMensagem(paciente.getUsuario(), mensagem);

                Toast.makeText(this, "Paciente removido !", Toast.LENGTH_SHORT).show();
                this.inicializaAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível remover o paciente: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
