package rocky.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import rocky.dao.User;
import rocky.service.UserService;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService service = new UserService();
		User user = service.login(username,password);
		request.setAttribute("user", user);
		System.out.println(username+"/"+password+"/"+user);
		if (user!=null) {
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}else {
			request.setAttribute("loginError", "用户名或者密码错误！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}