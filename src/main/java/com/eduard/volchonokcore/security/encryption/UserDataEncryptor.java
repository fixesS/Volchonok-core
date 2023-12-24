package com.eduard.volchonokcore.security.encryption;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

public class UserDataEncryptor implements AttributeConverter<String, String> {
    @Value("${encryption.algorithm.secretkey}")
    private String algorithm;
    @Value("${encryption.algorithm.cipher}")
    private String algorithmCipher;
    @Value("${encryption.secret}")
    private String secretKey;
    @Value("${encryption.initvector}")
    private String initVector;

    private Key key;
    private Cipher cipher;

    @PostConstruct
    public void init(){
        try{
            key = new SecretKeySpec(secretKey.getBytes(), algorithm);
            cipher = Cipher.getInstance(algorithmCipher);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        if(s == null){
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String s) {
        if(s == null){
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }
}
