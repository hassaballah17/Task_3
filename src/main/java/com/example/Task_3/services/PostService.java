package com.example.Task_3.services;

import com.example.Task_3.models.Comment;
import com.example.Task_3.models.Post;
import com.example.Task_3.models.User;
import com.example.Task_3.repositories.PostRepository;
import com.example.Task_3.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

   private final PostRepository postRepository;
   private UserRepository userRepository;

   public PostService(PostRepository postRepository) {
       this.postRepository = postRepository;
   }

   public Post savePost(Post post) {
       return postRepository.save(post);
   }
   public List<Post> getAllPosts() {
       return postRepository.findAll();
   }

   public Optional<Post> getPostById(String id) {
       return postRepository.findById(id);
   }

   public void deletePost(String id) {
       postRepository.deleteById(id);
   }

   //Get Posts By Author ID
   public List<Post> getPostsByAuthorID(String userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    List<Post> posts = postRepository.findAllById(List.of("id1",userId));
    if (posts.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No posts found for this user");
    }
    return posts;
    }

   //Add Comment to Post
   public Post addCommentToPost(String postId, Comment newComment) {
    Optional<Post> optionalPost = postRepository.findById(postId);
    
    if (optionalPost.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }

    Post post = optionalPost.get();
    
    post.getComments().add(newComment);
    
    return postRepository.save(post);
}

}
