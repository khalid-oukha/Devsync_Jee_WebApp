package org.example.services;

import org.example.entities.Tag;
import org.example.repository.tag.TagRepository;
import org.example.repository.tag.TagRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class TagService {

    private final TagRepository tagRepository;

    public TagService() {
        this.tagRepository = new TagRepositoryImpl();
    }

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

    public Tag findOrCreateTag(String tagName) {
        Optional<Tag> existingTag = tagRepository.findByName(tagName);
        if (existingTag.isPresent()) {
            return existingTag.get();
        } else {
            Tag newTag = new Tag(tagName);
            tagRepository.create(newTag);
            return newTag;
        }
    }

}
