package br.com.caminhodasaguas.api.domains;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification")
public class EmailVerificationDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Duration exp;

    @Column(nullable = false)
    private boolean checked = false;

    @Column(nullable = false, name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public EmailVerificationDomain() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Duration getExp() {
        return exp;
    }

    public void setExp(Duration exp) {
        this.exp = exp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
