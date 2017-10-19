package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 10/18/2017.
 */

public interface AbstractDao<T> {


    public T create(T address);
    public void update(T address);
    public T get(Integer id);
    public T delete(Integer id);
}
