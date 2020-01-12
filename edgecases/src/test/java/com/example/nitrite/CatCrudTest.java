package com.example.nitrite;

import com.example.nitrite.support.Cat;
import com.example.nitrite.support.CatCrud;
import com.example.nitrite.support.HappyFamilyTest;
import org.dizitart.no2.Nitrite;

public class CatCrudTest extends HappyFamilyTest<Cat, CatCrud> {
    @Override
    protected CatCrud makeCrud(Nitrite nitrite) {
        return new CatCrud(nitrite);
    }

    @Override
    protected Cat spawn() {
        return spawnCat();
    }
}
