package org.example.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entities.Tag;
import org.example.repository.tag.TagRepository;
import org.example.repository.tag.TagRepositoryImpl;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TagService {

    @Inject
    private TagRepository tagRepository;


    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public void create(Tag tag) {
        tagRepository.create(tag);
    }

    public void update(Tag tag) {
        tagRepository.update(tag);
    }

    public void delete(Long tagId) {
        tagRepository.delete(tagId);
    }

    public Tag findById(Long tagId) {
        return tagRepository.findById(tagId).orElse(null);
    }


}
