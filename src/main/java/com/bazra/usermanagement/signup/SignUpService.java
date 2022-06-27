package com.bazra.usermanagement.signup;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountService;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserInfoService;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.response.ResponseError;

import static java.lang.Character.isUpperCase;

@Service
public class SignUpService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AgentRepository agentRepository;
	@Autowired
	MerchantRepository merchantRepository;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AccountService accountservice;
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	//this is birthday checker for only integer value present
	private boolean checkBirthdate(String input) {
		char currentCharacter;
		boolean numberPresent = false;
		boolean lowerCasePresent = true;
		boolean upperCasePresent = true;
		for (int i = 1; i < input.length(); i++) {
			currentCharacter = input.charAt(i);
			if (Character.isDigit(currentCharacter)) {
				numberPresent = true;
			} else if (isUpperCase(currentCharacter)) {
				upperCasePresent = false;
			} else if (Character.isLowerCase(currentCharacter)) {
				lowerCasePresent = false;
			}
		}
		return numberPresent && upperCasePresent && lowerCasePresent;
	}
	//this checks user phone number
	private boolean checkUsername(String input) {
		char currentCharacter;
		boolean numberPresent = false;
		boolean lowerCasePresent = true;
		boolean upperCasePresent = true;
		for (int i = 9; i < input.length(); i++) {
			currentCharacter = input.charAt(i);
			if (Character.isDigit(currentCharacter)) {
				numberPresent = true;
			} else if (isUpperCase(currentCharacter)) {
				upperCasePresent = false;
			} else if (Character.isLowerCase(currentCharacter)) {
				lowerCasePresent = false;
			}
		}
		return numberPresent && upperCasePresent && lowerCasePresent;
	}

	//this for first name
	private boolean checkname(String input) {
		char currentCharacter;
		boolean numberPresent = true;
		boolean lowerCasePresent = false;
		boolean upperCasePresent = true;
		for (int i = 4; i < input.length(); i++) {
			currentCharacter = input.charAt(i);
			if (Character.isDigit(currentCharacter)) {
				numberPresent = false;
			} else if (isUpperCase(currentCharacter)) {
				upperCasePresent = false;
			} else if (Character.isLowerCase(currentCharacter)) {
				lowerCasePresent = true;
			}
		}
		return numberPresent && upperCasePresent && lowerCasePresent;
	}


	//this function checks lastName inputs
	private boolean checkLastname(String input) {
		char currentCharacter;
		boolean numberPresent = true;
		boolean lowerCasePresent = false;
		boolean upperCasePresent = true;
		for (int i = 4; i < input.length(); i++) {
			currentCharacter = input.charAt(i);
			if (Character.isDigit(currentCharacter)) {
				numberPresent = false;
			} else if (isUpperCase(currentCharacter)) {
				upperCasePresent = false;
			} else if (Character.isLowerCase(currentCharacter)) {
				lowerCasePresent = true;
			}
		}
		return numberPresent &&  lowerCasePresent && upperCasePresent ;
	}

	// this for password checker
	private boolean checkString(String input) {
		char currentCharacter;
		boolean numberPresent = false;
		boolean upperCasePresent = false;
		boolean lowerCasePresent = false;

		for (int i = 0; i < input.length(); i++) {
			currentCharacter = input.charAt(i);
			if (Character.isDigit(currentCharacter)) {
				numberPresent = true;
			} else if (isUpperCase(currentCharacter)) {
				upperCasePresent = true;
			} else if (Character.isLowerCase(currentCharacter)) {
				lowerCasePresent = true;
			}
		}

		return numberPresent && upperCasePresent && lowerCasePresent;
	}

	//this checks email
	public static boolean valEmail(String input){
		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
		Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailPat.matcher(input);
		return matcher.find();

	}



	public ResponseEntity<?> signUpUser(SignUpRequest request) {
		//this checks user phone number



		boolean userExists1 = userRepository.findByUsername(request.getUsername()).isPresent();
		boolean userExist1 = userRepository.findByEmail(request.getEmail()).isPresent();
		if (userExists1) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
		} else if (userExist1) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Email already in use!"));
		}
		String pass1 = request.getPassword();
		String pass2 = request.getConfirmPassword();


		if (!pass1.matches(pass2)) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Error: Passwords don't match!"));
		}
		if (accountRepository.findByAccountNumberEquals(request.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
		}
		UserInfo userInfo = new UserInfo(request.getFirstName(), request.getLastName(),
				passwordEncoder.encode(request.getPassword()), request.getUsername());
		userInfo.setCountry(request.getCountry());
		userInfo.setBirthday(request.getBirthDay());
		userInfo.setEmail(request.getEmail());
		userInfo.setRoles("USER");
		userRepository.save(userInfo);

		String strgender = request.getGender();

		if (strgender.matches("MALE")) {

			userInfo.setGender(strgender);

		} else if (strgender.matches("FEMALE")) {

			userInfo.setGender(strgender);
		}

		if (!userInfoService.checkString(pass1)) {
			return ResponseEntity.badRequest().body(
					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
		}

		if ((!checkname(userInfo.getFirstName()) )) {

			return ResponseEntity.badRequest().body(new SignUpResponse("First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if ((!checkLastname(userInfo.getLastName()) )) {

			return ResponseEntity.badRequest().body(new SignUpResponse("FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if (userInfo.getEmail().isBlank()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your email "));
		}
		if (userInfo.getFirstName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
		}
		if (userInfo.getLastName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father name "));
		}
		if (userInfo.getUsername().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
		}
		if (userInfo.getCountry().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your country "));
		}
		if ((!checkBirthdate(userInfo.getBirthday()) )) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Invalid date value"));
		}

		if ((!checkUsername(userInfo.getUsername()) )) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
		}

		userRepository.save(userInfo);
		UserInfo userInfo2 = userRepository.findByUsername(userInfo.getUsername()).get();
		Account account = new Account(userInfo2.getUsername(), new BigDecimal(1000), userInfo2.getId(),
				userInfo2.getUsername(), userInfo2.getRoles());
		String accountnumber = UserInfoService.NumericString(8);

		accountservice.save(account);
		return ResponseEntity.ok(new SignUpResponse(userInfo2.getUsername(), userInfo2.getRoles(),
				userInfo2.getCountry(), userInfo2.getGender(), "Successfully Registered", userInfo2.getFirstName(),
				userInfo2.getLastName()));
	}

	public ResponseEntity<?> signUpAgent(AgentSignUpRequest request) {
		boolean userExists1 = agentRepository.findByUsername(request.getUsername()).isPresent();
//        Aagent = accountRepository.findByusername(request.getUsername());
    	if (userExists1) {

            return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
    	}
        String pass1 = request.getPassword();
        String pass2 = request.getConfirmPassword();


        if (!pass1.matches(pass2)) {
            return ResponseEntity.badRequest().body(new SignUpResponse("Error: Passwords don't match!"));
        }
        if (accountRepository.findByAccountNumberEquals(request.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
		}

        AgentInfo agentInfo = new AgentInfo(request.getFirstName(), request.getLastName(),
                passwordEncoder.encode(request.getPassword()), request.getUsername());
        
        agentInfo.setLicenceNumber(request.getLicenceNumber());
        agentInfo.setCompanyName(request.getCompanyName());
        agentInfo.setBusinessLNum(request.getBusinessLNum());
        agentInfo.setRoles("AGENT");
        agentRepository.save(agentInfo);
        


		if (!userInfoService.checkString(pass1)) {
			return ResponseEntity.badRequest().body(
					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
		}

		if ((!userInfoService.checkname(agentInfo.getFirstName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if ((!userInfoService.checkLastname(agentInfo.getLastName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		
		if (agentInfo.getFirstName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
		}
		if (agentInfo.getLastName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father name "));
		}
		if (agentInfo.getUsername().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
		}
		if (agentInfo.getLicenceNumber().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your licence number "));
		}
		if (agentInfo.getBusinessLNum().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business licence number "));
		}
		

		if ((!userInfoService.checkUsername(agentInfo.getUsername()))) {

			return ResponseEntity.badRequest()
					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
		}

		agentRepository.save(agentInfo);
		AgentInfo agentInfo2 = agentRepository.findByUsername(agentInfo.getUsername()).get();

		Account account = new Account(agentInfo2.getUsername(), new BigDecimal(1000), agentInfo2.getId(),
				agentInfo2.getUsername(),agentInfo2.getRoles());

		
		accountservice.save(account);
		return ResponseEntity.ok(new SignUpResponse(agentInfo2.getUsername(), agentInfo2.getRoles(),
				 "Successfully Registered", agentInfo2.getFirstName(),
				 agentInfo2.getLastName()));
	}
	
	
	public ResponseEntity<?> signUpMerchant(AgentSignUpRequest request) {
		boolean userExists1 = merchantRepository.findByUsername(request.getUsername()).isPresent();
        
    	if (userExists1) {

            return ResponseEntity.badRequest().body(new SignUpResponse("Error: Username is already taken!"));
    	}
        String pass1 = request.getPassword();
        String pass2 = request.getConfirmPassword();


        if (!pass1.matches(pass2)) {
            return ResponseEntity.badRequest().body(new SignUpResponse("Error: Passwords don't match!"));
        }
        if (accountRepository.findByAccountNumberEquals(request.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Account already taken"));
		}
        MerchantInfo merchantInfo = new MerchantInfo(request.getFirstName(), request.getLastName(),
                passwordEncoder.encode(request.getPassword()), request.getUsername());
        
        merchantInfo.setLicenceNumber(request.getLicenceNumber());
        merchantInfo.setCompanyName(request.getCompanyName());
        merchantInfo.setBusinessLNum(request.getBusinessLNum());
        merchantInfo.setRoles("MERCHANT");
        merchantRepository.save(merchantInfo);
        


		if (!userInfoService.checkString(pass1)) {
			return ResponseEntity.badRequest().body(
					new SignUpResponse("Your password must have atleast 1 number, 1 uppercase and 1 lowercase letter"));
		} else if (pass1.chars().filter(ch -> ch != ' ').count() < 8
				|| pass1.chars().filter(ch -> ch != ' ').count() > 15) {
			return ResponseEntity.badRequest().body(new SignUpResponse("Your password must have 8 to 15 characters "));
		}

		if ((!userInfoService.checkname(merchantInfo.getFirstName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"First Name Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		if ((!userInfoService.checkLastname(merchantInfo.getLastName()))) {

			return ResponseEntity.badRequest().body(new SignUpResponse(
					"FatherName Should Start with One Uppercase and LoweCase letter, Minimum input 4 character and  String Character only"));
		}
		
		if (merchantInfo.getFirstName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your name "));
		}
		if (merchantInfo.getLastName().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your father name "));
		}
		if (merchantInfo.getUsername().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your phone "));
		}
		if (merchantInfo.getLicenceNumber().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your licence number "));
		}
		if (merchantInfo.getBusinessLNum().isBlank()) {

			return ResponseEntity.badRequest().body(new SignUpResponse("Enter your business licence number "));
		}
		

		if ((!userInfoService.checkUsername(merchantInfo.getUsername()))) {

			return ResponseEntity.badRequest()
					.body(new SignUpResponse("Phone Number Should be Integer Only and Minimum 10 Digit"));
		}

		merchantRepository.save(merchantInfo);
		MerchantInfo agentInfo2 = merchantRepository.findByUsername(merchantInfo.getUsername()).get();
		Account account = new Account(agentInfo2.getUsername(), new BigDecimal(1000), agentInfo2.getId(),
				agentInfo2.getUsername(),agentInfo2.getRoles());
		accountservice.save(account);
		return ResponseEntity.ok(new SignUpResponse(merchantInfo.getUsername(), merchantInfo.getRoles(),
				 "Successfully Registered", merchantInfo.getFirstName(),
				 merchantInfo.getLastName()));
	}

}

