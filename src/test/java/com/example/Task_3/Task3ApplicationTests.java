package com.example.Task_3;

import com.example.Task_3.models.Comment;
import com.example.Task_3.models.Post;
import com.example.Task_3.models.User;
import com.example.Task_3.repositories.PostRepository;
import com.example.Task_3.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class Task3ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	// Seeded entities (accessible by tests)
	private User user1, user2;
	private Post post1, post2;

	@BeforeEach
	public void setup() {
		// Clear collections to ensure a fresh seed for every test run.
		userRepository.deleteAll();
		postRepository.deleteAll();

		// --- Create and save Users ---
		user1 = new User("Alice", "alice@example.com");
		user2 = new User("Bob", "bob@example.com");
		user1 = userRepository.save(user1);
		user2 = userRepository.save(user2);

		// --- Create Comments for posts ---
		Comment comment1 = new Comment("Nice post!", "2025-03-02T12:00:00Z");
		Comment comment2 = new Comment("Very informative.", "2025-03-02T13:00:00Z");

		// --- Create and save Posts ---
		// Post 1: Authored by user1, with one comment initially.
		post1 = new Post("Post One", "Content of Post One", user1, Arrays.asList(comment1));
		// Post 2: Authored by user2, with one comment initially.
		post2 = new Post("Post Two", "Content of Post Two", user2, Arrays.asList(comment2));
		post1 = postRepository.save(post1);
		post2 = postRepository.save(post2);
	}

	@AfterEach
	public void tearDown() {
		// Clean up the database after each test
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	// ------------------------ Tests for updateUserUsername ------------------------

	@Test
	public void testUpdateUserUsername_Success() throws Exception {
		String payload = "{ \"id\": \"" + user1.getId() + "\", \"username\": \"AliceNew\" }";
		mockMvc.perform(put("/users/updateUser")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("AliceNew"));
	}

	@Test
	public void testUpdateUserUsername_NotFound() throws Exception {
		String payload = "{ \"id\": \"nonexistentid\", \"username\": \"NoUser\" }";
		mockMvc.perform(put("/users/updateUser")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isNotFound());
	}

	// ------------------------ Tests for getPostsByAuthorID ------------------------

	@Test
	public void testGetPostsByAuthorID_WithPosts() throws Exception {
		mockMvc.perform(get("/posts/postsByUser/"+ user1.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].author.id").value(user1.getId()));
	}

	@Test
	public void testGetPostsByAuthorID_NoPosts() throws Exception {
		// Remove posts by user2 to simulate no posts for user2.
		postRepository.delete(post2);
		mockMvc.perform(get("/posts/postsByUser/"+user2.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}

	// ------------------------ Tests for addComment ------------------------

	@Test
	public void testAddComment_Success() throws Exception {
		String commentPayload = "{ \"text\": \"Awesome post!\", \"date\": \"2025-03-02T15:00:00Z\" }";
		mockMvc.perform(post("/posts/" + post1.getId() + "/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentPayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.comments").isArray())
				.andExpect(jsonPath("$.comments[?(@.text=='Awesome post!')]").exists());
	}

	@Test
	public void testAddComment_PostNotFound() throws Exception {
		String commentPayload = "{ \"text\": \"Will not work\", \"date\": \"2025-03-02T15:00:00Z\" }";
		mockMvc.perform(post("/posts/12/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentPayload))
				.andExpect(status().isNotFound());
	}
}
