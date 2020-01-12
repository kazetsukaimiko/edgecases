package com.example.nitrite.support;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Highlights some of the issues I found sharing a Collection with several ObjectMappers.
 * Seems like this works fairly well, really.
 * @param <P>
 * @param <C>
 */
public abstract class HappyFamilyTest<P extends Pet, C extends PetsCrud<P>> extends HappyFamilyTestBase {

    abstract protected C makeCrud(Nitrite nitrite);
    abstract protected P spawn();

    protected Nitrite db;
    protected C crud;
    protected List<P> context;

    @Before
    public void init() {
        db = getDb();
        crud = makeCrud(db);

        context = IntStream.range(0, 100-RANDOM.nextInt(50))
                .mapToObj(idx -> crud.create(spawn()))
                .collect(Collectors.toList());

        db.commit();
    }



    @Test
    public void testDeleteIteratorNPE() {
        ObjectRepository<Llama> llamas = db.getRepository(Llama.class);
        Llama one = new Llama();
        one.setName("Jane");
        Llama two = new Llama();
        two.setName("Jill");

        llamas.insert(one, two);

        WriteResult writeResult = llamas.remove(ObjectFilters.eq("name", "Pete"));

        /* BUG: NPEs on getting the iterator because the field:
         *
         * private List<NitriteId> nitriteIdList;
         *
         * is not ensured to be non-null before getting the iterator. Return Collections.emptyList().iterator().
         */
        for (NitriteId id : writeResult) {
            System.out.println(id);
        }
    }


    @Test
    public void testCreate() {
        long count = context.size();

        assertEquals("We should know what has been created.", count, crud.count());

        crud.create(spawn());

        assertEquals("Should update", count+1, crud.count());

    }

    @Test
    public void testRead() {
        crud.readAll()
                .forEach(pet -> crud.read(pet.getId())
                        .ifPresentOrElse(samePet -> assertEquals("Should be able to find the same single instances.",
                        pet, samePet), () -> fail("Should find at least something")));
    }

    @Test
    public void testUpdate() {
        crud.readAll()
                .findFirst()
                .ifPresentOrElse(pet -> {
                    crud.read(pet.getId())
                            .ifPresentOrElse(samePet -> assertEquals(pet, samePet), () -> fail("Should find at least something"));
                    pet.setName(randomName());
                    crud.read(pet.getId())
                            .ifPresentOrElse(samePet -> assertNotEquals(pet, samePet), () -> fail("Should find at least something"));
                    crud.update(pet);
                    crud.read(pet.getId())
                            .ifPresentOrElse(samePet -> assertEquals(pet, samePet), () -> fail("Should find at least something"));
                }, () -> fail("Should find at least something"));
    }

    @Test
    public void testDelete() {
        P p = getSingle();
        crud.delete(p);

        // BUG: This == NPE. Searching for previously valid NitriteIds that do not exist
        assertTrue(crud.read(p.getId()).isEmpty());
        assertTrue(crud.readAll().noneMatch(other -> Objects.equals(other, p)));

    }

    public P getSingle() {
        return crud.readAll()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nothing? Really?"));
    }
}
