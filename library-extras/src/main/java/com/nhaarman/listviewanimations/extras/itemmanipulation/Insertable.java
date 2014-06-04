package com.nhaarman.listviewanimations.extras.itemmanipulation;

/**
 * An interface for inserting items at a certain index.
 */
public interface Insertable<T> {

    /**
     * Will be called to insert given {@code item} at given {@code index} in the list.
     *
     * @param index the index the new item should be inserted at
     * @param item  the item to insert
     */
    public void add(int index, T item);
}
