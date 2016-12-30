package com.ifma.appmhelp.lib;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by leo on 12/30/16.
 */

public class DataLib {

    public static int calulaIdade(Date dataDeNascimento) {
        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(dataDeNascimento);
        Calendar hoje = Calendar.getInstance();

        int idade = hoje.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);

        if (hoje.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH))
            idade--;
        else
            if (hoje.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH)
             && hoje.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH))
                idade--;

        return idade;
    }

    public static boolean validarDataDeNascimento(String data){
        Date dataParaValidar = converterData(data);
        if (dataParaValidar == null)
            return false;

        if (!dataMaiorQueAtual(dataParaValidar))
                return true;

        return false;

    }

    public static Date converterData(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(data);
        } catch(ParseException e) {
            return null;
        }
    }

    public static String converterData(Date data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        return sdf.format(data);
    }

    public static boolean dataMaiorQueAtual(Date data){
        Calendar hoje = Calendar.getInstance();
        Calendar dataParaValidar = Calendar.getInstance();
        dataParaValidar.setTime(data);
        return hoje.before(dataParaValidar);
    }
}
