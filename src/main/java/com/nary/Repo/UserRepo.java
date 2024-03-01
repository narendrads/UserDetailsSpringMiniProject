package com.nary.Repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nary.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer>{
	List<UserEntity> findByUserIdOrUserAge(Integer userId,  Integer userAge);
	
	    List<UserEntity> findAll(Sort sort);
}
