package br.com.caminhodasaguas.api.utils;

import java.security.SecureRandom;

public class CodeGeneratorUtils {
    public static String generator(){
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}