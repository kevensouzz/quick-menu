package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID menuId;
    private String name;
    private String code;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("menu")
    private List<OptionModel> options = new ArrayList<>();
}
