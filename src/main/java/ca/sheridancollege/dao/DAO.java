package ca.sheridancollege.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.bean.User;
import ca.sheridancollege.bean.Appt;
import ca.sheridancollege.bean.Payment;
import ca.sheridancollege.bean.Student;

@Repository
public class DAO {

	@Autowired
	private JdbcTemplate jdbc;

	public void addRole(long userId, long roleId) {
		try {
			String q = "insert into user_role (USER_ID, ROLE_ID)" + "values (?, ?);";
			jdbc.update(q, new Object[] { userId, roleId });
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

	public void addUser(String userName, String encryptedPassword) {
		try {

			String q = "insert into SEC_User " + "(USER_NAME, ENCRYPTED_PASSWORD, ENABLED)" + " values (?,?,1)";
			jdbc.update(q, new Object[] { userName, encryptedPassword });
		} catch (Exception e) {

			System.out.println(e);

		}

	}

	public User findUserAccount(String userName) {

		User user = null;
		try {
			String q = "SELECT * FROM sec_user WHERE user_name=?";

			user = jdbc.queryForObject(q, new Object[] { userName }, new BeanPropertyRowMapper(User.class));
			System.out.println("this user has been passed" + userName);
			System.out.println(q);
			System.out.println(user);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println(user);
		return user;
	}

	public static ArrayList<String> getUserRoles(long id) {
		ArrayList<String> roles = new ArrayList<String>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:mysql://localhost/pro", "root", "root");
			String command = " SELECT * FROM" + " user_role, sec_role WHERE user_role.role_id=sec_role.role_id "
					+ "AND user_role.user_id=" + id;

			Statement str = conn.createStatement();
			ResultSet rs = str.executeQuery(command);

			while (rs.next()) {
				roles.add(rs.getString(5));
			}
			conn.close();
		} catch (Exception e) {

		}

		return roles;
	}

	// project methods
	public void addAppt(Appt obj) {
		String command = "INSERT INTO Appt values (default,?,?,?,?,?,?) ";
		Object[] params = new Object[] { obj.getSubject(), obj.getDate(), obj.getTime(), obj.getDuration(),
				obj.getNotes(), obj.getFee() };
		jdbc.update(command, params);
	}

	public ArrayList<Appt> getAppt() {
		String q = "Select * from Appt";
		ArrayList<Appt> appointments = (ArrayList<Appt>) jdbc.query(q, new BeanPropertyRowMapper(Appt.class));
		return appointments;
	}

	/**
	 * public ArrayList<Appt> getStudentAppt() { String q = "Select * from Appt ";
	 * ArrayList<Appt> appointments = (ArrayList<Appt>) jdbc.query(q, new
	 * BeanPropertyRowMapper(Appt.class)); return appointments; }
	 */

	// insert register page is good for this
	public void addStudent(Student obj) {
		String command = "INSERT INTO student values (default,?,?,?) ";
		Object[] params = new Object[] { obj.getName(), obj.getEmail(), obj.getPassword() };
		jdbc.update(command, params);
	}

	// get student not needed for sec
	public Student getStudent(String email, String password) {
		String q = "Select * from student WHERE email=? AND password=?";
		Student student = (Student) jdbc.queryForObject(q, new Object[] { email, password },
				new BeanPropertyRowMapper<Student>(Student.class));
		return student;
	}

	// useless method
	public Student checkEmail(String email) {
		String q = "Select (email) from student WHERE email=?";
		System.out.println("***" + email);
		try {
			Student student = (Student) jdbc.queryForObject(q, new Object[] { email },
					new BeanPropertyRowMapper<Student>(Student.class));

			return student;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public void addPayment(Payment obj) {
		String q = "INSERT INTO payment values (default,?,?,?) ";
		Object[] params = new Object[] { obj.getType(), obj.getCardNumber(), obj.getExpiryDate() };
		jdbc.update(q, params);
	}

	public int getStudentId(Student obj) {
		String q = "SELECT (USER_ID) FROM student WHERE USER_NAME=?";
		int student_id = jdbc.queryForObject(q, new Object[] { obj.getEmail() }, Integer.class);
		return student_id;
	}

	public void deleteAppt(int id) {
		String q = "DELETE from stud_appt WHERE appt_id=?";
		String qq = "DELETE FROM appt WHERE appt_id=?";
		jdbc.update(q, new Object[] { id });
		jdbc.update(qq, new Object[] { id });
	}

	public int getPaymentId(Payment obj) {
		String q = "SELECT (card_id) FROM payment WHERE cardNumber=?";
		int card_id = jdbc.queryForObject(q, new Object[] { obj.getCardNumber() }, Integer.class);
		return card_id;
	}

	public int getApptId(Appt appt) {
		String q = " select  appt_id from appt where date=? and time=?";
		System.out.println(appt.getDate() + " time: " + appt.getTime());
		int appt_id = jdbc.queryForObject(q, new Object[] { appt.getDate(), appt.getTime() }, Integer.class);
		return appt_id;
	}

	public void linkPayment(int student_id, int card_id) {
		String q = "INSERT INTO student_payment VALUES (default, ?, ?)";
		Object params[] = new Object[] { student_id, card_id };
		jdbc.update(q, params);
	}

	public ArrayList<Appt> getStudentAppts(int id) {
		String q = "select a.appt_id,subject,date,time,duration,notes,fee from appt a inner JOIN stud_appt s ON a.appt_id=s.appt_id where s.student_id=?";
		Object params[] = new Object[] { id };
		ArrayList<Appt> appointments = (ArrayList<Appt>) jdbc.query(q, params, new BeanPropertyRowMapper(Appt.class));
		return appointments;

	}

	public void linkStudentAppt(int studentId, int apptId) {
		String command = "INSERT INTO  stud_appt VALUES (default,?,?)";
		Object[] params = new Object[] { studentId, apptId };

		jdbc.update(command, params);
	}

	public boolean hasPayment(int id) {
		boolean b = false;
		String q = "SELECT * FROM student_payment WHERE student_id=?";
		try {
			Student student = (Student) jdbc.queryForObject(q, new Object[] { id },
					new BeanPropertyRowMapper<Student>(Student.class));
			b = true;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			b = false;
		}
		return b;
	}

	public Integer[] getHours(String date) {
		String qq = " select time from appt where date = ?";
		ArrayList<Appt> appts = (ArrayList<Appt>) jdbc.query(qq, new Object[] { date },
				new BeanPropertyRowMapper(Appt.class));
		int x[] = new int[12];
		int i = 0;
		// get the times from appts and add them to array
		for (Appt appt : appts) {
			x[i] = appt.getTime();
			i++;
			System.out.println("hours are " + appt.getTime());
		}

		int[] y = new int[12];

		// get the value from the array of hours and assign it to the proper index
		for (int ii = 0; ii < x.length; ii++) {
			switch (x[ii]) {
			case 8:
				y[0] = 1;
				break;
			case 9:
				y[1] = 1;
				break;
			case 10:
				y[2] = 1;
				break;
			case 11:
				y[3] = 1;
				break;
			case 12:
				y[4] = 1;
				break;
			case 13:
				y[5] = 1;
				break;
			case 14:
				y[6] = 1;
				break;
			case 15:
				y[7] = 1;
				break;
			case 16:
				y[8] = 1;
				break;
			case 17:
				y[9] = 1;
				break;
			case 18:
				y[10] = 1;
				break;
			case 19:
				y[11] = 1;
				break;
			default:

			}
			System.out.println("run");
		}

		Integer z[] = new Integer[12];
		for (Integer n : y) {
			System.out.println(n);
		}
		for (int m = 0; m < 12; m++) {
			z[m] = y[m];
		}

		return z;
	}

	public String getDay(String date) {
		String q = "SELECT DAYNAME(?)";
		String day = jdbc.queryForObject(q, new Object[] { date }, String.class);
		// System.out.println(day);
		return day;
	}

	public void addTutorInfo(String min, String max, String subjects[], float pricePerHour) {
		try {
			String d = "DELETE FROM TUTOR";
			jdbc.update(d);

			String q = "insert into tutor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbc.update(q, new Object[] { min, max, subjects[0], subjects[1], subjects[2], subjects[3], subjects[4],
					subjects[5], pricePerHour });
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<String> getTutorInfo() {
		ArrayList<String> info = new ArrayList<String>();
		String a = "SELECT (min) FROM tutor";
		String min = jdbc.queryForObject(a, String.class);
		String b = "SELECT (max) FROM tutor";
		String max = jdbc.queryForObject(b, String.class);
		String c = "SELECT (subject1) FROM tutor";
		String sub1 = jdbc.queryForObject(c, String.class);
		String d = "SELECT (subject2) FROM tutor";
		String sub2 = jdbc.queryForObject(d, String.class);
		String e = "SELECT (subject3) FROM tutor";
		String sub3 = jdbc.queryForObject(e, String.class);
		String f = "SELECT (subject4) FROM tutor";
		String sub4 = jdbc.queryForObject(f, String.class);
		String g = "SELECT (subject5) FROM tutor";
		String sub5 = jdbc.queryForObject(g, String.class);
		String h = "SELECT (subject6) FROM tutor";
		String sub6 = jdbc.queryForObject(h, String.class);
		String i = "SELECT (pricePerHour) FROM tutor";
		float pricePerHour = jdbc.queryForObject(i, float.class);



		info.add(min);
		info.add(max);
		info.add(sub1);
		info.add(sub2);
		info.add(sub3);
		info.add(sub4);
		info.add(sub5);
		info.add(sub6);
		info.add(String.valueOf(pricePerHour));

		return info;
	}
}
