/**
 * 
 */
package ca.sheridancollege;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author azizo
 *
 */
public class SeucityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	public SeucityWebApplicationInitializer() {
		super(SecuirtyConfig.class);
		// TODO Auto-generated constructor stub
	}

}
