package com.pase.splitzer;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pase.splitzer.auth.model.AppRole;
import com.pase.splitzer.auth.model.AccountStatus;
import com.pase.splitzer.auth.model.AppUser;
import com.pase.splitzer.auth.repository.AppUserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"app.jwt.secret=dGVzdC1qd3Qtc2VjcmV0LWtleS1tdXN0LWJlLWF0LWxlYXN0LTMyLWJ5dGVz",
		"spring.datasource.url=jdbc:h2:mem:splitzer-test",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
class SplitzerApplicationTests {

	private static final Pattern ACCESS_TOKEN_PATTERN = Pattern.compile("\"accessToken\":\"([^\"]+)\"");

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private AppUserRepository users;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Autowired
	private JwtAuthenticationConverter jwtAuthenticationConverter;

	@Test
	void registeredUserCanGetTokenAndCallProtectedApi() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
		String username = "user-" + UUID.randomUUID();
		String password = "correct-horse";
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isUnauthorized());

		String adminUsername = "approver-" + UUID.randomUUID();
		users.save(new AppUser(adminUsername, passwordEncoder.encode(password), AppRole.ADMIN, AccountStatus.APPROVED));
		String adminToken = requestToken(mockMvc, adminUsername, password);
		Long userId = users.findByUsername(username).orElseThrow().getId();
		mockMvc.perform(put("/api/users/" + userId + "/approve")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
				.andExpect(status().isOk());

		MvcResult tokenResult = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andReturn();
		Matcher tokenMatcher = ACCESS_TOKEN_PATTERN.matcher(tokenResult.getResponse().getContentAsString());
		assertThat(tokenMatcher.find()).isTrue();
		String token = tokenMatcher.group(1);
		assertThat(token).isNotBlank();
		assertThat(jwtDecoder.decode(token).getClaimAsStringList("roles")).contains("ROLE_USER");

		mockMvc.perform(get("/api/secure/ping"))
				.andExpect(status().isUnauthorized());

		MvcResult protectedResult = mockMvc.perform(get("/api/secure/ping")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(protectedResult.getResponse().getContentAsString()).contains("\"username\":\"" + username + "\"");
	}

	@Test
	void tokenUsesRoleStoredForUser() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
		String username = "admin-" + UUID.randomUUID();
		String password = "correct-horse";
		users.save(new AppUser(username, passwordEncoder.encode(password), AppRole.ADMIN, AccountStatus.APPROVED));

		MvcResult tokenResult = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andReturn();
		Matcher tokenMatcher = ACCESS_TOKEN_PATTERN.matcher(tokenResult.getResponse().getContentAsString());
		assertThat(tokenMatcher.find()).isTrue();
		var jwt = jwtDecoder.decode(tokenMatcher.group(1));

		assertThat(jwt.getClaimAsStringList("roles")).containsExactly("ROLE_ADMIN");
		assertThat(jwtAuthenticationConverter.convert(jwt).getAuthorities())
				.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Test
	void onlyAdminsCanListUsersAndPasswordsAreNotReturned() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
		String regularUsername = "user-" + UUID.randomUUID();
		String password = "correct-horse";
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + regularUsername + "\",\"password\":\"" + password
								+ "\",\"name\":\"Test User\",\"email\":\"" + regularUsername
								+ "@example.com\",\"pictureUrl\":\"https://example.com/avatar.png\"}"))
				.andExpect(status().isCreated());
		AppUser regularUser = users.findByUsername(regularUsername).orElseThrow();
		regularUser.approve();
		users.save(regularUser);
		String userToken = requestToken(mockMvc, regularUsername, password);
		mockMvc.perform(get("/api/users").header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
				.andExpect(status().isForbidden());

		String adminUsername = "admin-" + UUID.randomUUID();
		users.save(new AppUser(adminUsername, passwordEncoder.encode(password), AppRole.ADMIN, AccountStatus.APPROVED));
		String adminToken = requestToken(mockMvc, adminUsername, password);
		MvcResult usersResult = mockMvc.perform(get("/api/users")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
				.andExpect(status().isOk())
				.andReturn();

		String response = usersResult.getResponse().getContentAsString();
		assertThat(response).contains(
				regularUsername,
				adminUsername,
				"Test User",
				regularUsername + "@example.com",
				"https://example.com/avatar.png",
				"USER",
				"ADMIN");
		assertThat(response).doesNotContain("passwordHash");
	}

	@Test
	void anyoneCanCreateAnotherUser() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"new-user-" + UUID.randomUUID()
								+ "\",\"password\":\"another-password\",\"name\":\"New User\"}"))
				.andExpect(status().isCreated());
	}

	private String requestToken(MockMvc mockMvc, String username, String password) throws Exception {
		MvcResult tokenResult = mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andReturn();
		Matcher tokenMatcher = ACCESS_TOKEN_PATTERN.matcher(tokenResult.getResponse().getContentAsString());
		assertThat(tokenMatcher.find()).isTrue();
		return tokenMatcher.group(1);
	}

	@Test
	void invalidCredentialsCannotReceiveToken() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
		mockMvc.perform(post("/api/auth/token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"unknown\",\"password\":\"incorrect-password\"}"))
				.andExpect(status().isUnauthorized());
	}

}
