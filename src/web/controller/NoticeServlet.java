package web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import domain.Notice;
import service.BusinessService;
import service.impl.BusinessServiceImpl;
import util.IdGenerator;

public class NoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusinessService business=new BusinessServiceImpl();
	private static Logger logger = Logger.getLogger(NoticeServlet.class); 
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation=request.getParameter("operation");
		if("releaseNotice".equals(operation)){
			releaseNotice(request,response);
		}
		if("checkNotice".equals(operation)){
			checkNotice(request,response);
		}
		if("deleteNotice".equals(operation)){
			deleteNotice(request,response);
		}
		if("getNotice".equals(operation)){
			getNotice(request,response);
		}
	}
	//��ȡ�ض�id��֪ͨ����
	private void getNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		HttpSession session=request.getSession();
		String userType=(String)session.getAttribute("userType");
		Notice notice=null;
		try {
			notice = business.getNotice(id);
			request.setAttribute("notice", notice);
			if(userType.equals("ѧ��")){
				request.getRequestDispatcher("/client/student/addAnswer.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/common/getNotice.jsp").forward(request, response);
			}
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
	//ɾ���ص�id��֪ͨ
	private void deleteNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		HttpSession session=request.getSession();
		try {
			business.deleteNotice(id);
			logger.info((String)session.getAttribute("userID")+"do deleteNotice,notice_id:"+id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
		checkNotice(request, response);
	}
	//��ȡ֪ͨ�б�
	private void checkNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String userType=(String)session.getAttribute("userType");
		List<Notice> nList=null;
		try {
			nList = business.checkNotice();
			request.setAttribute("nList", nList);
			if(userType.equals("����Ա")){
				request.getRequestDispatcher("/admin/checkNotice.jsp").forward(request, response);
			}
			else if(userType.equals("��ʦ")){
				request.getRequestDispatcher("/client/teacher/checkNotice.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/client/student/checkNotice.jsp").forward(request, response);
			}
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
	//����֪ͨ
	private void releaseNotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String userId=(String)session.getAttribute("userID");
		Notice notice=new Notice();
		notice.setId(IdGenerator.genPrimaryKey());
		notice.setAuthorId(userId);
		try {
			BeanUtils.populate(notice, request.getParameterMap());
			business.releaseNotice(notice);
			logger.info(userId+":do releaseNotice,notice_ID:"+notice.getId());
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			String errorMsg = "����ת�������������ڸ�ʽ";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		} 
		request.setAttribute("message", "<script type='text/javascript'>alert('�����ɹ���')</script>");
		try {
				request.getRequestDispatcher("/common/releaseNotice.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}
}
