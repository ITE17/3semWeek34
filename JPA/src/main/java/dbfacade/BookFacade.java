/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbfacade;

import entity.Book;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ludvig
 */
public class BookFacade {

    private static EntityManagerFactory emf;
    private static BookFacade instance;

    private BookFacade() {
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu"); //laver EntityManagerFactory baseret på persistence.xml
        BookFacade facade = BookFacade.getBookFacade(emf); //laver BookFacade med et EntityManagerFactory objekt
        Book b1 = facade.addBook("Author 1");
        Book b2 = facade.addBook("Author 2");
        
        //Find book by ID
        System.out.println("Book1: " + facade.findBook(b1.getId()).getAuthor());
        System.out.println("Book2: " + facade.findBook(b2.getId()).getAuthor());
        //Find all books
        System.out.println("Number of books: " + facade.getAllBooks().size());
    }

    /** 
     * Returns an instance of BookFacade.java. Instantiates new one if field instance is null.
     * 
     * @param _emf
     * @return BookFacade
     */
    public static BookFacade getBookFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookFacade();
        }
        return instance;
    }

    /**
     * Creates a Book object and inserts it in the database. 
     * First an EntityManager is instantiated. An EntityTransaction object is instantiated and resource transaction is started.
     * The Book object is managed and persisted and written to the database. Then the EntityManager is closed.
     * 
     * @param author
     * @return Book
     */
    public Book addBook(String author) {
        Book book = new Book(author);
        EntityManager em = emf.createEntityManager(); //laver EntityManager
        try {
            em.getTransaction().begin(); //starter resource transaktion
            em.persist(book); //book håndteres og bevares
            em.getTransaction().commit(); //resource transaktionen færdiggøres, data skrives til databasen
            return book;
        } finally {
            em.close();
        }
    }

    /**
     * Finds and returns a specific entity from the database based of specific id. 
     * 
     * @param id
     * @return Book
     */
    public Book findBook(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Book book = em.find(Book.class, id);
            return book;
        } finally {
            em.close();
        }
    }

    /**
     * Returns a List of all entities in a database of class Book.
     * 
     * @return List
     */
    public List<Book> getAllBooks() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Book> query
                    = em.createQuery("Select book from Book book", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
