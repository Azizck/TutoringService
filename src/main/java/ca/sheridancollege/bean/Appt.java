package ca.sheridancollege.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appt {
	private int appt_id;
	private String subject;
	private String date;
	private int time;
	private int duration;
	private String notes;
	private double fee;
	public Appt(String subject, String date, int time, int duration, String notes, double fee) {
		
		this.subject = subject;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.notes = notes;
		this.fee = fee;
	}

}
