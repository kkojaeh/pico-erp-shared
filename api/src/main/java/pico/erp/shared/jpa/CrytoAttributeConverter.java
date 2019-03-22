package pico.erp.shared.jpa;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

public class CrytoAttributeConverter implements AttributeConverter<String, String> {

  public static final String SECRET_PROPERTY_KEY = "JPA_ENCRYPTION_KEY";

  private static final String ALGORITHM = "AES/CFB/PKCS5Padding";

  private static byte[] KEY = null;

  static {
    String key = System.getenv(SECRET_PROPERTY_KEY);
    if (key == null || key.isEmpty()) {
      key = "-acepack-secret-";
    }
    KEY = key.getBytes();
  }

  @Override
  public String convertToDatabaseColumn(String sensitive) {
    if (sensitive == null) {
      return null;
    }
    if (sensitive.isEmpty()) {
      return "";
    }
    Key key = new SecretKeySpec(KEY, "AES");
    try {
      final Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(KEY));
      return new String(Base64.getEncoder().encode(c.doFinal(sensitive.getBytes())), "UTF-8");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String convertToEntityAttribute(String sensitive) {
    if (sensitive == null) {
      return null;
    }
    if (sensitive.isEmpty()) {
      return "";
    }
    Key key = new SecretKeySpec(KEY, "AES");
    try {
      final Cipher c = Cipher.getInstance(ALGORITHM);

      c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(KEY));
      return new String(c.doFinal(Base64.getDecoder().decode(sensitive.getBytes("UTF-8"))));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
