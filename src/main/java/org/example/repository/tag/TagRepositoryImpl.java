package org.example.repository.tag;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entities.Tag;

import java.util.List;
import java.util.Optional;

public class TagRepositoryImpl implements TagRepository {

    private final EntityManager em;

    public TagRepositoryImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    public List<Tag> findAll() {
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    public void create(Tag tag) {
        try {
            em.getTransaction().begin();
            em.persist(tag);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }

    }

    public void update(Tag tag) {
        try {
            em.getTransaction().begin();
            em.merge(tag);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }

    }

    public void delete(Long tagId) {
        try {
            em.getTransaction().begin();
            Tag tag = em.find(Tag.class, tagId);
            if (tag != null) {
                em.remove(tag);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Optional<Tag> findById(Long tagId) {
        Tag tag = em.find(Tag.class, tagId);
        return Optional.ofNullable(tag);
    }

    public Optional<Tag> findByName(String tagName) {
        Tag tag = em.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", tagName)
                .getSingleResult();
        return Optional.ofNullable(tag);
    }
}
