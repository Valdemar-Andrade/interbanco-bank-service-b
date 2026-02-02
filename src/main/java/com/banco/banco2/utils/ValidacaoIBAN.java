package com.banco.banco2.utils;

import java.util.regex.Pattern;

public class ValidacaoIBAN {

    //validar o formato do iban xxxx.xxxx
    public static boolean validarIBAN(String iban) {

        // Regex para validar o formato xxxx.xxxx (4 dígitos, um ponto, 4 dígitos)
        String regex = "^\\d{4}\\.\\d{4}$";

        // Compila a regex em um pattern
        Pattern pattern = Pattern.compile(regex);

        // Verifica se o IBAN corresponde ao padrão
        boolean padraoValido = pattern.matcher(iban).matches();

        return padraoValido;
    }

    // Função para verificar se os primeiros 4 dígitos são "0050" que representa este banco
    public static boolean verificarPrefixoIBAN(String iban) {
        return iban.startsWith("0050");
    }

    public static String getCAF(String ibanDestino) {
        return ibanDestino.substring(0, 4);
    }

}
