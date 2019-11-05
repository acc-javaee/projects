package edu.acc.jee.hubbub;

import edu.acc.jee.hubbub.domain.DataService;
import edu.acc.jee.hubbub.domain.Post;
import edu.acc.jee.hubbub.domain.Profile;
import edu.acc.jee.hubbub.domain.User;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
    private String actionDefault;
    private String viewDir;
    private String viewType;
    private String redirectTag;
    private int pageSize;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action"), destination = actionDefault;
        if (action == null) action = actionDefault;
        switch (action) {
            default:
            case "guest": destination = guest(request); break;
            case "login": destination = login(request); break;
            case "logout": destination = logout(request); break;
            case "join": destination = join(request); break;
            case "timeline": destination = timeline(request); break;
            case "post": destination = post(request); break;
            case "comments": destination = comments(request); break;
            case "profile": destination = profile(request); break;
        }
        
        String view;
        if (destination.startsWith(redirectTag)) {
            view = destination.substring(redirectTag.length());
            response.sendRedirect("main?action=" + view);
        }
        else {
            view = viewDir + '/' + destination + viewType;
            request.getRequestDispatcher(view).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    public void init() {
        ServletContext ctx = this.getServletContext();
        actionDefault = ctx.getInitParameter("action.default");
        viewDir = ctx.getInitParameter("view.dir");
        viewType = ctx.getInitParameter("view.type");
        redirectTag = ctx.getInitParameter("redirect.tag");
        pageSize = Integer.parseInt(ctx.getInitParameter("page.size"));
    }
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String guest(HttpServletRequest request) {
        if (loggedIn(request)) return redirectTag + "timeline";
        List<Post> posts = getDataService().findPostsByPage(0, pageSize);
        request.setAttribute("posts", posts);
        return "guest";
    }
    
    private String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return redirectTag + "guest";
    }

    private String timeline(HttpServletRequest request) {
        if (!loggedIn(request)) return redirectTag + "guest";
        List<Post> posts = getDataService().findPostsByPage(0, pageSize);
        request.setAttribute("posts", posts);
        return "timeline";       
    }
 
    private String login(HttpServletRequest request) {
        // logged in users can't log in again
        if (loggedIn(request)) return redirectTag + "timeline";
        // GETs to this action are requesting the form
        if (request.getMethod().equalsIgnoreCase("GET")) return "login";
        // Must be a POST if we get here; build a DTO and validate
        UserDTO dto = new UserDTO(
                request.getParameter("username"),
                request.getParameter("password")
        );
        if (!dto.isValid()) {
            request.setAttribute("flash", "Access Denied!");
            return "login";
        }
        // DTO is valid; is this a user and do they know their password
        DataService dao = this.getDataService();
        User user = dao.findUserByUserDTO(dto);
        if (user == null) {
            request.setAttribute("flash", "Access Denied!");
            return "login";
        }
        // User is authentic; start a session and redirect to timeline
        request.getSession().setAttribute("user", user);
        return redirectTag + "timeline";
    }

    private String join(HttpServletRequest request) {
        if (loggedIn(request)) return redirectTag + "timeline";
        if (request.getMethod().equalsIgnoreCase("GET")) return "join";
        String username = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if (password1 == null || password2 == null || !password1.equals(password2)) {
            request.setAttribute("flash", "Passwords don't match.");
            return "join";
        }
        UserDTO dto = new UserDTO(username, password1);
        if (!dto.isValid()) {
            request.setAttribute("flash", "Username or password is invalid.");
            return "join";
        }
        DataService dao = this.getDataService();
        if (dao.userExists(dto.getUsername())) {
            request.setAttribute("flash", "That username is unavailable.");
            return "join";
        }
        User user = dao.addUser(dto);
        request.getSession().setAttribute("user", user);
        return redirectTag + "timeline";
    }

    private String post(HttpServletRequest request) {
        User user = this.getSessionUser(request);
        if (user == null) return redirectTag + "guest";
        if (request.getMethod().equalsIgnoreCase("GET")) return "post";
        String content = request.getParameter("content");
        if (content == null || content.length() < 1) {
            request.setAttribute("flash", "Your post was empty.");
            return "post";
        }
        if (content.length() > 140) {
            request.setAttribute("flash", "There's too much post in yer post! 140 characters max, please.");
            return "post";
        }
        this.getDataService().addPost(content, user);
        return redirectTag + "timeline";       
    }
    
    private String comments(HttpServletRequest request) {
        if (!loggedIn(request)) return redirectTag + "guest";
        int postId = Integer.parseInt(request.getParameter("post"));
        Post post = getDataService().findPostById(postId);
        if (post == null) {
            request.setAttribute("flash", "Blurb&trade; ID not found: " + postId);
            return "comments";
        }
        request.setAttribute("post", post);
        if (request.getMethod().equalsIgnoreCase("GET")) return "comments";
        User author = getSessionUser(request);
        String content = request.getParameter("content");
        if (content != null || content.length() > 0 && content.length() <= 70)
            this.getDataService().addComment(author, post, content);
        else request.setAttribute("flash", "Your comment must have content.");
        return "comments";
    }
    
    private String profile(HttpServletRequest request) {
        if (!loggedIn(request)) return redirectTag + "guest";
        String forName = request.getParameter("for");
        User target = getDataService().findUserByUsername(forName);
        request.setAttribute("timeZones", getTimeZones());
        if (target == null) {
            request.setAttribute("flash", "No such Bub&trade;: " + forName);
            return "profile";
        }
        if (request.getMethod().equalsIgnoreCase("GET")) {
            request.setAttribute("target", target);
            return "profile";
        }
        User user = getSessionUser(request);
        if (!user.equals(target)) return redirectTag + "timeline";
        Profile temp = new Profile();
        temp.setFirstName(request.getParameter("firstName"));
        temp.setLastName(request.getParameter("lastName"));
        temp.setEmail(request.getParameter("email"));
        temp.setTimeZone(request.getParameter("timeZone"));
        temp.setBiography(request.getParameter("biography"));
        boolean ok = getDataService().updateProfileFor(user, temp);
        request.setAttribute("target", user);        
        if (ok) request.setAttribute("success", "Deets&trade; Updated");
        else request.setAttribute("flash", "Error updating your Deets&trade;.");
        return "profile";
    }
    
    @SuppressWarnings("unchecked")
    private DataService getDataService() {
        return (DataService)this.getServletContext().getAttribute("dao");
    }

    private boolean loggedIn(HttpServletRequest request) {
         return getSessionUser(request) != null;
    }

    @SuppressWarnings("unchecked")
    private User getSessionUser(HttpServletRequest request) {
        return (User)request.getSession().getAttribute("user");
    }
    
    private List<String> getTimeZones() {
        return ZoneId.SHORT_IDS.values()
                .stream()
                .filter((id) -> !id.startsWith("-"))
                //.sorted()
                .collect(Collectors.toList());
    }

}
