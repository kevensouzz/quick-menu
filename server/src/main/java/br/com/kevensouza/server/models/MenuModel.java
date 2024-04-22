package br.com.kevensouza.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuModel extends RepresentationModel<MenuModel> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID menuId;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties({"password", "role", "menus", "links"})
    private UserModel user;
    private String name;
    private String code;
    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("menu")
    private List<OptionModel> options = new ArrayList<>();
    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"configId", "menu"})
    private List<ConfigModel> config = new ArrayList<>();
}
