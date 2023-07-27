package com.example.defecttrackerserver.core.user.user.userDtos;

import com.example.defecttrackerserver.core.user.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    Integer id;
    String name;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getFirstName() + " " + user.getLastName();
    }
}
