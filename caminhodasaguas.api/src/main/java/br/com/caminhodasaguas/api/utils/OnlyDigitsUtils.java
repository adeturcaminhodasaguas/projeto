package br.com.caminhodasaguas.api.utils;

import java.text.Normalizer;

public class OnlyDigitsUtils {

    public static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

}
