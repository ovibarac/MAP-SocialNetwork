package service;

import domain.Entity;
import domain.Friendship;
import repo.Repository;

public class GenericService <ID, E extends Entity<ID>> implements Service<ID, E> {
    Repository<ID, E> repo;

    public GenericService(Repository<ID, E> repo) {
        this.repo = repo;
    }

    @Override
    public E add(E entity) {
        return repo.save(entity);
    }

    @Override
    public Iterable<E> findAll() {
        return repo.findAll();
    }

    @Override
    public E delete(ID id) {
        return repo.delete(id);
    }

    @Override
    public E findOne(ID id) {
        return repo.findOne(id);
    }


}
