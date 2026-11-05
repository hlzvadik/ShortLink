package dev.goltsov.shortlinksgenerator.security;

public class IncorrectLoginOrPassword extends IllegalArgumentException {
    public IncorrectLoginOrPassword(String message) {
        super(message);
    }
}
