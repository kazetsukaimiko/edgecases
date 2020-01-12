package com.example.nitrite.support;

import org.dizitart.no2.Nitrite;

public class CatCrud extends PetsCrud<Cat> {
    public CatCrud(Nitrite db) {
        super(db);
    }

    @Override
    Class<Cat> petKlazz() {
        return Cat.class;
    }
}
