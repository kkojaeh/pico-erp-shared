package pico.erp.shared.jpa;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslJpaSupport {

  <RT> Page<RT> paging(JPAQuery<RT> query, Pageable pageable, Expression<?>... expression);

  <RT> Page<RT> paging(JPAQuery<RT> query, Pageable pageable, Expression<RT> expression);

  String toLikeKeyword(String left, String keyword, String right);

}
