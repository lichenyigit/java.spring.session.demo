package session.demo.util;

import javax.servlet.http.Cookie;
import java.security.MessageDigest;
import java.util.*;

public class CommonUtil {

    private static final String FIELD_SIGN = "sign";
    private static final String FIELD_SIGN_VALUE = "lion@session";
    public static final char[] key = {'l', 'i', 'o', 'n'};

    public static boolean isSignatureValid(Map<String, Object> data) throws Exception {
        return isSignatureValid(data, FIELD_SIGN_VALUE);
    }
    public static boolean isSignatureValid(Map<String, Object> data, String key) throws Exception {
        if (!data.containsKey("key") ) {
            return false;
        }
        String sign = data.get(FIELD_SIGN).toString();
        return generateSignatureMD5(data, key).equals(sign);
    }

    public static String generateSignatureMD5(final Map<String, Object> data) throws Exception {
        return generateSignatureMD5(data, FIELD_SIGN_VALUE);
    }
    public static String generateSignatureMD5(final Map<String, Object> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("key")) {
                continue;
            }
            if (data.get(k).toString().trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();
    }

    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static String generateSessionId(){
        /*
        StringBuffer result = new StringBuffer("lion-session");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        result.append(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", calendar.getTime()));
        result.append(generateNonceStr(18));
        return result.toString();
        */
        return generateNonceStr();
    }

    public static String generateNonceStr() {
        return UUID.randomUUID().toString();
    }

    // 可逆的加密算法
    public static String jiaMi(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            for(int j = 0;j < key.length; j++){
                char c = key[j];
                a[i] = (char) (a[i] ^ c);
            }
        }
        String s = new String(a);
        return s;
    }

    // 加密后解密
    public static String jieMi(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            for(int j = 0;j < key.length; j++){
                char c = key[j];
                a[i] = (char) (a[i] ^ c);
            }
        }
        String k = new String(a);
        return k;
    }

    //************************************************************************************
    //**************************** cookie util
    //************************************************************************************

    public static String getCookie(Cookie[] cookies, String name){
        if(cookies == null)
            return null;
        for (Cookie cookie : cookies){
            if(!name.equals(cookie.getName())){
                continue;
            }
            return cookie.getValue();
        }
        return null;
    }

    public static Cookie setCookie(String name, String value, String domain){
        Cookie cookie = new Cookie("lion-cms", "token");
        cookie.setDomain(domain);
        return cookie;
    }

}
