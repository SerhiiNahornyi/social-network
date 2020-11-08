package com.kpi.project.model;

import lombok.NonNull;

public class NewUser {
    private String userName;
    private String login;
    private String password;
    private boolean isLogin;

    public NewUser(@NonNull String userName, @NonNull String login , @NonNull String password){
        this.userName = userName;
        this.login = login;
        this.password = password;
        this.isLogin = false;
    }
}
