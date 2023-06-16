package com.event.userservice.service;

import com.event.userservice.dto.*;
import com.event.userservice.exceptions.GenericBadRequestException;
import com.event.userservice.exceptions.ProfileNotFoundException;
import com.event.userservice.model.ApplicationUsers;
import com.event.userservice.model.Profile;
import com.event.userservice.repository.UserRepository;
import com.event.userservice.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final KafkaTemplate<String, KafkaTopic> kafkaTemplate;

    @Value("${profile.img.path}")
    private String PROFILE_IMG_PATH;

    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    @Transactional
    public void followUser(String followerUsername, String followeeUsername){
        userRepository.findByUsername(followerUsername).ifPresent(followerUser -> {
            var followeeUser = userRepository.findByUsername(followeeUsername).get();
            followeeUser.getFollowers().add(followerUser);
            followerUser.getFollowing().add(followeeUser);
            userRepository.save(followerUser);
            userRepository.save(followeeUser);
        });
        KafkaTopic kafkaTopic = KafkaTopic.builder()
                                .follower(followerUsername)
                                .followee(followeeUsername)
                                .build();
        kafkaTemplate.send("followUser", kafkaTopic);
    }

    public void addUser(ApplicationUserRequest userInfo){
        ApplicationUsers user = new ApplicationUsers();
        user.setEmail(userInfo.getEmail());
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());

        webClientBuilder.build().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("event-feed-service")
                        .path("/api/v1/event-feed/add-user/{username}")
                        .build(userInfo.getUsername()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) throws GenericBadRequestException {
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new GenericBadRequestException("There is no user with this username: " + loginRequest.getUsername()));
        var isProfileExists = user.getProfile() != null;

        if(user.getPassword().equals(loginRequest.getPassword())){
            return LoginResponse.builder()
                    .isSuccess(true)
                    .isProfileInfoFilled(isProfileExists)
                    .build();
        }

        return LoginResponse.builder()
                .isSuccess(false)
                .isProfileInfoFilled(false)
                .build();
    }

    @Transactional(readOnly = true)
    public FollowerResponse getFollowers(String username) throws GenericBadRequestException {
        return FollowerResponse.fromApplicationUser(userRepository.findByUsername(username)
                .orElseThrow(()-> new GenericBadRequestException("There is no user with this username: " + username)));
    }

    @Transactional(readOnly = true)
    public FollowingResponse getFollowees(String username) throws GenericBadRequestException {
        return FollowingResponse.fromApplicationUser(userRepository.findByUsername(username)
                .orElseThrow(()-> new GenericBadRequestException("There is no user with this username: " + username)));
    }

    @Transactional
    public void addProfileInfo(Profile profile, String username) throws GenericBadRequestException {
        var user = userRepository.findByUsername(username)
                        .orElseThrow(()-> new GenericBadRequestException("There is no user with this username: " + username));
        user.setProfile(profile);
        userRepository.save(user);
    }

    public Profile getProfileInfo(String username) throws GenericBadRequestException, ProfileNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(()-> new GenericBadRequestException("There is no user with this username: " + username));
        if(user.getProfile() == null){
            throw new ProfileNotFoundException("There is no profile information related with this username: " + username);
        }
        return user.getProfile();
    }


    @Transactional
    public void addProfilePhoto(MultipartFile img, String username) throws IOException {
        String absoluteImgPath = PROFILE_IMG_PATH + "/" + username;
        img.transferTo(new File(absoluteImgPath));
        var user = userRepository.findByUsername(username).get();
        user.getProfile().setProfilePhotoPath(absoluteImgPath);
        userRepository.save(user);

    }

    public byte[] getProfileImg(String username) throws IOException {
        var user = userRepository.findByUsername(username).get();
        var profile = user.getProfile();
        String filePath = profile.getProfilePhotoPath();

        byte[] imageInByte = Files.readAllBytes(new File(filePath).toPath());
        return imageInByte;
    }


    public boolean checkIfProfileExists(String username) throws GenericBadRequestException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new GenericBadRequestException("User not found"));
        return user.getProfile() != null;
    }

    @Transactional(readOnly = true)
    public ApplicationUserRestrictedResponse getUserInfo(String username) throws GenericBadRequestException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new GenericBadRequestException("User not found"));
        return ApplicationUserRestrictedResponse.fromApplicationUser(user);
    }

    public ApplicationUsersRestrictedResponse getUsersInfo(ApplicationUsersRestrictedInfoRequest restrictedInfoRequest) {
        List<ApplicationUserRestrictedResponse> responses = userRepository.findByUsernameIn(restrictedInfoRequest.getUsernames())
                .stream()
                .map(ApplicationUserRestrictedResponse::fromApplicationUser)
                .toList();
        return ApplicationUsersRestrictedResponse.builder()
                .usersInfo(responses)
                .build();
    }

    public ApplicationUsersRestrictedResponse getUsersByKeyword(String keyword) {
        var users = userRepository.findByUsernameContainingIgnoreCase(keyword);
        var responses = users.stream()
                .map(ApplicationUserRestrictedResponse::fromApplicationUser)
                .toList();
        return ApplicationUsersRestrictedResponse.builder()
                .usersInfo(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public FollowFollowingCountResponse getFollowerAndFollowingCount(String username) throws GenericBadRequestException {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(
                        ()->new GenericBadRequestException("There is no user found with the username of : " + username)
                );
        return FollowFollowingCountResponse.builder()
                .followerCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .build();
    }

    // is user1 following user2 ?
    @Transactional(readOnly = true)
    public boolean isFollowing(String user1, String user2) throws GenericBadRequestException {
        var user = userRepository
                .findByUsername(user1)
                .orElseThrow(
                        ()->new GenericBadRequestException("There is no user found with the username of : " + user1)
                );
        for(ApplicationUsers followee : user.getFollowing()){
            if(followee.getUsername().equals(user2)){
                return true;
            }
        }
        return false;
    }
}
