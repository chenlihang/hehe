package cn.com.boomhope.common.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by pengxt on 2016/7/12.
 * RSA加解密
 */
public abstract class I2fRSACoder {

    //非对称加密密钥算法
    public static final String KEY_ALGORITHM="RSA";

    private static final byte[] HEX_CHAR_TABLE = {
            (byte)'0', (byte)'1', (byte)'2', (byte)'3',
            (byte)'4', (byte)'5', (byte)'6', (byte)'7',
            (byte)'8', (byte)'9', (byte)'a', (byte)'b',
            (byte)'c', (byte)'d', (byte)'e', (byte)'f'
    };

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 53;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 64;

    /**
     * 将字节序列转化为对应的十六进制字符串表示形式
     * @param raw 原始字节序列
     * @return 十六进制字符串
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getHexString(byte[] raw) throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw)
        {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    /**
     * 将十六进制的形式的字符串解析为字节序列
     */
    public static byte[] hexStringToByteArray(String s)
    {
        if(s == null || s.length() < 2)
        {
            return new byte[0];
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String secureStr, String privateKeyStr)throws Exception{
        //取得私钥
        byte[] privateKeyData = hexStringToByteArray(privateKeyStr);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(doFinal(cipher, MAX_DECRYPT_BLOCK, hexStringToByteArray(secureStr)));
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String clearStr, String publicKeyStr) throws Exception{
        //取得公钥
        byte[] publicKeyData = hexStringToByteArray(publicKeyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return getHexString(doFinal(cipher, MAX_ENCRYPT_BLOCK, clearStr.getBytes()));
    }

    public static byte[] doFinal(Cipher cipher, int maxLen, byte[] bytes) throws Exception{
        int inputLen = bytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxLen) {
                    cache = cipher.doFinal(bytes, offSet, maxLen);
                } else {
                    cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
                }
                byteArrayOutputStream.write(cache, 0, cache.length);
                i++;
                offSet = i * maxLen;
            }
            return byteArrayOutputStream.toByteArray();
        }finally {
            if(null != byteArrayOutputStream) {
                byteArrayOutputStream.close();
            }
        }

    }

    private static final String PUBLIC_KEY = "305c300d06092a864886f70d0101010500034b0030480241008c07c997ab5077bd735a0da71674d73a1e955e41ce59d29c949e356f582c37213c4cc933c6bff7648bd411edc009a0ae9c27a59654f8d888ad5382de945be2310203010001";
    private static final String PRIVATE_KEY = "30820154020100300d06092a864886f70d01010105000482013e3082013a0201000241008c07c997ab5077bd735a0da71674d73a1e955e41ce59d29c949e356f582c37213c4cc933c6bff7648bd411edc009a0ae9c27a59654f8d888ad5382de945be23102030100010240211ed26ac245751ce544be87efc2d10959248137d563df71d3a11c086af582504b7923d6c07aad37a6313faf997cb5acfb634d1620fa6e4459a321d080c8cdb1022100c5cfd7667d60fe4d8884fd3066cb469645d0c2f252adce38b7aed239cc71d18d022100b538bb6fa414c69d4e80f126f5a7123e3d00282977537df01b92097bf65980350221008b9139c9766d19005b7279b1ccd55fd8672a04623533ac6d182ca0e766f8678102204d2dcf2dd9bc7b65ed541ee00fc8ca8d351f86fb39f595f2175bd7ac329ba56d02204528043df11d5f3fd8d0fc94781af60a5c6c075a3d379665a49b3ab4a22c7d38";

    public static void main(String[] args) throws Exception{
        String clearStr = "加密字符串......asdsadasdasdasdasdasdas";
        String encodeStr = encryptByPublicKey(clearStr,PUBLIC_KEY);
        System.out.println("加密后字符串:" + encodeStr);
        String decodeStr = decryptByPrivateKey(encodeStr,PRIVATE_KEY);
        System.out.println("解密后字符串:" + decodeStr + ";与原句比较:" + decodeStr.equals(clearStr));
    }
}
