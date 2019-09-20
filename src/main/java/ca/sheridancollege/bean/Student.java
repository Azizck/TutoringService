package ca.sheridancollege.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	private int d;
	private String name;
	private String email;
	private String password;
	public Student(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}	
}


