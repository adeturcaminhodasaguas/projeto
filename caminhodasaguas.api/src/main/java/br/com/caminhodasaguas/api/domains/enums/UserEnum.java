package br.com.caminhodasaguas.api.domains.enums;

public enum UserEnum {
    ADMIN("Admin");

    private final String descricao;

    UserEnum(String role) {
        this.descricao = role;
    }

    public String getDescricao() {
        return this.descricao;
    }
}