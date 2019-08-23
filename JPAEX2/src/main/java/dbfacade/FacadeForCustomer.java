/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbfacade;

import entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ludvig
 */
public class FacadeForCustomer {

    private static EntityManagerFactory emf;
    private static FacadeForCustomer instance;
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        FacadeForCustomer facade = FacadeForCustomer.getCustomerFacade(emf);
        
        Customer c1 = facade.addCustomer("Hans", "Preben");
        Customer c2 = facade.addCustomer("Simone", "Lonesen");
        Customer c3 = facade.addCustomer("Kim", "Bim");
        
        List<Customer> list = facade.getAllCustomers();
        for (Customer c : list){
            System.out.println(c.getFirstName());
        }
        //System.out.println("Antal" + facade.getCustomerCount());
    }

    private FacadeForCustomer() {
    }

    public static FacadeForCustomer getCustomerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FacadeForCustomer();
        }
        return instance;
    }

    public Customer addCustomer(String firstName, String lastName) {
        Customer c = new Customer(firstName, lastName);
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        } finally {
            em.close();
        }
    }

    public Customer findCustomer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer c = em.find(Customer.class, id);
            return c;
        } finally {
            em.close();
        }
    }

    public Customer findByLastName(String lastName) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer c = em.find(Customer.class, lastName);
            return c;
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Customer> query
                    = em.createQuery("Select customer from Customer customer", Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("Select COUNT(customer) from Customer customer", Customer.class);
            return (int) query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
