package com.eduard.volchonokcore.security.encryption;

import com.eduard.volchonokcore.config.SecurityConfig;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Attr;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

public class ColumnEncryptor implements AttributeConverter<String, String> {
    @Value("${encryption.algorithm}")
    private String algorithm;
    @Value("${encryption.secret}")
    private String secretKey;

    private final Key key;
    private final Cipher cipher;

    public ColumnEncryptor() throws Exception{
        key = new SecretKeySpec(secretKey.getBytes(), algorithm);
        cipher = Cipher.getInstance(algorithm);
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String s) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}
