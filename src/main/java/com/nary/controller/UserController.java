package com.nary.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	 
	 @GetMapping("/pagedAndSorted")
	 public ResponseEntity<List<UserEntity>> getUsersPagedAndSorted(
	         @RequestParam(defaultValue = "0") Integer page,
	         @RequestParam(defaultValue = "5") Integer size,
	         @RequestParam(required = true) String columnName,
	         @RequestParam(defaultValue = "asc") String sortOrder) {
	     
	     Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	     Sort sort = Sort.by(direction, columnName);
	     
	     Page<UserEntity> userPage = service.getUserInPageAndSorted(page, size, sort);
	     List<UserEntity> content = userPage.getContent();
	     return new ResponseEntity<>(content, HttpStatus.OK);
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
	

	    @GetMapping("/pagedAndSorted1")
	    public byte[] getPagedAndSortedUsersPdf(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "userId") String columnName,
	            @RequestParam(defaultValue = "asc") String sortOrder) {

	        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), columnName));
	        Page<UserEntity> usersPage = service.getUserInPageAndSorted(pageRequest);

	        return generatePdf(usersPage);
	    }

	    private byte[] generatePdf(Page<UserEntity> usersPage) {
	        try (PDDocument document = new PDDocument()) {
	            PDPage page = new PDPage();
	            document.addPage(page);

	            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	                contentStream.beginText();
	                contentStream.newLineAtOffset(100, 700);

	                // Write headers
	                contentStream.showText("User ID");
	                contentStream.newLineAtOffset(100, 0);
	                contentStream.showText("First Name");
	                contentStream.newLineAtOffset(100, 0);
	                contentStream.showText("Last Name");
	                contentStream.newLineAtOffset(100, 0);
	                contentStream.showText("Age");
	                contentStream.newLineAtOffset(100, 0);
	                contentStream.showText("Phone Number");
	                contentStream.newLineAtOffset(-400, -20);

	                // Write data
	                List<UserEntity> users = usersPage.getContent();
	                for (UserEntity user : users) {
	                    contentStream.showText(user.getUserId().toString());
	                    contentStream.newLineAtOffset(100, 0);
	                    contentStream.showText(user.getUserFirstName());
	                    contentStream.newLineAtOffset(100, 0);
	                    contentStream.showText(user.getUserLastName());
	                    contentStream.newLineAtOffset(100, 0);
	                    contentStream.showText(String.valueOf(user.getUserAge()));
	                    contentStream.newLineAtOffset(100, 0);
	                    contentStream.showText(user.getUserPhoneNo().toString());
	                    contentStream.newLineAtOffset(-400, -20);
	                }
	                contentStream.endText();
	            }

	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            document.save(baos);
	            return baos.toByteArray();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	 }
