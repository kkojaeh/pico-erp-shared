package pico.erp.shared.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(max = TypeDefinitions.ADDRESS_POSTAL_LENGTH)
  @Column(length = TypeDefinitions.ADDRESS_POSTAL_LENGTH)
  private String postalCode;

  @Size(max = TypeDefinitions.ADDRESS_STREET_LENGTH)
  @Column(length = TypeDefinitions.ADDRESS_STREET_LENGTH)
  private String street;

  @Size(max = TypeDefinitions.ADDRESS_DETAIL_LENGTH)
  @Column(length = TypeDefinitions.ADDRESS_DETAIL_LENGTH)
  private String detail;

}
