package ca.sheridancollege.bean;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
private long USER_ID;
private  String USER_NAME;
private String ENCRYPTED_PASSWORD;

}