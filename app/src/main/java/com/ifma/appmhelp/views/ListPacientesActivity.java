package com.ifma.appmhelp.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.inicializaAdapter();
    }

    private void inicializaAdapter(){
        ArrayList<Paciente> listaDePacientes = (ArrayList<Paciente>) this.carregaPacientes();
        if(listaDePacientes != null){
            //if (!getSupportFragmentManager().beginTransaction().isEmpty())
                getSupportFragmentManager().beginTransaction().replace(R.id.container_list_pacientes,ListPacientesFragment.newInstance(listaDePacientes)).commit();
            //else
            //     getSupportFragmentManager().beginTransaction().add(R.id.container_list_pacientes,ListPacientesFragment.newInstance(listaDePacientes)).commit();
        }else {
            Toast.makeText(this, "Este médico não possui pacientes adicionados", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private List<Paciente> carregaPacientes(){
        Medico medico = (Medico) UsuarioLogado.getInstance().getModelo();
        if (medico != null){
            try {
                return new MedicoPacienteDao(this).getPacientesByMedico(medico);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar pacientes: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this, "Erro ao carregar pacientes: Médico não está logado",Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onPatientSelected(Paciente paciente) {
        if (paciente.getProntuario() == null || paciente.getProntuario().getId() == null)
            confirmarCadastroDeProntuario(paciente);
        else
            abrirProntuario(paciente);
    }

    private void confirmarCadastroDeProntuario(final Paciente paciente){
        new AlertDialog.Builder(this)
                .setTitle("Novo Prontuário")
                .setMessage("Paciente sem prontuário, deseja cadastrar um novo?")
                .setPositiveButton(R.string.resposta_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cadastrarProntuario(paciente))
                           abrirProntuario(paciente);

                    }
                })
                .setNegativeButton(R.string.resposta_nao,null)
                .show();

    }

    private boolean cadastrarProntuario(Paciente paciente){
        try {
            paciente.setProntuario(new Prontuario());
            new PacienteDao(this).persistir(paciente, true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao cadastrar prontuário: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void abrirProntuario(Paciente paciente){
        Intent it = new Intent(ListPacientesActivity.this, ProntuarioActivity.class);
        it.putExtra(GenericBundleKeys.PACIENTE.toString(),paciente);
        startActivityForResult(it,RESULT_FIRST_USER);
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
        new AlertDialog.Builder(this)
        .setTitle("Confirmação")
        .setMessage("Deseja excluir o paciente " + paciente.getUsuario().getNome() + " ?")
        .setNegativeButton(R.string.resposta_nao,null)
        .setPositiveButton(R.string.resposta_sim, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                removerPaciente(paciente);
            }})

        .show();
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
