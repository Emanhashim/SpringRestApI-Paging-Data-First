package com.bazra.usermanagement.model;

import static java.lang.Character.isUpperCase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.signup.SignUpRequest;
import com.bazra.usermanagement.signup.SignUpResponse;
import com.bazra.usermanagement.signup.UpdateRequest;
import com.bazra.usermanagement.signup.UpdateResponse;

/**
 * User information service
 * 
 * @author Bemnet
 * @version 4/2022
 *
 */
@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AgentRepository agentRepository;
    
    @Autowired
    AccountService accountservice;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	String password="";
    	if (agentRepository.findByUsername(email).isPresent()) {
    		AgentInfo agentInfo = agentRepository.findByUsername(email).get();
        	password= agentInfo.getPassword();
        	return new User(email, password, new ArrayList<>());
		}
    	else if (merchantRepository.findByUsername(email).isPresent()) {
    		MerchantInfo merchantInfo = merchantRepository.findByUsername(email).get();
        	password= merchantInfo.getPassword();
        	return new User(email, password, new ArrayList<>());
			
		}
    	
        UserInfo userInfo = userRepository.findByUsername(email).get();
        password = userInfo.getPassword();

        return new User(email, password, new ArrayList<>());

    }
    
//    public UserDetails loadAgentByUsername(String email) throws UsernameNotFoundException {
//
//        AgentInfo agentInfo = agentRepository.findByUsername(email).get();
//        String passwor = agentInfo.getPassword();
//
//        return new User(email, passwor, new ArrayList<>());
//
//    }

    //this is birthday checker for only integer value present
    public boolean checkBirthdate(String input) {
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
    public boolean checkUsername(String input) {
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
    public boolean checkname(String input) {
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
    public boolean checkLastname(String input) {
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
    public boolean checkString(String input) {
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

    /**
     * check of password validity
     * 
     * @param str
     * @return
     */
    public ResponseEntity<?> Validation(SignUpRequest request) {

        return ResponseEntity.badRequest().body(new SignUpResponse("Error: Invalid Password"));
    }

    /**
     * validates field for user signup
     * 
     * @param userInfo
     * @return signup status
     */
    
    
    public static String NumericString(int n)
    {
  
        // chose a Character random from this String
        String AlphaNumericString = "0123456789";
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
  
        for (int i = 0; i < n; i++) {
  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }
    

    /**
     * update password
     * 
     * @param request
     * @param newpassword
     * @return update status
     */
    public ResponseEntity<?> updatePassword(UpdateRequest request, String newpassword, String onldpass) {
        UserInfo userInfo = userRepository.findByUsername(request.getUsername()).get();

        userInfo.setPassword(newpassword);
        userRepository.save(userInfo);

        return ResponseEntity.ok(new UpdateResponse("successfully updated password"));

    }

    /**
     * checking for registered user
     * 
     * @param username
     * @param jwt
     * @return user
     */
//    public ResponseEntity<?> signInUser(UserDetails username, String jwt) {
//        UserInfo userInfo = userRepository.findByUsername(username.getUsername()).get();
//        boolean userExists = userRepository.findByUsername(userInfo.getUsername()).isPresent();
//
//        if (!userExists) {
//            System.out.println("Wrong username or password");
//        }
//
//        return ResponseEntity.ok(new SignInResponse(userInfo.getId(), userInfo.getUsername(), userInfo.getRoles(),
//                userInfo.getCountry(), userInfo.getGender(), jwt));
//    }

}
