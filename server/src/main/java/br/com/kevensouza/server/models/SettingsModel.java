package br.com.kevensouza.server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID settingsId;
    @OneToOne
    @JoinColumn(name = "menuId")
    private MenuModel menu;
    private String backgroundColor;
    private String fontColor;
    private int fontSize;
}
