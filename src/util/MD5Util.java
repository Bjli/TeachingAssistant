package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private MD5Util() {
    }
    public static String getMD5(String originString) {
        String result = null;
        if (originString != null) {
            try {
                // ָ�����ܵķ�ʽΪMD5
                MessageDigest md = MessageDigest.getInstance("MD5");
                // ���м�������
                byte bytes[] = md.digest(originString.getBytes());
                for (int i = 0; i < bytes.length; i++) {
                    // ������ת����ʮ��������ʽ���ַ��� ������0xff�����������ԭ���Ǳ�֤ת�����Ϊ32λ
                    String str = Integer.toHexString(bytes[i] & 0xFF);
                    if (str.length() == 1) {
                        str += "F";
                    }
                    result += str;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result.toUpperCase();
    }
}
