package com.pedro.f20.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tb_email_codes")
@Entity(name = "EmailCode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EmailCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "email_code")
    private String emailCode;

    @Column(name = "is_used")
    private Boolean isUsed;

    private LocalDateTime dtcreate;
    private LocalDateTime dtupdate;

    public EmailCode(User user) {
        this.user = user;
        this.emailCode = generateCode();
        this.isUsed = false;
        this.dtcreate = LocalDateTime.now();
        this.dtupdate = LocalDateTime.now();
    }

    private String generateCode() {
        int length = 6;
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int)(Math.random() * 10);
            code.append(digit);
        }
        return code.toString();
    }
}
