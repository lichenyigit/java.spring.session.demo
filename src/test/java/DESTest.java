import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class DESTest {

    private final static String KEY_DES = "DESede";
    static String tool(byte[] b) {
        String ans = "";
        for (int i = 0; i < b.length; i++) {
            ans += String.format("%02X", b[i]);
        }

        return ans;
    }

    @Test
    public void test()
            throws NoSuchAlgorithmException, InvalidKeyException,
            NoSuchPaddingException, InvalidKeySpecException,
            IllegalBlockSizeException, BadPaddingException {
        SecureRandom secure = new SecureRandom("weidiao".getBytes());;
        KeyGenerator generator = KeyGenerator.getInstance(KEY_DES);
        generator.init(secure);
        byte[] key = generator.generateKey().getEncoded();
        // DESKeySpec dks = new DESKeySpec(key);
        // SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DES);
        // SecretKey secretKey = factory.generateSecret(dks);
        // 上述三行代码与下面这行代码等效,但是上面代码只适用于DES,而下面的代码可以适用于很多其它加密方式
        // 实际上,下面这行代码会自动调用上述代码
        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_DES);
        byte[] data = "123".getBytes();
        Cipher cipher = Cipher.getInstance(KEY_DES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypt = cipher.doFinal(data);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypt = cipher.doFinal(encrypt);


        System.out.println("密钥: " + tool(key));
        System.out.println("原始数据: " + tool(data));
        System.out.println("加密后: " + tool(encrypt));
        System.out.println("解密后:" + tool(decrypt));
    }

}
