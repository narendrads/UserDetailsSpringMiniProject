package com.nary.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.nary.Repo.UserRepo;
import com.nary.entity.UserEntity;
@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	public void saveUser(UserEntity entity) {
		userRepo.save(entity);
	}
	public  Optional<UserEntity> getUserById(Integer id) {
		 Optional<UserEntity> findById = userRepo.findById(id);
		 return findById;
	}
	public List<UserEntity> filterbyidandage(Integer id,Integer age) {
		List<UserEntity> findByuserIdOruserAge = userRepo.findByUserIdOrUserAge(id, age);
		return findByuserIdOruserAge;
	}
	
	public List<UserEntity> getUsersByFilter(UserEntity filter) {
		Example<UserEntity> e=Example.of(filter);
        return userRepo.findAll(e);
    }
	public List<UserEntity> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll() ;
	}
	
	public List<UserEntity> getAllEntitiesSorted(Sort sort) {
        return userRepo.findAll(sort);
    }
	public Page<UserEntity> getUserInPage(Integer page,Integer size){
		return userRepo.findAll(PageRequest.of(page, size));
	
	}
	
	public UserEntity updateUser(Integer id,UserEntity entity) {
		return userRepo.findById(id).map(user->
		
				{
					user.setUserFirstName(entity.getUserFirstName());
					user.setUserLastName(entity.getUserLastName());
					user.setUserAge(entity.getUserAge());
					user.setUserPhoneNo(entity.getUserPhoneNo());
					return userRepo.save(user);
				})
				.orElse(null);
		
	}
	public void deletebyid(Integer id) {
		userRepo.deleteById(id);
	}
}
