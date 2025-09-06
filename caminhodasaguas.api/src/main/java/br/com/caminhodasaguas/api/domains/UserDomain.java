package br.com.caminhodasaguas.api.domains;

import org.hibernate.annotations.SQLDelete;

import br.com.caminhodasaguas.api.domains.enums.UserEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
public class UserDomain extends BaseDomain {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserEnum role = UserEnum.ADMIN;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String phone;

    public UserDomain() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public UserEnum getRole() {
        return role;
    }

    public void setRole(UserEnum role) {
        this.role = role;
    }
}
