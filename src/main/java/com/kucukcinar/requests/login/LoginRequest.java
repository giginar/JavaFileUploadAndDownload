package com.kucukcinar.requests.login;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * The type Login request.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;


}
