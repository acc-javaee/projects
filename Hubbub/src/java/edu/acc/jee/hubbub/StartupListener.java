package edu.acc.jee.hubbub;

import edu.acc.jee.hubbub.domain.DataService;
import edu.acc.jee.hubbub.domain.ListDao;
import edu.acc.jee.hubbub.domain.Post;
import edu.acc.jee.hubbub.domain.User;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataService dao = new ListDao();
        
        User u1 = dao.addUser(new UserDTO("johndoe", "P@ssw0rd"));
        User u2 = dao.addUser(new UserDTO("janedoe", "P@ssw0rd"));
        
        Post p1 = dao.addPost("My first Hubbub post! #JavaRules", u1);
        Post p2 = dao.addPost("Joined 'cause @johndoe told me to. #JavaRules", u2);
        
        sce.getServletContext().setAttribute("dao", dao);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
