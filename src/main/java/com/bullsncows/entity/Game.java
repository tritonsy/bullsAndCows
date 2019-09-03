package com.bullsncows.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "game", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "guessedNumber", "dateOfGame", "attempts"})
public class Game {
    @Id
    @SequenceGenerator(name = "game_sequence", sequenceName = "game_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_of_game", nullable = false, insertable = false, updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateOfGame;

    @ManyToOne(optional = false, targetEntity = Player.class)
    @JoinColumn(name = "player", nullable = false)
    private Player player;

    @Basic
    @Column(name = "guessed_number", nullable = false)
    private String guessedNumber;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    @OrderBy("id")
    private List<Attempt> attempts;
}
