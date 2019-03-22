package pico.erp.shared.jpa;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.data.AuthorizedUser;

public class AuditorAwareImpl implements AuditorAware<Auditor> {

  @Override
  public Optional<Auditor> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (null == authentication || !authentication.isAuthenticated()) {
      return Optional.of(new Auditor());
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof AuthorizedUser) {
      AuthorizedUser user = (AuthorizedUser) principal;
      return Optional.of(new Auditor(user.getUsername(), user.getName()));
    }
    return Optional.of(new Auditor());
  }
}
