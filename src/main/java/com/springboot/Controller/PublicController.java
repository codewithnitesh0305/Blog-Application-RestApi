package com.springboot.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.DTO.UserDto;
import com.springboot.File.FileUpload;
import com.springboot.Model.Comment;
import com.springboot.Model.Post;
import com.springboot.Model.RefreshToken;
import com.springboot.Model.User;
import com.springboot.Payload.JwtRequest;
import com.springboot.Payload.JwtResponse;
import com.springboot.Payload.RefreshTokenRequest;
import com.springboot.Payload.SuccessResponse;
import com.springboot.Repository.PostRepository;
import com.springboot.Security.JwtHelper;
import com.springboot.Service.RefreshTokenService;
import com.springboot.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@Tag(name = "Public Api", description = "This endpoint can access everyone (No need to authenticate)")
public class PublicController {
	@Autowired
    private UserService service;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private PostRepository postRepository;
    
	@PostMapping("/SignUp")
	@Operation(summary = "SignUp endpoint, New user can signup by this endpoint")
	public ResponseEntity<?> uploadFileWithData(@RequestParam String user, @RequestParam MultipartFile file)throws Exception {
		String filename = fileUpload.uploadFile(file);
		if (filename != null) {
			ObjectMapper objectMapper = new ObjectMapper();

			User readValue = objectMapper.readValue(user, User.class);
			readValue.setProfileImageName(filename);
			User saveUser = service.createUser(readValue);
			String imageUrlByName = fileUpload.getImageUrlByName(saveUser.getProfileImageName(), "api");
			saveUser.setUrl(imageUrlByName);
			SuccessResponse<User> response = new SuccessResponse<>("Success", "User SignUp Successfully", saveUser,HttpStatus.CREATED);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Data and File Upload Failed...", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

    @PostMapping("/Login")
    @Operation(summary = "Login endpoint, User can login by this endpoint")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        //this.doAuthenticate(request.getEmail(), request.getPassword());
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        String token = this.helper.generateToken(authentication);
        RefreshToken refershToken = refreshTokenService.createRefershToken(authentication.getName());
        JwtResponse response = JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .refreshToken(refershToken.getRefreshToken())
                .build();
		SuccessResponse<JwtResponse> successResponse = new SuccessResponse<>("Success","User login successfully!",response,HttpStatus.OK);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/Refresh")
    @Operation(summary = "Refresh token endpoint, User can get again the JWT token by this endpoint")
    public ResponseEntity<?> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefershToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String generateToken = this.helper.generateToken(authentication);
        JwtResponse response = JwtResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .token(generateToken)
                .username(userDetails.getUsername())
                .build();
		SuccessResponse<JwtResponse> successResponse = new SuccessResponse<>("Success","Refresh token generate successfully!",response,HttpStatus.OK);
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
