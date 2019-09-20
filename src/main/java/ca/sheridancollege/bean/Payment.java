package ca.sheridancollege.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	private int card_id;
	private String type;
	private String cardNumber;
	private String expiryDate;

	public Payment(String type, String cardNumber, String expiryDate) {
		this.type = type;
		this.cardNumber = cardNumber;
		this.expiryDate = expiryDate;
	}
}
