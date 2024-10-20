package services;

import org.example.entities.Tag;
import org.example.entities.User;
import org.example.repository.tag.TagRepository;
import org.example.repository.user.UserRepository;
import org.example.services.TagService;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TagServiceTests {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_should_return_list_of_tags() {
        Tag tag1 = Tag.builder()
                .id(1000L)
                .name("task1")
                .build();

        Tag tag2 = Tag.builder()
                .id(1001L)
                .name("task2")
                .build();

        List<Tag> mockTags = Arrays.asList(tag1, tag2);
        when(tagRepository.findAll()).thenReturn(mockTags);

        List<Tag> tags = tagService.findAll();

        assertNotNull(tags, "Tags list should not be null");
        assertEquals(2, tags.size(), "Array size must match the number of mock tags");

        System.out.println("Tags found: " + tags.size());

        assertEquals(mockTags.get(0).getName(), tags.get(0).getName(), "First tag's name should match");
        assertEquals(mockTags.get(1).getName(), tags.get(1).getName(), "Second tag's name should match");
    }

    @Test
    void findById_should_return_tag_if_present(){
        Tag expectedTag = Tag.builder().id(1L).name("khalid").build();
        when(tagRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedTag));
        Tag result = tagService.findById(1L);

        assertEquals(expectedTag.getId(),result.getId(),"tag should match");
        assertEquals(expectedTag.getName(),result.getName(),"tag name should match");

    }

    @Test
    void findById_should_return_null_if_not_present(){
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());
        Tag result = tagService.findById(1L);
        assertNull(result,"tag should be null");
    }

    @Test
    void create_should_create_new_tag(){
        Tag tag = Tag.builder()
                .id(1000L)
                .name("frontEnd")
                .build();

        when(tagRepository.findById(1000L)).thenReturn(Optional.empty());
        Tag beforeTag = tagService.findById(1000L);
        assertNull(beforeTag, "Tag should not be already created");

        doNothing().when(tagRepository).create(tag);

        tagService.create(tag);

        when(tagRepository.findById(1000L)).thenReturn(Optional.of(tag));
        Tag afterTag = tagService.findById(1000L);

        assertNotNull(afterTag, "Tag should be created and found");
        assertEquals(afterTag.getName(), tag.getName(), "Name should match the created tag");
        assertEquals(afterTag.getId(), tag.getId(), "ID should match the created tag");
    }

    @Test
    void delete_should_delete_tag(){
        Tag tag = Tag.builder()
                .id(1000L)
                .name("frontEnd")
                .build();

        when(tagRepository.findById(1000L)).thenReturn(Optional.of(tag));
        Tag beforeTag = tagService.findById(1000L);
        assertNotNull(beforeTag, "Tag should be present before deletion");

        doNothing().when(tagRepository).delete(1000L);

        tagService.delete(1000L);

        when(tagRepository.findById(1000L)).thenReturn(Optional.empty());
        Tag afterTag = tagService.findById(1000L);

        assertNull(afterTag, "Tag should be deleted");
    }

    @Test
    void update_should_update_tag(){
        Tag tag = Tag.builder()
                .id(1000L)
                .name("frontEnd")
                .build();

        when(tagRepository.findById(1000L)).thenReturn(Optional.of(tag));
        Tag beforeTag = tagService.findById(1000L);
        assertNotNull(beforeTag, "Tag should be present before update");

        Tag updatedTag = Tag.builder()
                .id(1000L)
                .name("backEnd")
                .build();

        doNothing().when(tagRepository).update(updatedTag);

        tagService.update(updatedTag);

        when(tagRepository.findById(1000L)).thenReturn(Optional.of(updatedTag));
        Tag afterTag = tagService.findById(1000L);

        assertNotNull(afterTag, "Tag should be updated");
        assertEquals(afterTag.getName(), updatedTag.getName(), "Name should match the updated tag");
        assertEquals(afterTag.getId(), updatedTag.getId(), "ID should match the updated tag");
    }

}
