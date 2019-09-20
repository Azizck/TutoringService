package ca.sheridancollege;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.dao.DAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private DAO DAO;
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ca.sheridancollege.bean.User user = DAO.findUserAccount(username);
		if (user == null) {
			System.out.println("user Not found " + username);
			throw new UsernameNotFoundException("USER " + username + "was not found in the database");

		}

		ArrayList<String> roleNames = DAO.getUserRoles(user.getUSER_ID());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(user.getUSER_NAME(), user.getENCRYPTED_PASSWORD(), grantList);

		return userDetails;
	}

}
