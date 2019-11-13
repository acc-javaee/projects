package edu.acc.jee.hubbub;

import edu.acc.jee.hubbub.domain.DataService;
import edu.acc.jee.hubbub.domain.Profile;
import edu.acc.jee.hubbub.domain.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AvatarController extends HttpServlet {
    @Override
    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subject = request.getParameter("for");
        if (subject == null || subject.length() == 0)
            subject = ((User)request.getSession().getAttribute("user")).getUsername();
        DataService dao = (DataService)this.getServletContext().getAttribute("dao");
        try {
            User target = dao.findUserByUsername(subject);
            int profileId = target.getProfileId();
            Profile profile = dao.findProfileById(profileId);
            String mime = profile.getMime();
            byte[] avatar = profile.getAvatar();
            if (mime == null || avatar == null) {
                response.sendRedirect("images/generic_avatar.png");
                return;
            }
            response.setContentType(mime);
            response.getOutputStream().write(avatar);
        } catch (NullPointerException e) {
            response.sendRedirect("images/generic_avatar.png");
        }
    }
}
