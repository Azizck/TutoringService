package ca.sheridancollege;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ca.sheridancollege.bean.Contact;
import ca.sheridancollege.bean.Payment;
import ca.sheridancollege.bean.Student;
import ca.sheridancollege.bean.User;
import ca.sheridancollege.bean.Appt;
import ca.sheridancollege.dao.DAO;

@Controller
public class HomeController {

	@Autowired
	private DAO dao;

	@GetMapping("/")
	public String main(HttpSession session) {
		try {
			session.setAttribute("appt", new Appt());
			session.setAttribute("min", dao.getTutorInfo().get(0));
			session.setAttribute("max", dao.getTutorInfo().get(1));
			String sub[] = { dao.getTutorInfo().get(2), dao.getTutorInfo().get(3), dao.getTutorInfo().get(4),
					dao.getTutorInfo().get(5), dao.getTutorInfo().get(6), dao.getTutorInfo().get(7) };
			ArrayList<String> subjects = new ArrayList<String>(Arrays.asList(sub));
			session.setAttribute("subjects", subjects);
			// System.out.println(session.getAttribute("subjects").toString());
			session.setAttribute("cost", dao.getTutorInfo().get(8));
			System.out.println(subjects.toString());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "redirect:/showTutor";
		}

		return "general.html";
	}

	@GetMapping("/modify")
	public String modify(HttpSession session) {
		return "modify.html";
	}

	// navigate to the page1
	@GetMapping("/page1")
	public String page1(HttpSession session, Model model) {
		// this should be from database
		return "page1.html";
	}

	// assign the requested String subject to the object app which is stored in a
	// session
	@GetMapping("/book")
	public String book(HttpSession session, @RequestParam String subject) {
		Appt appt = (Appt) session.getAttribute("appt");
		appt.setSubject(subject);
		session.setAttribute("appt", appt);
		return "redirect:/showBooking";
	}

	// assigns the requested params to the app object
	@GetMapping("/book2")
	public String bookTwo(HttpSession session, @RequestParam int duration, @RequestParam String notes,
			@RequestParam(required = false) String subject, @RequestParam double fee) {
		Appt appt = (Appt) session.getAttribute("appt");
		appt.setDuration(duration);
		appt.setNotes(notes);
		appt.setFee(fee);
		if (subject != null)
			appt.setSubject(subject);

		System.out.println("subject is" + ((Appt) session.getAttribute("appt")).getFee());
		session.setAttribute("appt", appt);
		return "summary.html";
	}

	@GetMapping("cancel")
	public String cancel(HttpSession session) {
		session.setAttribute("appt", null);
		return "redirect:/";
	}

	@GetMapping("/calendar")
	public String showCalendar(HttpSession session, Model model, @RequestParam(required = false) String date) {
		// get the current date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		// String date = "2019-06-12";
		// if string date is not provided assign the current date to it
		if (date == null) {
			date = dtf.format(now);
			System.out.print("date it is null but it has this value now : " + date);
		}

		// assign business hours to an arrayList
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(8);
		numbers.add(9);
		numbers.add(10);
		numbers.add(11);
		numbers.add(12);
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		numbers.add(4);
		numbers.add(5);
		numbers.add(6);
		numbers.add(7);

		// assign 8 days to an array
		ArrayList<String> days = new ArrayList<String>();
		days.add(dao.getDay(date));
		days.add(dao.getDay(increaseDate(date, 1)));
		days.add(dao.getDay(increaseDate(date, 2)));
		days.add(dao.getDay(increaseDate(date, 3)));
		days.add(dao.getDay(increaseDate(date, 4)));
		days.add(dao.getDay(increaseDate(date, 5)));
		days.add(dao.getDay(increaseDate(date, 6)));
		days.add(dao.getDay(increaseDate(date, 7)));
		// booked hours of specified date
		ArrayList<Integer[]> hours = new ArrayList<>();
		hours.add(dao.getHours(date));
		hours.add(dao.getHours(increaseDate(date, 1)));
		hours.add(dao.getHours(increaseDate(date, 2)));
		hours.add(dao.getHours(increaseDate(date, 3)));
		hours.add(dao.getHours(increaseDate(date, 4)));
		hours.add(dao.getHours(increaseDate(date, 5)));
		hours.add(dao.getHours(increaseDate(date, 6)));
		hours.add(dao.getHours(increaseDate(date, 7)));
		model.addAttribute("hours", hours);
		model.addAttribute("days", days);
		model.addAttribute("date", date);
		return "calendar.html";
	}

	// show booking
	@GetMapping("showBooking")
	public String refershCalendar(HttpSession session, Model model, @RequestParam(required = false) String date) {
		// get current date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		// String date = "2019-06-12";
		String day = dao.getDay(currentDate);
		if (date == null) {
			date = dtf.format(now);
			System.out.print("date it is null");
		}
		System.out.println("current date is " + dtf.format(now));
		model.addAttribute("day", dao.getDay(date));
		model.addAttribute("h", dao.getHours(date));
		model.addAttribute("date", date);
		return "calendar2.html";
	}

	// process booking
	@GetMapping("prBooking")
	public String prBooking(HttpSession session, Model model, @RequestParam String date, @RequestParam int time) {
		System.out.println(date);
		if (session.isNew())
			System.out.println("empty");
		String day = dao.getDay(date);
		Appt appt = (Appt) session.getAttribute("appt");
		appt.setDate(date);
		appt.setTime(time);
		session.setAttribute("appt", appt);
		model.addAttribute("day", day);

		model.addAttribute("h", dao.getHours(date));
		return "page2.html";
	}

