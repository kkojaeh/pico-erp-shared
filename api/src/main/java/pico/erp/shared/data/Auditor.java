package pico.erp.shared.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;

@Embeddable
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Auditor implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(max = TypeDefinitions.ID_LENGTH)
  @Column(length = TypeDefinitions.ID_LENGTH)
  String id = "anonymous";

  @Size(max = TypeDefinitions.NAME_LENGTH)
  @Column(length = TypeDefinitions.NAME_LENGTH)
  String name = "anonymous";

}
