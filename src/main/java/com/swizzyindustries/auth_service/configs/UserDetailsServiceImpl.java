/**
 * 
 */
package com.swizzyindustries.auth_service.configs;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swizzyindustries.auth_service.models.CodeLordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private CodeLordRepository codelordRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String codeLordEmail) throws UsernameNotFoundException {
		return codelordRepo.findByEmail(codeLordEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Lord" + codeLordEmail + "not found!"));
	}

}
