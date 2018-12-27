package pico.erp.shared.data;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizedUser extends UserDetails {

  String getDepartmentName();

  String getEmail();

  String getMobilePhoneNumber();

  String getName();

  String getPosition();

  boolean hasAnyRole(String... roles);

  boolean hasRole(String role);

}
