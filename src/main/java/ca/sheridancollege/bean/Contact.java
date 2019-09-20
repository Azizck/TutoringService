package ca.sheridancollege.bean;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter

public class Contact {
	private String name;
	private String address;
	private long phoneNumber;
	private String email;
	private int role;

	public Contact(String name, String address, long phoneNumber, String email) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;

	}

}