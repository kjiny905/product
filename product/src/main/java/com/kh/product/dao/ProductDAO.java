package com.kh.product.dao;

import java.util.Optional;

public interface ProductDAO {
  // 등록
  Long save(Product product);
  // 조회
  Optional<Product> findById(Long productId);
  // 수정
  int update(Long productId, Product product);
  // 삭제
  int delete(Long productId);
  // 전체삭제
  int deleteAll();
  // 목록
  //List<Product> findAll();
}
