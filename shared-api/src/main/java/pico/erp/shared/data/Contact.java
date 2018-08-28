package pico.erp.shared.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.jpa.CrytoAttributeConverter;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(max = TypeDefinitions.NAME_LENGTH)
  @Column(length = TypeDefinitions.NAME_LENGTH)
  private String name;

  @Size(max = TypeDefinitions.EMAIL_LENGTH)
  @Column(length = TypeDefinitions.EMAIL_LENGTH * 2)
  @Convert(converter = CrytoAttributeConverter.class)
  private String email;

  @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
  @Column(length = TypeDefinitions.PHONE_NUMBER_LENGTH * 2)
  @Convert(converter = CrytoAttributeConverter.class)
  private String telephoneNumber;

  @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
  @Column(length = TypeDefinitions.PHONE_NUMBER_LENGTH * 2)
  @Convert(converter = CrytoAttributeConverter.class)
  private String mobilePhoneNumber;

  @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
  @Column(length = TypeDefinitions.PHONE_NUMBER_LENGTH * 2)
  @Convert(converter = CrytoAttributeConverter.class)
  private String faxNumber;

}
