package com.springboot.Service;

import com.springboot.Model.RefreshToken;
import com.springboot.Model.User;
import com.springboot.Repository.RefreshTokenRepository;
import com.springboot.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImp implements  RefreshTokenService{
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    public UserRepository userRepository;

    public long refershTokenValidity = 5*60*60*10000;

    @Override
    public RefreshToken createRefershToken(String email) {
        // TODO Auto-generated method stub
        User user = userRepository.findByEmail(email);
        RefreshToken refershToken = user.getRefreshToken();
        //If refresh token is not generated than generate new refresh token.
        if(refershToken == null) {
            refershToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refershTokenValidity))
                    .user(user)
                    .build();
        }
        //If refresh token is already generated than only expiry time will we updated.
        else {
            refershToken.setExpiry(Instant.now().plusMillis(refershTokenValidity));
        }
        user.setRefreshToken(refershToken);
        refreshTokenRepository.save(refershToken);
        return refershToken;
    }

    @Override
    public RefreshToken verifyRefershToken(String refreshToken) {
        // TODO Auto-generated method stub
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refersh token does not exist"));
        if(token.getExpiry().compareTo(Instant.now()) <0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refersh Token Expired");
        }
        return token;
    }
}
