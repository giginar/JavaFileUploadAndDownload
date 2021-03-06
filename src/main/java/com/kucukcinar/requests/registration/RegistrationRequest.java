package com.kucukcinar.requests.registration;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * The type Registration request.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String password;
	@NotBlank
	private String email;

}
