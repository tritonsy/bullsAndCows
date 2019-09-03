package com.bullsncows.controller;

import lombok.Data;

@Data
public class RegistrationForm {
    String login;
    String password;
    String passwordConfirm;
}
