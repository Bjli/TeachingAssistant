package web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import domain.FileInfo;
import service.BusinessService;
import service.impl.BusinessServiceImpl;
import util.IdGenerator;

public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusinessService business = new BusinessServiceImpl();
	private static Logger logger = Logger.getLogger(UserServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if ("checkFile".equals(operation)) {
			checkFile(request, response);
		}
		if ("deleteFile".equals(operation)) {
			deleteFile(request, response);
		}
		if ("downloadFile".equals(operation)) {
			downloadFile(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			uploadFile(request, response);
		}
		doGet(request, response);
	}

	// �����ļ�
	private void downloadFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		try {
			OutputStream os = response.getOutputStream();
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(name.getBytes("UTF-8"), "iso-8859-1"));
			response.setContentType("application/octet-stream");
			business.downloadFile(id, os);
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

	private void deleteFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		HttpSession session = request.getSession();
		try {
			business.deleteFile(id);
			logger.info((String) session.getAttribute("userID")+"do deleteFile,delete:"+id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			String errorMsg = "���ݿ�����쳣��������";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
		checkFile(request, response);
	}

	// �鿴�ļ�
	private void checkFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("userType");
		try {
			List<FileInfo> fList = business.checkFile();
			request.setAttribute("fList", fList);
			if (userType.equals("����Ա")) {
				request.getRequestDispatcher("/admin/checkFile.jsp").forward(request, response);
			} else if (userType.equals("��ʦ")) {
				request.getRequestDispatcher("/client/teacher/checkFile.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/client/student/checkFile.jsp").forward(request, response);
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
		} catch (ServletException e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}

	// �ϴ��ļ�
	private void uploadFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userID");
		FileInfo fileinfo = new FileInfo();
		fileinfo.setId(IdGenerator.genPrimaryKey());
		fileinfo.setUploaderId(userid);
		//���Ӱ�ʱ��洢�ļ�
		Date d = new Date();
		int day = d.getDate();
		int month = d.getMonth() + 1;
		int year = d.getYear() + 1900;
		StringBuffer sb = new StringBuffer();
		sb.append("" + year);
		if (month < 10) {
			sb.append("0" + month);
		} else {
			sb.append("" + month);
		}
		sb.append("" + day);
		String savePath = "D:\\Teaching_file\\"+sb.toString()+"\\";
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		fileinfo.setSavePath(savePath);
		
		
		String tempPath = "D:\\Teaching_file\\temp\\";
		File tmpFile = new File(tempPath);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(5 * 1024 * 1024);
		factory.setRepository(tmpFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		int maxSize = 50 * 1024 * 1024;// ���ϴ����ļ����Ϊ50M
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) { // ����ͨ��������
					String name = item.getFieldName();
					String value = item.getString("UTF-8");
					if (name.equals("uploader")) {
						fileinfo.setUploader(value);
					} else if (name.equals("uploadTime")) {
						fileinfo.setUploadTime(value);
					} else if (name.equals("description")) {
						fileinfo.setDescription(value);
					}
				} else { // ���ϴ��ļ�
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						request.setAttribute("message", "<script type='text/javascript'>alert('�ļ�������Ϊ��!')</script>");
						break;
					}
					if (item.getSize() > maxSize) {
						request.setAttribute("message", "<script type='text/javascript'>alert('�ļ�̫��')</script>");
						break;
					}
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					fileinfo.setName(filename);
					business.uploadFile(fileinfo, item);
					logger.info(userid+"do uploadFile,filename:"+filename);
					request.setAttribute("message", "<script type='text/javascript'>alert('�ϴ��ɹ���')</script>");

					InputStream is = item.getInputStream();
					FileOutputStream fos = new FileOutputStream(fileinfo.getSavePath()+ filename);
					byte[] buff = new byte[1024];
					int len = 0;
					while ((len = is.read(buff)) > 0) {
						fos.write(buff);
					}
					is.close();
					fos.close();
				}
			}
			try {
					request.getRequestDispatcher("/common/uploadFile.jsp").forward(request, response);
			} catch (IOException e) {
				logger.error(e.getMessage());
				String errorMsg = "IO�쳣,������";
				request.setAttribute("errorMsg", errorMsg);
				request.getRequestDispatcher("../common/error.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			String errorMsg = "δ֪�쳣�������Ի���ϵ����Ա";
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("../common/error.jsp").forward(request, response);
		}
	}
}
