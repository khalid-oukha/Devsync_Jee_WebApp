package org.example.repository.tag;

import org.example.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<Tag> findAll();

    void create(Tag tag);

    void update(Tag tag);

    void delete(Long tagId);

    Optional<Tag> findById(Long tagId);

    Optional<Tag> findByName(String tagName);
}
