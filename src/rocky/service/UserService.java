package rocky.service;

import java.sql.SQLException;

import rocky.dao.User;
import rocky.dao.UserDao;

public class UserService {

	public boolean register(User user) throws SQLException {
		UserDao dao = new UserDao();
		int row = dao.register(user);
		return row>0?true:false;
	}

	// 激活
	public void active(String activeCode) {
		UserDao dao = new UserDao();
		try {
			dao.active(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// 校验用户名是否存在
	public boolean checkUsername(String username) {
		UserDao dao = new UserDao();
		Long row = 0L;
		try {
			row = dao.checkUsername(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row>0?true:false;
	}

}
