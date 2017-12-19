package rocky.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("all")
public class BaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			// 1.获得请求的method名
			String methodName = request.getParameter("method");
			// 2.获得当前被访问的对象的字节码
			Class clazz = this.getClass();
			// 3.获得这个字节码对象中方法
			@SuppressWarnings("unchecked")
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			// 4.执行响应功能方法
			method.invoke(this, request,response);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}