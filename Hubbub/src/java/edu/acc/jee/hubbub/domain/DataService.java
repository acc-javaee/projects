package edu.acc.jee.hubbub.domain;

import edu.acc.jee.hubbub.UserDTO;
import java.io.InputStream;
import java.util.List;

public interface DataService {
    User addUser(UserDTO bean);
    User findUserByUserDTO(UserDTO bean);
    User findUserByUsername(String username);
    boolean userExists(String username);
    
    Post addPost(String content, User author);
    Post findPostById(int id);
    List<Post> findPostsByPage(int offset, int limit);
    List<Post> findPostsByAuthorAndPage(String authorName, int offset, int limit);
    
    Profile addProfile();
    Profile findProfileById(int id);
    boolean updateProfileFor(User user, Profile changed);
    
    Comment addComment(User user, Post target, String content);
    Comment findCommentById(int id);
    List<Comment> findCommentsByPostAndPage(Post target, int offset, int limit);
    
    boolean updateAvatarFor(User user, String mime, InputStream is);
    boolean revertAvatarFor(User user);
    
    boolean follow(User follower, User followee);
    boolean unfollow(User follower, User followee);
    List<User> findFolloweesByUser(User user);
    List<User> findFollowersByUser(User user);
    
    List<Post> findPostsByMentionOf(User subject);
    Mention addMention(User subject, Post post);
    
    List<Post> findPostsByTag(Tag tag);
    Tag findTagByTagName(String tagName);
    Tag addTag(User creator, String tagName);
    boolean tagPost(String tagName, Post post);
    List<Tag> findTagsByCreator(User creator);
}
