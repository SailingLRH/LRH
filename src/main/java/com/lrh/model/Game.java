package com.lrh.model;

import java.io.Serializable;

public class Game implements Serializable {
    private Integer id;

    private Integer gameType;

    private Integer gameTime;

    private String gameName;

    private Integer userId;

    private String addTime;

    private String gameAll;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Integer getGameTime() {
        return gameTime;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    public String getGameAll() {
        return gameAll;
    }

    public void setGameAll(String gameAll) {
        this.gameAll = gameAll == null ? null : gameAll.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", gameType=").append(gameType);
        sb.append(", gameTime=").append(gameTime);
        sb.append(", gameName=").append(gameName);
        sb.append(", userId=").append(userId);
        sb.append(", addTime=").append(addTime);
        sb.append(", gameAll=").append(gameAll);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}