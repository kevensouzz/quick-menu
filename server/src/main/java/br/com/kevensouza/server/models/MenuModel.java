package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID menuId;

    @Column(columnDefinition = "text", nullable = false)
    private String name;

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Size(min = 6, max = 6)
    private String code;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("menu")
    private List<OptionModel> options = new ArrayList<>();
}
