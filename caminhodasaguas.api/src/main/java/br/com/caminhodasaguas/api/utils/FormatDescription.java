package br.com.caminhodasaguas.api.utils;

public class FormatDescription {
    public static String format(String description){
        return description
                .trim()
                .replaceAll("([.!?])(\\s+|$)", "$1\n\n")
                .replaceAll("(\\R){3,}", "\n\n");
    }
}
