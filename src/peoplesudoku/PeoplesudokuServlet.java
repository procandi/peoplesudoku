package peoplesudoku;

import java.io.IOException;

import javax.servlet.http.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import peoplesudoku.Greeting;
import peoplesudoku.PMF;


@SuppressWarnings("serial")
public class PeoplesudokuServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*建立JSP格式說明*/
		resp.setContentType("text/plain");
		resp.getWriter().println("write bigtable and query data from bigtable sample");
		
		
		/*建立實例*/
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String content = "just test content";
        Date date = new Date();
        Greeting greeting = new Greeting(user, content, date);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        
        
        /*寫入BigTable*/
        try {
            pm.makePersistent(greeting);
        } finally {
            pm.close();
        }

        
        /*由BigTable撈出資料*/
        pm = PMF.get().getPersistenceManager();
        String query = "select from " + Greeting.class.getName();
        List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute();
        if (greetings.isEmpty()) {
        	resp.getWriter().println("Not have anymore data for show");
        }else{
        	for (Greeting g : greetings) {
                if (g.getAuthor() == null) {
                	resp.getWriter().println("An anonymous person ever login");
                	resp.getWriter().println(g.getDate());
                	resp.getWriter().println();
                }else {
                	resp.getWriter().println(g.getAuthor().getNickname());
                	resp.getWriter().println(g.getDate());
                	resp.getWriter().println();
                }
        	}
        }

        
		
		
	}
}
