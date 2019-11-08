package edu.acc.jee.hubbub;

import edu.acc.jee.hubbub.domain.DataService;
import edu.acc.jee.hubbub.domain.DerbyDAO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /*
        DataService dao = new ListDAO();
        
        User u1 = dao.addUser(new UserDTO("johndoe", "P@ssw0rd"));
        User u2 = dao.addUser(new UserDTO("janedoe", "P@ssw0rd"));
        
        Profile u2Profile = new Profile();
        u2Profile.setBiography("I sometimes hang with @johndoe.");
        u2Profile.setTimeZone("America/Los_Angeles");
        dao.updateProfileFor(u2, u2Profile);
        
        Post p1 = dao.addPost("My first Hubbub post! #JavaRules", u1);
        Post p2 = dao.addPost("Joined 'cause @johndoe told me to. #JavaRules", u2);
        
        dao.addComment(u1, p2, "Welcome!");
        dao.addComment(u2, p1, "I'm here, @johndoe!");
        */
        DataService dao = new DerbyDAO(sce.getServletContext());        
        sce.getServletContext().setAttribute("dao", dao);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
