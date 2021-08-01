package com.leverx.olingoodata2menagerieservice.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;

@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "pet", schema = "public")
@Inheritance(strategy = JOINED)
public abstract class Pet {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "breed")
    private String breed;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "is_alive")
    @NotNull
    private Boolean isAlive = true;

    @Column(name = "is_healthy")
    private Boolean isHealthy;

    @ManyToOne(cascade = {
            PERSIST,
            MERGE,
            DETACH,
            REFRESH
    })
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @PreRemove
    public void dismissOwner() {
        this.owner.dismissPet(this);
        this.owner = null;
    }
}
