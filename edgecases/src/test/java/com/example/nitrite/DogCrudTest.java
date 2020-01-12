package com.example.nitrite;

import com.example.nitrite.support.Dog;
import com.example.nitrite.support.DogCrud;
import com.example.nitrite.support.HappyFamilyTest;
import org.dizitart.no2.Nitrite;

// This will fail all tests because Dog doesn't have @InheritIndices.
public class DogCrudTest extends HappyFamilyTest<Dog, DogCrud> {
    @Override
    protected DogCrud makeCrud(Nitrite nitrite) {
        return new DogCrud(nitrite);
    }

    @Override
    protected Dog spawn() {
        return spawnDog();
    }
}
