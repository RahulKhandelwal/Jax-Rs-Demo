/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.resources;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class holds a collection of books in memory and provide CRUD over that.
 *
 * @author rahul.kh
 */
public enum BookDao {

    INSTANCE;

    private final Map<Integer, Book> books;
    private final AtomicInteger count;

    private BookDao() {
        this.books = new TreeMap<>();
        this.count = new AtomicInteger(0);
    }

    public Book add(Book _book) {
        _book.setId(count.incrementAndGet());
        this.books.put(_book.getId(), _book);
        return _book;
    }

    public Book remove(int _id) {
        return this.books.remove(_id);
    }

    public Collection<Book> all() {
        return this.books.values();
    }

    public Book get(int _id) {
        return this.books.get(_id);
    }

    public Book update(int _id, Book _book) {
        Book old = this.books.get(_id);

        if (old == null) {
            return null;
        }

        _book.setId(_id);
        this.books.put(_id, _book);
        return old;
    }
}
