package br.com.kevensouza.server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID configId;
    @OneToOne
    @JoinColumn(name = "menuId")
    private MenuModel menu;
    private String backgroundColor;
    private String fontColor;
    private int fontSize;
}
