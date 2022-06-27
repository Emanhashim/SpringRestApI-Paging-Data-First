package com.bazra.usermanagement.signup;

import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.Roles;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserInfoService;
import com.bazra.usermanagement.repository.RoleRepository;
import com.bazra.usermanagement.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import javax.management.loading.PrivateClassLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * SignUp Controller
 * 
 * @author Bemnet
 * @version 4/2022
 */

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@Api(value = "Signup User Endpoint", description = "HERE WE TAKE USER'S DATA TO REGISTER TO BAZRA WALLET")
@ApiResponses(value ={
        @ApiResponse(code = 404, message = "web user that a requested page is not available "),
        @ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
        @ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
        @ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
        @ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
@AllArgsConstructor

public class SignUpUser {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SignUpService signUpService;
    @Autowired
    RoleRepository roleRepository;
    
//    public SignUpUser(SignUpService signUpService) {
//    	this.signUpService=signUpService;
//    }
    /**
     * Handles user SignUp request
     * 
     * @param request( user input)
     * @return 
     * @return 
     * @return signup validation response
     */
    @PostMapping("/signup/agent")
    public ResponseEntity<?> registerAgent(@RequestBody AgentSignUpRequest request) {
		
    	 return signUpService.signUpAgent(request);
    }
    @PostMapping("/signup/user")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request) {

    	 return signUpService.signUpUser(request);
    }
    @PostMapping("/signup/merchant")
    public ResponseEntity<?> registerMerchant(@RequestBody AgentSignUpRequest request) {
		
    	 return signUpService.signUpMerchant(request);
    }
        
    
}
