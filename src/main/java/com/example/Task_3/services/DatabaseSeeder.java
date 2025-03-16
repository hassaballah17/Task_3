package com.example.Task_3.services;

import com.example.Task_3.models.Comment;
import com.example.Task_3.models.Post;
import com.example.Task_3.models.User;
import com.example.Task_3.repositories.PostRepository;
import com.example.Task_3.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class DatabaseSeeder {
   private final UserRepository userRepository;
   private final PostRepository postRepository;

   public DatabaseSeeder(UserRepository userRepository, PostRepository postRepository) {
       this.userRepository = userRepository;
       this.postRepository = postRepository;
   }

   public void seed() {
       // Clear existing data
       userRepository.deleteAll();
       postRepository.deleteAll();

       // --- Create Sample Users ---
       User alice = new User("Alice", "alice@example.com");
       User bob = new User("Bob", "bob@example.com");
       User tester = new User("tester", "tester@example.com");
       // Save the users (MongoDB will generate unique ids)
       alice = userRepository.save(alice);
       bob = userRepository.save(bob);
       tester = userRepository.save(tester);

       // --- Create Comments for Posts ---
       Comment comment1 = new Comment("Great post!", "2025-03-01");
       Comment comment2 = new Comment("Thanks for sharing!", "2025-03-02");
       Comment comment3 = new Comment("Tester Comment Here", "2025-03-02");
       // --- Create Sample Posts ---
       // Post 1: Authored by Alice with two embedded comments
       Post post1 = new Post(
               "First Post",
               "This is the content of the first post.",
               alice,
               Arrays.asList(comment1, comment2)
       );

       // Post 2: Authored by Bob with one embedded comment
       Post post2 = new Post(
               "Second Post",
               "This is the content of the second post.",
               bob,
               Collections.singletonList(comment1)
       );

       // Post 3: Authored by Alice with no comments
       Post post3 = new Post(
               "Third Post",
               "More insights from Alice.",
               alice,
               Collections.emptyList()
       );

       Post post4 = new Post(
               "Test Post",
               "The is the content of the test post",
               tester,
               Collections.singletonList(comment3)
       );

       // Save posts
       postRepository.saveAll(Arrays.asList(post1, post2, post3, post4));

       // Optionally, print the seeded data to the console for verification
       System.out.println("Seeded Users: " + userRepository.findAll());
       System.out.println("Seeded Posts: " + postRepository.findAll());
   }
}
