package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.JWTToken;

import jakarta.transaction.Transactional;
@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer>{
	  Optional<JWTToken> findByToken(String token);
	@Query("SELECT t FROM JWTToken t WHERE t.user.userId = :userId")
JWTToken findByUserId(@Param("userId") int userId);

//@Query("DELETE FROM JWTToken where t.user.userId = :userId")
//void deleteByUserId(@Param("userId")int useerId);



@Modifying
@Transactional
@Query("DELETE FROM JWTToken t WHERE t.user.userId = :userId")
void deleteByUserId(@Param("userId") int userId);
}
