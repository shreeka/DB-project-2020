package com.example.GrpSB.model;

public class Euser {

    public Euser(Long euid, String nickname) {
        this.euid = euid;
        this.nickname = nickname;
    }

    private Long euid;
    private String nickname;

    public Long getEuid() {
        return euid;
    }

    public void setEuid(Long euid) {
        this.euid = euid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
