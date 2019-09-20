package ca.sheridancollege;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyEncodingTester {

	

	public static String BCcryptPasswordEncoder(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	public static void main(String  args){
		String password = "123";
		String encryptedPassword = BCcryptPasswordEncoder(password);
		System.out.println(encryptedPassword );
		encryptedPassword =  BCcryptPasswordEncoder(password);

	}
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) {
		
		
		
	}
}
