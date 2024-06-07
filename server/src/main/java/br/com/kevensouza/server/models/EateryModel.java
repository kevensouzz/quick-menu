package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "eataries")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EateryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID eataryId;

    @Column(unique = true, columnDefinition = "text", nullable = false)
    private String name;

    private byte[] picture;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "VARCHAR(11)", nullable = false)
    @Size(min= 11, max = 11)
    private String phone;

    @Column(columnDefinition = "text", nullable = false)
    private String address;

    @Column(columnDefinition = "VARCHAR(18)", nullable = false)
    @Size(min = 14, max = 18)
    private String cnpj;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("eateries")
    private List<UserModel> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuModel> menus = new ArrayList<>();
}
