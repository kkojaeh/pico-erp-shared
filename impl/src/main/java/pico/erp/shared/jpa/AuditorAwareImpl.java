package pico.erp.shared.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.data.AuthorizedUser;

public class AuditorAwareImpl implements AuditorAware<Auditor> {

  @Override
  public Auditor getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (null == authentication || !authentication.isAuthenticated()) {
      return new Auditor();
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof AuthorizedUser) {
      AuthorizedUser user = (AuthorizedUser) principal;
      return new Auditor(user.getUsername(), user.getName());
    }
    return new Auditor();
  }
}
