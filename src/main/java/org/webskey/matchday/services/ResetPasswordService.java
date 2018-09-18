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
import org.springframework.stereotype.Service;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.ResetPasswordEntity;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ResetPasswordService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private ResetPasswordDao resetPasswordDao;

	public void resetPassword(UsersDto usersDto, String url) throws ArithmeticException {
		Optional<UsersEntity> user = usersDao.findByEmail(usersDto.getEmail());		
		if(!user.isPresent())
			throw new ArithmeticException();

		createToken(user.get(), url);
	}

	public void createToken(UsersEntity user, String url) {
		UUID uuid = UUID.randomUUID();

		ResetPasswordEntity rpe = new ResetPasswordEntity();
		rpe.setUsername(user.getUsername());
		rpe.setToken(uuid.toString());
		rpe.setDate(new Date());

		resetPasswordDao.save(rpe);
		
		constructAndSendEmail(user, url, uuid.toString());
	}
	
	public void constructAndSendEmail(UsersEntity user, String url, String token) {
		String urlToReset = url + "/" + user.getUsername() + "/" + token;
		System.out.println(urlToReset);
	}

	public boolean checkLink(String username, String token) {
		Optional<ResetPasswordEntity> rpeO = resetPasswordDao.findById(username);
		if(!rpeO.isPresent()) return false;
		
		ResetPasswordEntity rpe = rpeO.get();
		if(!rpe.getToken().equals(token)) return false;
		
		long hoursSinceTokenSent = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - rpe.getDate().getTime());
		if(hoursSinceTokenSent > 24) return false;
		
		return true;
	}
}
