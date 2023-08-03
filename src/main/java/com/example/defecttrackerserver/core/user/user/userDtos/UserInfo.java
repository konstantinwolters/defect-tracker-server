package com.example.defecttrackerserver.core.user.user.userDtos;

import com.example.defecttrackerserver.core.user.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private Integer id;
    private String fullName;

    public UserInfo(User user) {
        this.id = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();
    }
}
