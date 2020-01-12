package com.example.nitrite.support;

import org.dizitart.no2.Nitrite;

public class DogCrud extends PetsCrud<Dog> {
    public DogCrud(Nitrite db) {
        super(db);
    }

    @Override
    Class<Dog> petKlazz() {
        return Dog.class;
    }
}
