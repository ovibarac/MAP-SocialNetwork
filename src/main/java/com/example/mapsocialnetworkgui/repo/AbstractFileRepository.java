package com.example.mapsocialnetworkgui.repo;

import com.example.mapsocialnetworkgui.repo.exception.Validator;
import com.example.mapsocialnetworkgui.domain.Entity;
import com.example.mapsocialnetworkgui.domain.Friendship;
import com.example.mapsocialnetworkgui.domain.User;


import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    File file;

    public AbstractFileRepository(Validator<E> validator, String filename) {
        super(validator);
        file=new File(filename);
        try {
            file.createNewFile();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Load data from file into memory
     */
    abstract void loadFromFile();

    /**
     * Save data from memory to file
     */
    abstract void saveToFile();

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        saveToFile();
        return e;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> d = super.delete(id);
        saveToFile();
        return d;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> e= super.update(entity);
        saveToFile();
        return e;
    }
}
