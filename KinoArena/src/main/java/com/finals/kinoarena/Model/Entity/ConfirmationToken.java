package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

//    @Temporal(TemporalType.DATE)
//    private LocalDateTime createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        //createdDate = LocalDateTime.now();
        confirmationToken = UUID.randomUUID().toString();
    }

}