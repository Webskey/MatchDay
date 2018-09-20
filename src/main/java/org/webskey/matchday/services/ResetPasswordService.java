package org.webskey.matchday.services;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.ResetPasswordEntity;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.mailmessages.ResetPasswordMessage;

@Service
public class ResetPasswordService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private ResetPasswordDao resetPasswordDao;
	
	private UsersEntity user;
	
	public UsersEntity getUser() {
		return this.user;
	}

	public void resetPassword(UsersDto usersDto, String url) throws ArithmeticException {
		Optional<UsersEntity> user = usersDao.findByEmail(usersDto.getEmail());		
		if(!user.isPresent())
			throw new ArithmeticException();
		
		createToken(user.get(), url);
	}

	private void createToken(UsersEntity user, String url) {
		String token = UUID.randomUUID().toString();
		String link = url + "/" + user.getUsername() + "/" + token;
		System.out.println(link);

		ResetPasswordEntity rpe = new ResetPasswordEntity();
		rpe.setUsername(user.getUsername());
		rpe.setToken(token);
		rpe.setDate(new Date());

		resetPasswordDao.save(rpe);
		
		constructAndSendEmail(user, link);
	}
	
	private void constructAndSendEmail(UsersEntity user, String link) {		
		emailService.sendHtmlEmail(new ResetPasswordMessage(user, link));
	}

	public String checkLink(String username, String token) {
		Optional<ResetPasswordEntity> rpeO = resetPasswordDao.findById(username);
		if(!rpeO.isPresent()) return "Given username in link doesnt exists";
			
		ResetPasswordEntity rpe = rpeO.get();
		if(!rpe.getToken().equals(token)) return "Incorrect token";
		
		long hoursSinceTokenSent = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - rpe.getDate().getTime());
		if(hoursSinceTokenSent > 24) return "Token expired";		
		
		Optional<UsersEntity> user = usersDao.findByUsername(username);
		this.user = user.get();
		
		return "ok";
	}
	
	public void changePassword(UsersDto usersDto) {
		user.setPassword(new BCryptPasswordEncoder().encode(usersDto.getPassword()));
		usersDao.save(user);
	}
}
