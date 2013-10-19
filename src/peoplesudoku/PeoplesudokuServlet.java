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
		/*�إ�JSP�榡����*/
		resp.setContentType("text/plain");
		resp.getWriter().println("write bigtable and query data from bigtable sample");
		
		
		/*�إ߹��*/
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String content = "just test content";
        Date date = new Date();
        Greeting greeting = new Greeting(user, content, date);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        
        
        /*�g�JBigTable*/
        try {
            pm.makePersistent(greeting);
        } finally {
            pm.close();
        }

        
        /*��BigTable���X���*/
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
