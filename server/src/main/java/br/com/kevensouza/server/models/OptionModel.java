package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    @ManyToOne
    @JoinColumn(name = "menuId")
    @JsonIgnoreProperties("menu")
    private MenuModel menu;
    private String name;
    private String description;
    private byte[] picture;
    private BigDecimal price;
    private Boolean avaliability;
}
