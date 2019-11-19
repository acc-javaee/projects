package edu.acc.jee.jpatest;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATestPU");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin(); //------------------------------TRANSACTION START
        Family family = new Family();
        family.setDescription("The Dandy Family");
        em.persist(family);
        for (int i = 0; i < 40; i++) {
            Person person = new Person();
            person.setFirstName("Jim_" + i);
            person.setLastName("Dandy");
            person.setFamily(family);
            em.persist(person);
            /*
                entities are assigned their id numbers when persisted,
                so we can't add persons to the family without persisting
                them first
            */
            family.getMembers().add(person);
            /*
                since we've changed the members of the family, we have to
                update the family too by repersisting
            */
            em.persist(family);
        }
        em.getTransaction().commit(); //-----------------------------TRANSACTION END
        em.close();
        
        
        em = emf.createEntityManager();
        em.getTransaction().begin(); //------------------------------TRANSACTION START
        Query q = em.createQuery("SELECT p FROM Person p");
        List<Person> people = q.getResultList();
        System.out.printf("There are %d people in the database.\n", people.size());
        people.stream().forEach(System.out::println);
        em.getTransaction().commit(); //-----------------------------TRANSACTION END
        em.close();


        em = emf.createEntityManager();
        em.getTransaction().begin(); //------------------------------TRANSACTION START
        q = em.createQuery("SELECT f FROM Family f");
        List<Family> families = q.getResultList();
        System.out.println("Family count: " + families.size());
        System.out.printf(
                "Members of the %s family: %d\n",
                families.get(0).getDescription(),
                families.get(0).getMembers().size()
        );     
        em.getTransaction().commit(); //-----------------------------TRANSACTION END
        em.close();
        
        
        em = emf.createEntityManager();
        em.getTransaction().begin(); //------------------------------TRANSACTION START
        q = em.createQuery("SELECT p FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName");
        q.setParameter("firstName", "Jim_1");
        q.setParameter("lastName", "Dandy");
        Person jim1 = (Person)q.getSingleResult();
        em.remove(jim1);
        em.getTransaction().commit(); //-----------------------------TRANSACTION END
        try {
            jim1 = (Person)q.getSingleResult();
            System.out.println(jim1);
        }
        catch (NoResultException nre) {
            System.out.println("Jim1 has been removed");
        }
        em.close();        
        
    }
}
