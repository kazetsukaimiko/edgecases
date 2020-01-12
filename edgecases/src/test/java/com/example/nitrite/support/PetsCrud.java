package com.example.nitrite.support;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class PetsCrud<P extends Pet> {
    private final Nitrite db;
    abstract Class<P> petKlazz();

    protected PetsCrud(Nitrite db) {
        this.db = db;
    }

    // Save, really.
    public P create(P pet) {
        Optional<NitriteId> nitriteId = resultStream((pet.getId() != null) ? getRepo().update(pet) : getRepo().insert(pet))
                .findFirst();
        nitriteId.ifPresent(pet::setId);
        getRepo().update(pet);
        return pet;
    }

    public Optional<P> read(NitriteId id) {
        return Optional.ofNullable(getRepo().getById(id))
                .filter(petKlazz()::isInstance)
                .map(petKlazz()::cast);
    }

    public Stream<P> readAll() {
        return streamCursor(getRepo().find());
    }

    public P update(P pet) {
        return create(pet);
    }

    public Optional<NitriteId> delete(P pet) {
        return resultStream(getRepo().remove(pet))
                .findFirst();
    }

    public Optional<NitriteId> deleteById(NitriteId nitriteId) {
        return resultStream(getRepo().remove(ObjectFilters.eq("id", nitriteId)))
            .findFirst();
    }

    public Optional<NitriteId> deleteById2(NitriteId nitriteId) {
        return read(nitriteId)
                .flatMap(this::delete);
    }

    public long count() {
        // getRepo().size(); // Wrong
        // There's a way to do this using the Jackson field generated.
        // Nitrite needs count(ObjectFilter) method.
        return readAll()
                .count();
    }



    private ObjectRepository<P> getRepo() {
        return db.getRepository(petKlazz());
    }

    private static Stream<NitriteId> resultStream(WriteResult writeResult) {
        return StreamSupport.stream(writeResult.spliterator(), false);
    }

    private static <X> Stream<X> streamCursor(Cursor<X> cursor) {
        return StreamSupport.stream(cursor.spliterator(), false);
    }
    private static <X extends Pet> Stream<X> streamCursor(Cursor<Pet> cursor, Class<X> childClass) {
        return streamCursor(cursor)
                .filter(childClass::isInstance)
                .map(childClass::cast);
    }
}
