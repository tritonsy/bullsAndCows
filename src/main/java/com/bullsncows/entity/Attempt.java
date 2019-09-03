package com.bullsncows.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "attempt", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "number"})
@NoArgsConstructor
public class Attempt {
    @Id
    @SequenceGenerator(name = "attempt_sequence", sequenceName = "attempt_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attempt_sequence")
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "number")
    private String number;

    @Column(name = "attempt_time", nullable = false, insertable = false, updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime attempt_time;

    @ManyToOne(optional = false, targetEntity = Game.class)
    @JoinColumn(name = "game", nullable = false)
    private Game game;
}
