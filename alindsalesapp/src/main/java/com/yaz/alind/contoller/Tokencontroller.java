package com.yaz.alind.contoller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;




import com.yaz.alind.model.Employee;
import com.yaz.security.Iconstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Tokencontroller {

	@RequestMapping(value="/token", method = RequestMethod.POST)
	public ResponseEntity<String> getToken(@RequestBody Employee login) throws ServletException {

		String jwttoken = "";

		if(login.getUserName().isEmpty() || login.getPasswordHash().isEmpty())
			return new ResponseEntity<String>("Username or password cannot be empty.", HttpStatus.BAD_REQUEST);

		String name = login.getUserName(), 
				password = login.getPasswordHash();

		if(!(name.equalsIgnoreCase("Test") && password.equalsIgnoreCase("1234")))
			return new ResponseEntity<String>("Invalid credentials. Please check the username and password.", HttpStatus.UNAUTHORIZED);
		else {
			// Creating JWT using the user credentials.
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("usr", login.getUserName());
			claims.put("sub", "Authentication token");
			claims.put("iss", Iconstants.ISSUER);
			claims.put("rol", "Administrator, Developer");
			claims.put("iat", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			//			UUID uuid = UUID.randomUUID();
			//			String randomUUIDString = uuid.toString();
			//			System.out.println("UUID: "+randomUUIDString);

			jwttoken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, Iconstants.SECRET_KEY).compact();
			System.out.println("Returning the following token to the user= "+ jwttoken);
		}

		return new ResponseEntity<String>(jwttoken, HttpStatus.OK);
	}
}