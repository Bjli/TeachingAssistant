package domain;

import java.io.Serializable;

public class FileInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//Ψһ��ʶ
	private String name;//�ļ���������
	private String uploadTime;//�ϴ�����
	private String savePath;//�ļ�����Ŀ¼
	private String description;//�ļ�����
	private String uploader;//�ϴ���
	private String uploaderId;//�ϴ��߹���
	
	
	
	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}
	public String getUploaderId() {
		return uploaderId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
}
