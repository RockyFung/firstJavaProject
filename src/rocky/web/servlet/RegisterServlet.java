package rocky.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import rocky.dao.User;
import rocky.service.UserService;
import rocky.utils.CommonsUtils;
import rocky.utils.MailUtils;

public class RegisterServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		// 获取数据
		Map<String, String[]> parameterMap = request.getParameterMap();
		User user = new User();
		try {
			BeanUtils.populate(user, parameterMap);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 手动设置其他属性
		user.setUid(CommonsUtils.getUUID());
		user.setTelephone("12345678900");
		user.setState(0);//未激活
		// 激活码
		String activeCode = CommonsUtils.getUUID();
		user.setCode(activeCode);
		
		// 传递数据
		UserService service = new UserService();
		boolean isSuccess = false;
		try {
			isSuccess = service.register(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 判断
		if (isSuccess) {
			//发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户\n"
					+ "<a href='http://localhost:8080/Shop/active?activeCode="+activeCode+"'>"
							+ "http://localhost:8080/Shop/active?activeCode="+activeCode+"</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else {
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}