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

import javax.inject.*;

/**
 * This class holds a collection of books in memory and provide CRUD over that.
 *
 * @author rahul.kh
 */
@Singleton
public class BookDao {

    private final Map<Integer, Book> books = new TreeMap<>();
    private final AtomicInteger count = new AtomicInteger(0);
    private final Object lock = new Object();

    private BookDao() {
    }

    public Book add(Book _book) {
        synchronized(this.lock) {
            _book.setId(count.incrementAndGet());
            this.books.put(_book.getId(), _book);
        }

        return _book;
    }

    public Book remove(int _id) {
        synchronized(this.lock) {
            return this.books.remove(_id);
        }
    }

    public Collection<Book> all() {
        synchronized(this.lock) {
            return this.books.values();
        }
    }

    public Book get(int _id) {
        synchronized(this.lock) {
            return this.books.get(_id);
        }
    }

    public Book update(int _id, Book _book) {
        Book old = this.get(_id);

        if (old == null) {
            return null;
        }

        synchronized(this.lock) {
            _book.setId(_id);
            this.books.put(_id, _book);
        }
        
        return old;
    }
}
