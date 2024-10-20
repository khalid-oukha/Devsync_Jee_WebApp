package org.example.repository.tag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import org.example.entities.Tag;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private final EntityManager em;

    public TagRepositoryImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    @Override
    public List<Tag> findAll() {
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Optional<Tag> findById(Long tagId) {
        Tag tag = em.find(Tag.class, tagId);
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        Tag tag = em.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", tagName)
                .getSingleResult();
        return Optional.ofNullable(tag);
    }
}
