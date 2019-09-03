package com.bullsncows.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "player", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "login", "pass", "dateOfRegistration"})
public class Player {

    @Id
    @SequenceGenerator(name = "player_sequence", sequenceName = "player_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_sequence")
    @Column(name = "id")
    private Integer id;

    @Basic
    @NonNull
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Basic
    @NonNull
    @Column(name = "pass", nullable = false)
    private String pass;

    @Column(name = "date_of_registration", nullable = false, insertable = false, updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateOfRegistration;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    private List<Game> games;
}
