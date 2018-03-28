package domain;

import java.io.Serializable;

public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userId;//ѧ��
	private String userName;//����
	private String workId;//��Ӧ��ҵ��ʶ
	private String workTitle;//��ҵ����
	private String teacherName;//��Ӧ��ʦ������
	private String teacherId;//��Ӧ��ʦ����
	private int score;//�ɼ�
	private String remark;//����
 
	
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getUserName() {
		return userName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getWorkTitle() {
		return workTitle;
	}
	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}
	public String getUserId() {
		return userId;
	}
	public String getWorkId() {
		return workId;
	}
	public int getScore() {
		return score;
	}
	public String getRemark() {
		return remark;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
