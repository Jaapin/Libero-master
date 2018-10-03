package com.jaapin.library.user.manager;


import com.jaapin.library.libero.anonotation.ClassId;

@ClassId("a")
public interface IUserManager {

    public void setUserName(String name);

    public String getUserName();
}
