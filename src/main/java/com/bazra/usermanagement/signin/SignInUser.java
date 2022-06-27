package com.bazra.usermanagement.signin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.MerchantInfo;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserInfoService;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.MerchantRepository;
import com.bazra.usermanagement.repository.UserRepository;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Signin Controller
 * 
 * @author Bemnet
 * @version 4/2022
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api")
@Api(value = "SignIn User Endpoint", description =
"HERE WE TAKE USER'S PHONENUMEBR AND PASSWORD TO LOGGEDIN")
@ApiResponses(value ={

        @ApiResponse(code = 404, message = "web user that a requested page is not available "),
        @ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
        @ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
        @ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
        @ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class SignInUser {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoService userInfoService;

    private UserInfo userInfo;

    private UserDetails userDetails;
    private AgentInfo agentInfo;
    private MerchantInfo merchantInfo;
    Account account;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Generate authentication token
     * @param authenticationRequest
     * @return user info plus jwt
     * @throws AuthenticationException
     */
    
    @PostMapping("/signin/user")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest authenticationRequest)
            throws AuthenticationException {
        boolean userExists = userRepository.findByUsername(authenticationRequest.getUsername()).isPresent();
        if (userExists) {

            userDetails = userInfoService.loadUserByUsername(authenticationRequest.getUsername());
            userInfo = userRepository.findByUsername(authenticationRequest.getUsername()).get();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            userDetails = userInfoService.loadUserByUsername(authenticationRequest.getUsername());
            
            final String jwt = jwtUtil.generateToken(userDetails);
            
            userRepository.findByUsername(authenticationRequest.getUsername()).get().setResetPasswordToken(jwt);
            userInfo = userRepository.findByUsername(authenticationRequest.getUsername()).get();
            try {
            	account = accountRepository.findByAccountNumberEquals(userInfo.getUsername()).get();
    		} catch (Exception e) {
    			return ResponseEntity.badRequest().body(new ResponseError("Account not found"));
    		}
//            Account account = accountRepository.findByAccountNumberEquals(userInfo.getUsername()).get();
            return ResponseEntity.ok(new SignInResponse(userInfo.getId(),  userInfo.getUsername(), userInfo.getFirstName(), userInfo.getRoles(),
                    userInfo.getCountry(), account.getBalance(), userInfo.getGender(), jwt));
            
        }

         return ResponseEntity.badRequest().body(new SigninErrorResponse("Error: Invalid username or password")); 

    }
    @PostMapping("/signin/agent")
    public ResponseEntity<?> createAuthenticationToken1(@RequestBody AgentSignInRequest agentSignInRequest)
            throws AuthenticationException {
    boolean userExists = agentRepository.findByUsername(agentSignInRequest.getUsername()).isPresent();
    if (userExists) {

        userDetails = userInfoService.loadUserByUsername(agentSignInRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        		agentSignInRequest.getUsername(), agentSignInRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        userDetails = userInfoService.loadUserByUsername(agentSignInRequest.getUsername());
        
        final String jwt = jwtUtil.generateToken(userDetails);

       
        agentInfo = agentRepository.findByUsername(agentSignInRequest.getUsername()).get();
        try {
        	account = accountRepository.findByAccountNumberEquals(agentInfo.getUsername()).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account not found"));
		}
        
        return ResponseEntity.ok(new AgentSignInResponse(agentInfo.getId(), agentInfo.getUsername(), agentInfo.getRoles(),account.getBalance(),  jwt));
        
    }

     return ResponseEntity.badRequest().body(new SigninErrorResponse("Error: Invalid username or password")); 
    }
    
    
    @PostMapping("/signin/merchant")
    public ResponseEntity<?> createAuthenticationToken2(@RequestBody AgentSignInRequest agentSignInRequest)
            throws AuthenticationException {
    boolean userExists = merchantRepository.findByUsername(agentSignInRequest.getUsername()).isPresent();
    if (userExists) {

        userDetails = userInfoService.loadUserByUsername(agentSignInRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        		agentSignInRequest.getUsername(), agentSignInRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        userDetails = userInfoService.loadUserByUsername(agentSignInRequest.getUsername());
        
        final String jwt = jwtUtil.generateToken(userDetails);

       
        merchantInfo = merchantRepository.findByUsername(agentSignInRequest.getUsername()).get();
        try {
        	account = accountRepository.findByAccountNumberEquals(merchantInfo.getUsername()).get();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ResponseError("Account not found"));
		}
//        Account account = accountRepository.findByAccountNumberEquals(merchantInfo.getUsername()).get();
        return ResponseEntity.ok(new AgentSignInResponse(merchantInfo.getId(), merchantInfo.getUsername(), merchantInfo.getRoles(),account.getBalance(),  jwt));
        
    }

     return ResponseEntity.badRequest().body(new SigninErrorResponse("Error: Invalid username or password")); 
    }


}
