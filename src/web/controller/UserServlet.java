package web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import domain.User;
import service.BusinessService;
import service.impl.BusinessServiceImpl;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusinessService business = new BusinessServiceImpl();
	private static Logger logger = Logger.getLogger(UserServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if ("login".equals(operation)) {
			login(request, response);
		}
		if ("logout".equals(operation)) {
			logout(request, response);
		}
		if ("addUser".equals(operation)) {
			addUser(request, response);
		}
		if ("checkUser".equals(operation)) {
			checkUser(request, response);
		}
		if ("deleteUser".equals(operation)) {
			deleteUser(request, response);
		}
		if ("findPWD".equals(operation)) {
			findPWD(request, response);
		}
		if ("modifyPWD".equals(operation)) {
			modifyPWD(request, response);
		}
	}

	// ע����¼
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);// ��ֹ����Session
		try {
			if (session == null) {
				response.sendRedirect("/TeachingAssistant/common/login.jsp");
			} else {
				session.removeAttribute("userID");
				session.removeAttribute("usetType");
				session.invalidate();
				response.sendRedirect("/TeachingAssistant/common/login.jsp");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	// �޸�����
	private void modifyPWD(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userID = (String) session.getAttribute("userID");
		String userType = (String) session.getAttribute("userType");
		String password = request.getParameter("password");
		User user = new User();
		user.setUserID(userID);
		user.setUserType(userType);
		user.setPassword(password);
		try {
			business.modifyPWD(user);
			request.setAttribute("message", "<script type='text/javascript'>alert('�޸ĳɹ�')</script>");
			logger.info(userID+": do modifyPWD");
			request.getRequestDispatcher("/common/modifyPWD.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	private void findPWD(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
			business.findPWD(user);
			request.setAttribute("message", "<script type='text/javascript'>alert('�����ѷ�����������䣡')</script>");
			logger.info(user.getUserID()+": do modifyPWD.");
			request.getRequestDispatcher("/common/login.jsp").forward(request, response);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		String loginRes = null;
		try {
			BeanUtils.populate(user, request.getParameterMap());
			loginRes = business.login(user);
			if (loginRes.equals("��¼�ɹ�")) {
				User login = business.getUser(user.getUserID());
				logger.info(login.getUserID()+"///"+login.getUserName()+":login_success");
				HttpSession session = request.getSession(true);
				// session.setMaxInactiveInterval(60);
				session.setAttribute("userID", login.getUserID());
				session.setAttribute("userName", login.getUserName());
				session.setAttribute("userType", login.getUserType());
				if (user.getUserType().equals("����Ա")) {
					response.sendRedirect("/TeachingAssistant/admin/mainFrm.jsp");
				} else if (user.getUserType().equals("��ʦ")) {
					response.sendRedirect("/TeachingAssistant/client/teacher/mainFrm.jsp");
				} else {
					response.sendRedirect("/TeachingAssistant/client/student/mainFrm.jsp");
				}
			} else if (loginRes.equals("��������")) {
				request.setAttribute("message", "<script type='text/javascript'>alert('��������')</script>");
				request.getRequestDispatcher("/common/login.jsp").forward(request, response);
			} else if (loginRes.equals("�����ڸ��û�")) {
				request.setAttribute("message",
						"<script type='text/javascript'>alert('�û������ڣ������û���Ż���ϵ����Ա��')</script>");
				request.getRequestDispatcher("/common/login.jsp").forward(request, response);
			} else if (loginRes.equals("�û����ʹ���")) {
				request.setAttribute("message", "<script type='text/javascript'>alert('��ѡ����ȷ���û����ͣ�')</script>");
				request.getRequestDispatcher("/common/login.jsp").forward(request, response);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	// ɾ���û�
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userID = request.getParameter("userID");
		String userType = request.getParameter("userType");
		User user = new User();
		user.setUserID(userID);
		user.setUserType(userType);
		try {
			business.deleteUser(user);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
		logger.info((String) session.getAttribute("userID")+":do deleteUser:"+userID);
		checkUser(request, response);
	}

	// �鿴�����û�
	private void checkUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<User> uList;
		try {
			uList = business.checkUser();
			request.setAttribute("uList", uList);
			request.getRequestDispatcher("/admin/checkUser.jsp").forward(request, response);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	// ����û�
	private void addUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userID");
		try {
			BeanUtils.populate(user, request.getParameterMap());
			business.addUser(user);
			request.setAttribute("message", "<script type='text/javascript'>alert('ע��ɹ���')</script>");
			logger.info("User:"+user.getUserID()+"register.");
			if (userId == "" || userId == null) {
				request.getRequestDispatcher("/common/login.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
