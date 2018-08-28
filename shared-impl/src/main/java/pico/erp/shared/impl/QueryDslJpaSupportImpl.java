package pico.erp.shared.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.Assert;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@SuppressWarnings("unchecked")
public class QueryDslJpaSupportImpl implements QueryDslJpaSupport {

  public char escapeChar = '\\';

  public QueryDslJpaSupportImpl(char escapeChar) {
    this.escapeChar = escapeChar;
  }

  @Override
  public <RT> Page<RT> paging(JPAQuery<RT> query, Pageable pageable, Expression<?>... expression) {
    long total = query.fetchCount();
    QueryDslSupport<RT> support = new QueryDslSupport<RT>(expression);
    support.applyPagination(pageable, query);
    query.select(expression);
    List<RT> content = pageable == null || total > pageable.getOffset() ? query.fetch()
      : Collections.emptyList();
    return new PageImpl<RT>(content, pageable, total);
  }

  @Override
  public <RT> Page<RT> paging(JPAQuery<RT> query, Pageable pageable, Expression<RT> expression) {
    long total = query.fetchCount();
    QueryDslSupport<RT> support = new QueryDslSupport<>(expression);
    support.applyPagination(pageable, query);
    query.select(expression);
    List<RT> content = pageable == null || total > pageable.getOffset() ? query.fetch()
      : Collections.emptyList();
    return new PageImpl<RT>(content, pageable, total);
  }

  @Override
  public String toLikeKeyword(String left, String keyword, String right) {
    keyword = keyword.replace("%", escapeChar + "%");
    keyword = keyword.replace("_", escapeChar + "_");
    return left + keyword + right;
  }

  private static class QueryDslSupport<E> {

    private final Expression<E> expression;

    public QueryDslSupport(Expression<E> e) {
      expression = e;
    }

    @SuppressWarnings("unchecked")
    public QueryDslSupport(Expression<?>... o) {
      expression = (Expression<E>) Projections.tuple(o);
    }

    public <T> JPQLQuery<T> addOrderByFrom(QSort qsort, JPQLQuery<T> query) {

      List<OrderSpecifier<?>> orderSpecifiers = qsort.getOrderSpecifiers();
      return query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]));
    }

    public <T> JPQLQuery<T> addOrderByFrom(Sort sort, JPQLQuery<T> query) {

      Assert.notNull(sort, "Sort must not be null!");
      Assert.notNull(query, "Query must not be null!");

      for (Sort.Order order : sort) {
        query.orderBy(toOrderSpecifier(order));
      }

      return query;
    }

    public <T> JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query) {

      if (pageable == null) {
        return query;
      }

      query.offset(pageable.getOffset());
      query.limit(pageable.getPageSize());

      return applySorting(pageable.getSort(), query);
    }

    public <T> JPQLQuery<T> applySorting(Sort sort, JPQLQuery<T> query) {

      if (sort == null) {
        return query;
      }

      if (sort instanceof QSort) {
        return addOrderByFrom((QSort) sort, query);
      }

      return addOrderByFrom(sort, query);
    }

    private Expression<?> buildOrderPropertyPathFrom(Sort.Order order) {

      Assert.notNull(order, "Order must not be null!");

      PropertyPath path = PropertyPath.from(order.getProperty(), expression.getType());
      Expression<?> sortPropertyExpression = expression;

      while (path != null) {

        if (!path.hasNext() && order.isIgnoreCase()) {
          // if order is ignore-case we have to treat the last path segment as a String.
          sortPropertyExpression =
            Expressions.stringPath((Path<?>) sortPropertyExpression, path.getSegment()).lower();
        } else if (sortPropertyExpression instanceof Path) {
          sortPropertyExpression =
            Expressions.path(path.getType(), (Path<?>) sortPropertyExpression, path.getSegment());
        } else if (sortPropertyExpression instanceof QBean) {
          QBean bean = (QBean) sortPropertyExpression;
          String segment = path.getSegment();
          Path beanPath = ExpressionUtils.path(path.getType(), path.getSegment());
          List<Expression> args = bean.getArgs();
          sortPropertyExpression = args.stream()
            .filter(e -> e instanceof Path)
            .map(e -> (Path) e)
            .filter(p -> segment.equals(p.getMetadata().getName()))
            .findFirst()
            .orElse(null);
          if (sortPropertyExpression == null) {
            sortPropertyExpression = args.stream()
              .filter(e -> e instanceof Operation)
              .map(e -> (Operation) e)
              .filter(o -> o.getOperator().equals(Ops.ALIAS) && o.getArg(1).equals(beanPath))
              .map(o -> o.getArg(0))
              .findFirst()
              .orElse(null);
          }
        } else {
          sortPropertyExpression = Expressions.path(path.getType(), path.getSegment());
        }

        path = path.next();
      }

      return sortPropertyExpression;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public OrderSpecifier<?> toOrderSpecifier(Sort.Order order) {

      return new OrderSpecifier(
        order.isAscending() ? com.querydsl.core.types.Order.ASC
          : com.querydsl.core.types.Order.DESC,
        buildOrderPropertyPathFrom(order), toQueryDslNullHandling(order.getNullHandling()));
    }

    private OrderSpecifier.NullHandling toQueryDslNullHandling(
      Sort.NullHandling nullHandling) {

      Assert.notNull(nullHandling, "NullHandling must not be null!");

      switch (nullHandling) {

        case NULLS_FIRST:
          return OrderSpecifier.NullHandling.NullsFirst;

        case NULLS_LAST:
          return OrderSpecifier.NullHandling.NullsLast;

        case NATIVE:
        default:
          return OrderSpecifier.NullHandling.Default;
      }
    }
  }

}
