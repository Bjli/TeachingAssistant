package dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import dao.GradeDao;
import domain.Grade;
import util.DBCPUtil;

public class GradeDaoImpl implements GradeDao {
	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	//�ϴ��ɼ�
	public void inputGrade(Grade grade) throws SQLException {
		String sql="insert into Grade(userId,username,workId,workTitle,teacherId,teachername,score,remark) values(?,?,?,?,?,?,?,?)";
		String sql2="update answer set state=? where workId=? and userId=?";
		qr.update(sql,grade.getUserId(),grade.getUserName(),grade.getWorkId(),grade.getWorkTitle(),grade.getTeacherId(),grade.getTeacherName(),grade.getScore(),grade.getRemark());
		qr.update(sql2,"������",grade.getWorkId(),grade.getUserId());
	}

	//����Ա�鿴�ɼ�
	public List<Grade> aCheckGrade() throws SQLException {
		String sql="select * from Grade order by teacherid desc";
		return qr.query(sql, new BeanListHandler<Grade>(Grade.class));
	}
	
	//��ʦ��ȡ�ɼ���
	public List<Grade> tCheckGrade(String teacherId) throws SQLException {
		String sql="select * from Grade where teacherId=?";
		return qr.query(sql, new BeanListHandler<Grade>(Grade.class),teacherId);
	}
	
	//ѧ����ȡ���˳ɼ���ϸ��Ϣ
	public List<Grade> getGrade(String studentID) throws SQLException {
		return qr.query("select * from Grade where userID=?", new BeanListHandler<Grade>(Grade.class),studentID);
	}
	
	//��ʦͨ��ѧ�Ż�ȡ���˳ɼ�
	public List<Grade> tGetGradeByUid(String studentID,String teacherId) throws SQLException {
		return qr.query("select * from Grade where userID=? and teacherId=?", new BeanListHandler<Grade>(Grade.class),studentID,teacherId);
	}
	//��ʦͨ����ҵ��ȡ��������
	public 	List<Grade> tGetGradeByTitle(String workTitle,String teacherId) throws SQLException {
		return qr.query("select * from Grade where workTitle=? and teacherId=?",new BeanListHandler<Grade>(Grade.class), workTitle,teacherId);
	}


	//ɾ���ɼ�
	public void deleteGrade(String userid,String workId) throws SQLException {
		String sql="delete from Grade where userId=? and workId=?";
		qr.update(sql,userid,workId);
		String sql2="update answer set state=? where workId=? and userId=?";
		qr.update(sql2,"���ύ",workId,userid);
	}

	//�޸ĳɼ�
	public void modifyGrade(Grade grade) throws SQLException {
		String sql="update Grade set score=?,remark=? where userID=? and workTitle=?";
		qr.update(sql,grade.getScore(),grade.getRemark(), grade.getUserId(), grade.getWorkTitle());
	}
}
