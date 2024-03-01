package com.nary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
	@GeneratedValue
	@Id
private Integer userId;
private String userFirstName;
private String userLastName;
private Integer userAge;
private Long userPhoneNo;

}
