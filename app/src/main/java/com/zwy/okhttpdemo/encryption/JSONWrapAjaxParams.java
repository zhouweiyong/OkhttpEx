package com.zwy.okhttpdemo.encryption;

import android.util.Base64;

import com.huika.xokhttp.params.AjaxParams;
import com.huika.xokhttp.params.encryption.ParamEncryptor;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/13.
 */
public class JSONWrapAjaxParams extends AjaxParams {


    private static String PUBLIC_KEY_VALUE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3//sR2tXw0wrC2DySx8vNGlqt3Y7ldU9+LBLI6e1KS5lfc5jlTGF7KBTSkCHBM3ouEHWqp1ZJ85iJe59aF5gIB2klBd6h4wrbbHA2XE1sq21ykja/Gqx7/IRia3zQfxGv/qEkyGOx+XALVoOlZqDwh76o2n1vP1D+tD3amHsK7QIDAQAB";
    //public static PublicKey publicKey = RsaHelper.decodePublicKeyFromXml(PUBLIC_KEY_VALUE);

    public JSONWrapAjaxParams() {
        super();

        //添加rsa加密字段
        initEncrypt(new ParamEncryptor() {

            @Override
            public String encrypt(Object o) {
//                return RsaHelper.encryptDataFromStr(o.toString(), publicKey);
                return encryptByPublic(o.toString(), PUBLIC_KEY_VALUE);
            }
        }, "password","oldPassword","myName");
        addEncrypt("userId");
        addEncrypt("productId");
        addEncrypt("activityId");
        addEncrypt("userName");
        addEncrypt("payPwd");
        addEncrypt("phone");
        addEncrypt("amount");
    }

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }


    public static String encryptByPublic(String content,String PUBLIC_KEY) {
        try {
            PublicKey pubkey = getPublicKeyFromX509("RSA", PUBLIC_KEY);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            String s = new String(Base64.encode(output, Base64.DEFAULT));

            return s;

        } catch (Exception e) {
            return null;
        }
    }

}
