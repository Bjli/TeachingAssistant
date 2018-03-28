package domain;

import java.io.Serializable;

public class AnswerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String answerid;//�����ʶid
	private String userid;//�ش���id
	private String username;//�ش�������
	private String date;//����ʱ��
	private String content;//��ҵ����
	private String workid;//��ҵ��ʶid��������
	private String worktitle;//��ҵ���⣨������
	private String workuser;//��ҵ������
	private String workuserid;//��ҵ������id
	private String state;// ���ύ/������
	

	public String getWorkuserid() {
		return workuserid;
	}

	public void setWorkuserid(String workuserid) {
		this.workuserid = workuserid;
	}


	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getWorkuser() {
		return workuser;
	}

	public String getState() {
		return state;
	}

	public void setWorkuser(String workuser) {
		this.workuser = workuser;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWorkid() {
		return workid;
	}

	public String getWorktitle() {
		return worktitle;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public void setWorktitle(String worktitle) {
		this.worktitle = worktitle;
	}

	public void setAnswerid(String answerid) {
		this.answerid = answerid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setDate(String releaseDate) {
		this.date = releaseDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnswerid() {
		return answerid;
	}

	public String getUserid() {
		return userid;
	}

	public String getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

}
