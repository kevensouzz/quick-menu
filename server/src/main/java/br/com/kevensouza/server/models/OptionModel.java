package br.com.kevensouza.server.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "options")
@EqualsAndHashCode(callSuper = false)
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
    private byte[] picture;
    private BigDecimal price;
    private Boolean avaliability;
}
