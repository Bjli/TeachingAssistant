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

import domain.AnswerInfo;
import service.BusinessService;
import service.impl.BusinessServiceImpl;
import util.IdGenerator;

public class AnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusinessService business = new BusinessServiceImpl();
	private static Logger logger = Logger.getLogger(AnswerServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation=request.getParameter("operation");
		if("addAnswer".equals(operation)){
			addAnswer(request, response);
		}
		if("checkAnswer".equals(operation)){
			checkAnswer(request, response);
		}
		if("deleteAnswer".equals(operation)){
			deleteAnswer(request, response);
		}
		if("getAnswer".equals(operation)){
			getAnswer(request, response);
		}
	}

	
	//ͨ����ʶid����ȡ��������
	private void getAnswer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		HttpSession session=request.getSession();
		String userType=(String)session.getAttribute("userType");
		AnswerInfo answerinfo = null;
		try{
		answerinfo = business.getAnswer(id);
		request.setAttribute("answerinfo", answerinfo);
		if(userType.equals("ѧ��")){
			request.getRequestDispatcher("/client/student/getAnswer.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/client/teacher/inputGrade.jsp").forward(request, response);
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
		
	// ɾ���ض�id����ҵ��¼
	private void deleteAnswer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			business.deleteAnswer(id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
		checkAnswer(request, response);
	}
	// ��ȡ��ҵ�б�
	private void checkAnswer(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			HttpSession session=request.getSession();
			String userType=(String)session.getAttribute("userType");
			List<AnswerInfo> nList=null;
			try {
	            if(userType.equals("��ʦ")){
					nList = business.checkAnswerT((String)session.getAttribute("userID"));
					request.setAttribute("nList", nList);
	            	request.getRequestDispatcher("/client/teacher/checkAnswer.jsp").forward(request, response);
				}
				else {
					nList = business.checkAnswerS((String)session.getAttribute("userID"));
					request.setAttribute("nList", nList);
					request.getRequestDispatcher("/client/student/checkAnswer.jsp").forward(request, response);
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
	// �ش���ҵ
	private void addAnswer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AnswerInfo answer = new AnswerInfo();
		answer.setAnswerid(IdGenerator.genPrimaryKey());
		HttpSession session=request.getSession();
		answer.setUsername((String)session.getAttribute("userName"));
		try {
			BeanUtils.populate(answer, request.getParameterMap());
			business.addAnswer(answer);
			logger.info((String)session.getAttribute("userID")+"do addAnswer,for:"+answer.getAnswerid());
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
		request.setAttribute("message", "<script type='text/javascript'>alert('�ύ�ɹ���')</script>");
		try {
			request.getRequestDispatcher("/client/student/addAnswer.jsp").forward(request, response);
		} catch (IOException e) {
			logger.error(e.getMessage());
			String errorMsg = "IO�쳣,������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}
}