	@GetMapping("submitAppt")
	public String submitAppt(HttpSession session) {
		System.out.println("Appt is about to be submitted");
		Appt appt = (Appt) session.getAttribute("appt");
		System.out.println("befoer submitting" + appt.toString());
		session.setAttribute("appt", appt);
		dao.addAppt(appt);
		// session should be deactivated here
		int studentId = (int) ((User) session.getAttribute("student")).getUSER_ID();
		System.out.println("got student id ");
		dao.getApptId(appt);
		System.out.println("got appt id ");
		dao.linkStudentAppt(studentId, dao.getApptId(appt));
		// session.setAttribute("appt", null);
		return "completed.html";
	}

	@GetMapping("showDashboard")
	public String showDashboard(HttpSession session, Model model) {
		// session.setAttribute("appt", null);
		model.addAttribute("appts", dao.getAppt());
		for (Appt appt : dao.getAppt()) {
			System.out.println("appt is " + appt);
		}

		return "dashboard.html";
	}

	@GetMapping("studentDashboard")
	public String studentDashboard(HttpSession session, Model model) {
		User s = null;
		try {
			s = (User) session.getAttribute("student");
			// to throw an exception if student does not exist in session
			s.getUSER_ID();
			model.addAttribute("appts", dao.getStudentAppts((int) s.getUSER_ID()));
		} catch (Exception ex) {
			System.out.println("error in student dashboard proabaly no student in session " + ex.getMessage());
		}
		return "dashboard.html";
	}

	@GetMapping("deleteAppt")
	public String appt(HttpSession session, Model model) {
		return "showDashboard";

	}

	@GetMapping("/dellink/{id}")
	public String delStudent(Model model, @PathVariable int id) {
		System.out.println("the id is " + id);
		// uncomment the below line and the chosen appt where be deleted
		dao.deleteAppt(id);
		return "redirect:/showDashboard";

	}

	@GetMapping("/showPayment")
	public String showPayment(HttpSession session, Authentication authentication, HttpServletRequest request,
			Model model) {
		String returnVal = "payment.html";
		String name = authentication.getName();
		session.setAttribute("student", dao.findUserAccount(name));
		System.out.println("name is " + name);
		boolean hasPayment = dao.hasPayment((int) dao.findUserAccount(name).getUSER_ID());
		if (hasPayment) {
			returnVal = "confirmPayment.html";
		}
		System.out.println("has payment " + hasPayment);
		model.addAttribute("payment", new Payment());
		System.out.print("added");
		return returnVal;
	}

	// add student
	@PostMapping("/addPayment")
	public String addPayment(HttpSession session, Model model, @ModelAttribute Payment payment) {
		dao.addPayment(payment);
		try {
			if (session.isNew()) {
				System.out.println("session is new ");
			}
			User user = (User) session.getAttribute("student");
			dao.linkPayment((int) user.getUSER_ID(), dao.getPaymentId(payment));
			System.out.println(user);
		} catch (Exception ex) {
			System.out.println("Student does not exist. Payment can't be linked: " + ex.getMessage());
		}
		return "confirmPayment.html";

	}

	@GetMapping("/register")
	public String goreg() {
		return "register.html";
	}

	@GetMapping("/sec")
	public String mymain() {
		return "index.html";
	}

	@PostMapping("/register")
	public String doreg(HttpSession session, @RequestParam String username, @RequestParam String password,
			@RequestParam String role) {
		dao.addUser(username, BCcryptPasswordEncoder(password));
		System.out.println("user name is :" + username);
		System.out.println("role is :" + role);
		System.out.println("the id is " + dao.findUserAccount(username).getUSER_ID());
		long userId = dao.findUserAccount(username).getUSER_ID();
		if (role.equalsIgnoreCase("admin"))
			dao.addRole(userId, 1);
		if (role.equalsIgnoreCase("student"))
			dao.addRole(userId, 2);

		try {
			if (((Appt) session.getAttribute("appt")).getSubject() != null && role.equals("USER")) {
				return "redirect:/showPayment";
			}
		} catch (Exception ex) {
			System.out.println("no appt in session user taken to main page" + ex.getMessage());
		}

		return "/general.html";
	}

	public static String increaseDate(String date, long days) {
		LocalDate inDate = LocalDate.parse(date);
		LocalDate newDate = inDate.plusDays(days);
		String d = newDate.toString();
		return d;
	}

	public static String BCcryptPasswordEncoder(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}

	@GetMapping("login")
	public String login(HttpSession session, Model model) {
		return "login.html";
	}

	@GetMapping("access-denied")
	public String denied() {
		return "error/access-denied.html";
	}

	@GetMapping("addTutor")
	public String showTutorInfor(HttpSession session, @RequestParam String min, @RequestParam String max,
			@RequestParam String subject1, @RequestParam String subject2, @RequestParam String subject3,
			@RequestParam String subject4, @RequestParam String subject5, @RequestParam String subject6,
			@RequestParam float pricePerHour) {
		String[] s = { subject1, subject2, subject3, subject4, subject5, subject6 };
		dao.addTutorInfo(min, max, s, pricePerHour);
		return "redirect:/";
	}

	@GetMapping("showTutor")
	public String showTutor(HttpSession session, Authentication auth, HttpServletRequest request, Model model) {
		session.setAttribute("name", auth.getName());
		return "tutor.html";
	}

}