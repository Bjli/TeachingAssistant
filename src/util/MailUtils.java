package util;

import org.apache.commons.mail.EmailException;  
import org.apache.commons.mail.HtmlEmail;

import domain.Mail;
  
public class MailUtils {  
    public boolean send(Mail mail) {  
        // ����email  
        HtmlEmail email = new HtmlEmail();  
        try {  
            // ������SMTP���ͷ����������֣�163�����£�"smtp.163.com"  
            email.setHostName(mail.getHost());  
            // �ַ����뼯������  
            email.setCharset(Mail.ENCODEING);  
            // �ռ��˵�����  
            email.addTo(mail.getReceiver());  
            // �����˵�����  
            email.setFrom(mail.getSender(), mail.getName());  
            // �����Ҫ��֤��Ϣ�Ļ���������֤���û���-���롣�ֱ�Ϊ���������ʼ��������ϵ�ע�����ƺ�����  
            email.setAuthentication(mail.getUsername(), mail.getPassword());  
            // Ҫ���͵��ʼ�����  
            email.setSubject(mail.getSubject());  
            // Ҫ���͵���Ϣ������ʹ����HtmlEmail���������ʼ�������ʹ��HTML��ǩ  
            email.setMsg(mail.getMessage());  
            // ����  
            email.send();  
            return true;  
        } catch (EmailException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

//    public static void main(String[] args) {  
//        Mail mail = new Mail();  
//        mail.setHost("mail.163.com"); // �����ʼ�������,�������163��,�Լ����ҿ���ص�  
//        mail.setSender("ljb_nwuer@163.com");  
//        mail.setReceiver("542871547@qq.com"); // ������  
//        mail.setUsername("ljb_nwuer@163.com"); // ��¼�˺�,һ�㶼�Ǻ�������һ����  
//        mail.setPassword("080230ljb"); // ����������ĵ�¼����  
//        mail.setSubject("�����һ�");  
//        mail.setMessage("ceshi");  
//        new MailUtils().send(mail);  
//    }
}  
