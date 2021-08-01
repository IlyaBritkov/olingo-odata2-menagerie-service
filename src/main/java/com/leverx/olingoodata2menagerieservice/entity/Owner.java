package com.leverx.olingoodata2menagerieservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true)

@Entity
@Table(name = "owner", schema = "public")
public class Owner {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "owner",
            fetch = EAGER,
            cascade = {
                    PERSIST,
                    MERGE,
                    DETACH,
                    REFRESH
            }
    )
    private List<Pet> pets = new ArrayList<>();

    @Builder
    public Owner(String firstName, String lastName, Integer age, Boolean isAlive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.isAlive = isAlive == null || isAlive;
    }

    public Pet addPet(Pet pet) {
        pets.add(pet);
        pet.setOwner(this);
        return pet;
    }

    public void dismissPet(Pet pet) {
        this.pets.remove(pet);
    }

    public void dismissAllPets() {
        this.pets
                .forEach(Pet::dismissOwner);
        this.pets.clear();
    }
}
