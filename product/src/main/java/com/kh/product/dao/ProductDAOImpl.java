package com.kh.product.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {

  private final NamedParameterJdbcTemplate template;

  /**등록**/
  @Override
  public Long save(Product product) {
    StringBuffer sb = new StringBuffer();
    sb.append("insert into product(pid,pname,quantity,price) ");
    sb.append("values(product_pid_seq.nextval, :pname, :quantity, :price)");

    SqlParameterSource param = new BeanPropertySqlParameterSource(product);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sb.toString(),param,keyHolder,new String[]{"pid"});

    long pid = keyHolder.getKey().longValue();

    return pid;
  }


  /**조회**/
  @Override
  public Optional<Product> findById(Long pid) {
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append(" from product ");
    sb.append(" where pid = :id ");

    try {
      Map<String, Long> param = Map.of("id", pid);
      Product product = template.queryForObject(sb.toString(), param, productRowMapper());
      return Optional.of(product);
    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없을때
      return Optional.empty();
    }
  }

  /**수정**/
  @Override
  public int update(Long pid, Product product) {
    StringBuffer sb = new StringBuffer();
    sb.append("update product ");
    sb.append(" set pname = :pname, ");
    sb.append(" quantity = :quantity, ");
    sb.append(" price = :price ");
    sb.append(" where pid = :id ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("pname",product.getPname())
        .addValue("quantity",product.getQuantity())
        .addValue("price",product.getPrice())
        .addValue("id",pid);

    return template.update(sb.toString(),param);
  }

  /**삭제**/
  @Override
  public int delete(Long pid) {
    String sql = "delete from product where pid = :id ";
    return template.update(sql,Map.of("id",pid));
  }

  /**목록**/
  @Override
  public List<Product> findAll() {
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");

    List<Product> list = template.query(
        sb.toString(),
        BeanPropertyRowMapper.newInstance(Product.class)
    );

    return list;
  }

  class RowMapperImpl implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
      Product product = new Product();
      product.setPid(rs.getLong("pid"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      product.setPrice(rs.getLong("price"));
      return product;
    }
  }

  //조회에서 productRowMapper의 함수
  private RowMapper<Product> productRowMapper() {
    return (rs, rowNum) -> {
      Product product = new Product();
      product.setPid(rs.getLong("pid"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      product.setPrice(rs.getLong("price"));
      return product;
    };
  }
}
