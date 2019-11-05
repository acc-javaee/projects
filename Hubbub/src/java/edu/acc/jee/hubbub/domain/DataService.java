package edu.acc.jee.hubbub.domain;

import edu.acc.jee.hubbub.UserDTO;
import java.util.List;

public interface DataService {
    User addUser(UserDTO bean);
    User findUserByUserDTO(UserDTO bean);
    User findUserByUsername(String username);
    boolean userExists(String username);
    
    Post addPost(String content, User author);
    Post findPostById(int id);
    List<Post> findPostsByPage(int offset, int limit);
    
    boolean updateProfileFor(User user, Profile changed);
    
    Comment addComment(User user, Post target, String content);
}
