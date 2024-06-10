package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.model.Profile;
import com.example.onlinebankingsystem.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void creatProfile(Profile profile) {
        profileRepository.save(profile);
    }

   public Profile findProfile(Long id) {
        return profileRepository.findById(id).get();
    }

    public Profile getProfile(Long id) {
        return profileRepository.findById(id).orElseThrow();
    }

    public void updateProfile(Profile profile) {
        getProfile(profile.getId());
        profileRepository.save(profile);
    }
}
