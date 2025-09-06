package br.com.caminhodasaguas.api.utils;

public class RegexUtils {
    public static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static final String CEP_REGEX = "^\\d{5}-?\\d{3}$";;

    public static final String DOCUMENT_REGEX = "^(?:\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}|\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2})$";

    public static final String ANY_PHONE_REGEX =
            "^\\(?\\d{2}\\)?[\\s-]?(?:9\\d{4}|\\d{4})-?\\d{4}$";
}
