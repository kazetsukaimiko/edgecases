package com.example.nitrite.support;

import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;

import java.util.Objects;


/**
 * This entity is designed so all child classes go into the same
 * ObjectRepository.
 */

//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@entity")
public abstract class Pet {

    @Id
    private NitriteId id;
    private String name;

    public NitriteId getId() {
        return id;
    }

    public void setId(NitriteId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id) &&
                Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + getName();
    }
}
