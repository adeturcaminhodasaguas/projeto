package br.com.caminhodasaguas.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationValueUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(RegexUtils.EMAIL_REGEX);
    private static final Pattern CEP_PATTERN = Pattern.compile(RegexUtils.CEP_REGEX);
    private static final Pattern DOCUMENT_PATTERN = Pattern.compile(RegexUtils.DOCUMENT_REGEX);
    private static final Pattern ANY_PHONE_PATTERN = Pattern.compile(RegexUtils.ANY_PHONE_REGEX);

    public static boolean isInteger(Integer integer) {
        return integer != null && integer.compareTo(0) > 0;
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidCep(String cep){
        return CEP_PATTERN.matcher(cep).matches();
    }

    public static boolean isValidAnyPhone(String phone) {
        return ANY_PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isLengthValid(List<MultipartFile> file, int MAX_SIZE) {
        return file.size() < MAX_SIZE;
    }

    public static boolean isValidDocument(String document) {
        if (!DOCUMENT_PATTERN.matcher(document).matches()) {
            return false;
        }

        String numeric = OnlyDigitsUtils.normalize(document);

        if (numeric.length() == 11) {
            return isValidCPF(numeric);
        } else if (numeric.length() == 14) {
            return isValidCNPJ(numeric);
        }
        return false;
    }

    private static boolean isValidCPF(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int sm, i, r, num, peso;
            char dig10, dig11;

            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = cpf.charAt(i) - 48;
                sm += (num * peso--);
            }
            r = 11 - (sm % 11);
            dig10 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = cpf.charAt(i) - 48;
                sm += (num * peso--);
            }
            r = 11 - (sm % 11);
            dig11 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
        } catch (InputMismatchException e) {
            return false;
        }
    }

    private static boolean isValidCNPJ(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int sm, i, r, num, peso;
            char dig13, dig14;

            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                num = cnpj.charAt(i) - 48;
                sm += (num * peso);
                peso = (peso == 9) ? 2 : peso + 1;
            }
            r = sm % 11;
            dig13 = (r < 2) ? '0' : (char) ((11 - r) + 48);

            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = cnpj.charAt(i) - 48;
                sm += (num * peso);
                peso = (peso == 9) ? 2 : peso + 1;
            }
            r = sm % 11;
            dig14 = (r < 2) ? '0' : (char) ((11 - r) + 48);

            return dig13 == cnpj.charAt(12) && dig14 == cnpj.charAt(13);
        } catch (InputMismatchException e) {
            return false;
        }
    }

}
