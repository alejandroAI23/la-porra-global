package com.porraglobal.tournament.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre genérico de la selección, p. ej. "España". Sin marcas oficiales. */
    @Column(nullable = false, unique = true, length = 60)
    private String name;

    /** Código ISO-3166 alpha-3, p. ej. "ESP". */
    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(name = "flag_emoji", length = 10)
    private String flagEmoji;

    @Column(name = "group_name", length = 5)
    private String groupName;
}
