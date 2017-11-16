import java.security.MessageDigest;

public class MD5Util {
    public static final char[] key = {'l', 'i', 'o', 'n'};

    // 可逆的加密算法
    public static String KL(String inStr) {
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
    public static String JM(String inStr) {
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

    // 测试主函数
    public static void main(String args[]) {
        String s = new String("lichenyi!@#$%^&*()_+");
        System.out.println("原始：" + s);
        /*
        System.out.println("MD5后：" + MD5(s));
        System.out.println("MD5后再加密：" + KL(MD5(s)));
        System.out.println("解密为MD5后的：" + JM(KL(MD5(s))));
        */
        System.out.println("加密后：" + KL(s));
        System.out.println("解密后的：" + JM(KL(s)));
    }
}