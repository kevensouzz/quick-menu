package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID optionId;
    private String name;
    private String description;
    private float price;
    private Boolean avaliability;
    @ManyToOne
    @JoinColumn(name = "menuId")
    @JsonIgnoreProperties("menu")
    private MenuModel menu;
}
