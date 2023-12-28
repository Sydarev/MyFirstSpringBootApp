package ru.netology.conditionalapp.profiles;


public class DevProfile implements SystemProfile {
    @Override
    public String getProfile() {
        return "Current profile is Dev";
    }
}
