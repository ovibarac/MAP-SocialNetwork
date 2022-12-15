package com.example.mapsocialnetworkgui.repo;
import com.example.mapsocialnetworkgui.repo.exception.Validator;
import com.example.mapsocialnetworkgui.domain.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private final Validator<E> validator;
    Map<ID,E> entities;
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }
    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return Optional.of(entity);
        }
        else entities.put(entity.getId(),entity);
        return Optional.empty();
    }
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> toDelete = this.findOne(id);
        toDelete.ifPresent(e -> entities.remove(e.getId()));
        return toDelete;
    }
    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        entities.put(entity.getId(),entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}