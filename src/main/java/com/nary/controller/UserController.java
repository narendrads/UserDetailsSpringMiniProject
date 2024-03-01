package com.nary.controller;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.nary.entity.UserEntity;
import com.nary.service.UserService;
@RestController
public class UserController {
	
	@Autowired
	private UserService service;
	@PostMapping("/save")
public ResponseEntity<String> saveUser(@RequestBody UserEntity entity){
	String msg="user saved successfuly";
	service.saveUser(entity);
	return new ResponseEntity<String>(msg,HttpStatus.OK);
}
	@GetMapping("/get/{id}")
public ResponseEntity<Optional<UserEntity>> getById(@PathVariable Integer id){
	
	Optional<UserEntity> userById = service.getUserById(id);
	return new ResponseEntity<Optional<UserEntity>>(userById,HttpStatus.OK);
}
	@GetMapping("/getall")
	public ResponseEntity<List<UserEntity>> filte( @RequestParam(required = false) Integer id,@RequestParam(required = false) Integer age){
		List<UserEntity> filterbyidandage = service.filterbyidandage(id,age);
		return new ResponseEntity<List<UserEntity>>(filterbyidandage,HttpStatus.OK);
	}
	@GetMapping("get")
	public ResponseEntity<List<UserEntity>> getAll() {
		List<UserEntity> allUsers = service.getAllUsers();
		return new ResponseEntity<List<UserEntity>>(allUsers,HttpStatus.OK);
	}

	@GetMapping("/users")
	public List<UserEntity> getUsersByFilter(@ModelAttribute UserEntity filter) {
		
	    // Check if at least one field in the filter object is not null
	    if (filter != null && hasNonNullField(filter)) {
	        return service.getUsersByFilter(filter);
	    } else {
	        // If no filter parameters are provided, return all users
	        return service.getAllUsers();
	    }
	}
	public boolean notnull(UserEntity filter) {
		if(filter.getUserAge()==null||filter.getUserFirstName()==null)
			return false;
		else
		return true;
		
	}
	// Method to check if any field in the filter object is not null
	private boolean hasNonNullField(UserEntity filter) {
	    Field[] fields = filter.getClass().getDeclaredFields();
	    for (Field field : fields) {
	        field.setAccessible(true);
	        try {
	            Object value = field.get(filter);
	            if (value != null) {
	                return true;
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
	 @GetMapping("/sorted")
	    public ResponseEntity<List<UserEntity>> getAllEntitiesSortedByColumn(
	            @RequestParam(required = true) String columnName,
	            @RequestParam(defaultValue = "asc") String sortOrder) {
	        
	        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	        Sort sort = Sort.by(direction, columnName);
	        
	        List<UserEntity> entities = service.getAllEntitiesSorted(sort);
	        return new ResponseEntity<>(entities, HttpStatus.OK);
	    }
	 
	 @GetMapping("/pagenation")
		public ResponseEntity<  List<UserEntity>> getUserInPage(@RequestParam(defaultValue = "0")Integer page,@RequestParam(defaultValue = "5") Integer size){
			Page<UserEntity> userInPage = service.getUserInPage(page, size);
			
       List<UserEntity> content = userInPage.getContent();
	       
	    System.out.println(content);
			for(UserEntity s:userInPage) {
				System.out.println(s  );
			}
		        
			return new ResponseEntity<  List<UserEntity>> (content,HttpStatus.OK);

}
	 @PutMapping("/update/{id}")
	 public ResponseEntity<UserEntity> update(@PathVariable("id") Integer id, @RequestBody UserEntity updatedUser){
		 UserEntity entity=service.updateUser(id, updatedUser);
		 if(entity!=null)
		return ResponseEntity.ok(entity);
		 else {
			 return ResponseEntity.notFound().build();
		 }
		 
	 }
	 @DeleteMapping("/delete/{id}")
	 public ResponseEntity<String> delete(@PathVariable("id") Integer id){
		 service.deletebyid(id);
		return  ResponseEntity.ok("dalete success");
		 
	 }
	 }
