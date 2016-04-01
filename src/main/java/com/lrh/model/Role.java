package com.lrh.model;

import java.io.Serializable;

public class Role implements Serializable {
    private Integer id;

    private String roleName;

    private Boolean system;

    private Boolean music;

    private Boolean video;

    private Boolean photo;

    private Boolean product;

    private Boolean speak;

    private Boolean blog;

    private Boolean sport;

    private Boolean home;

    private Boolean game;

    private Boolean leaveMessage;

    private Boolean scenery;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean getMusic() {
        return music;
    }

    public void setMusic(Boolean music) {
        this.music = music;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Boolean getPhoto() {
        return photo;
    }

    public void setPhoto(Boolean photo) {
        this.photo = photo;
    }

    public Boolean getProduct() {
        return product;
    }

    public void setProduct(Boolean product) {
        this.product = product;
    }

    public Boolean getSpeak() {
        return speak;
    }

    public void setSpeak(Boolean speak) {
        this.speak = speak;
    }

    public Boolean getBlog() {
        return blog;
    }

    public void setBlog(Boolean blog) {
        this.blog = blog;
    }

    public Boolean getSport() {
        return sport;
    }

    public void setSport(Boolean sport) {
        this.sport = sport;
    }

    public Boolean getHome() {
        return home;
    }

    public void setHome(Boolean home) {
        this.home = home;
    }

    public Boolean getGame() {
        return game;
    }

    public void setGame(Boolean game) {
        this.game = game;
    }

    public Boolean getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(Boolean leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public Boolean getScenery() {
        return scenery;
    }

    public void setScenery(Boolean scenery) {
        this.scenery = scenery;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleName=").append(roleName);
        sb.append(", system=").append(system);
        sb.append(", music=").append(music);
        sb.append(", video=").append(video);
        sb.append(", photo=").append(photo);
        sb.append(", product=").append(product);
        sb.append(", speak=").append(speak);
        sb.append(", blog=").append(blog);
        sb.append(", sport=").append(sport);
        sb.append(", home=").append(home);
        sb.append(", game=").append(game);
        sb.append(", leaveMessage=").append(leaveMessage);
        sb.append(", scenery=").append(scenery);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}