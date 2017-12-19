package rocky.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 移除session
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		// 把自动登陆的cookie设为空
		Cookie cookie_username= new Cookie("cookie_username", "");
		response.addCookie(cookie_username);
		// 重定向
		response.sendRedirect(request.getContextPath() + "/login.jsp");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}