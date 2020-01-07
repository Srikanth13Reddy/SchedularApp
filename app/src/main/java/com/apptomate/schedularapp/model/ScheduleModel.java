package com.apptomate.schedularapp.model;

public class ScheduleModel
{
    private String name;
    private String phonenumber;
    private String doNotDisturbId;
    private String reason;
    private  String userId;
    private String day;
    private String openTime;
    private String closeTime;
    private String isAllowAlarmRings;
    private String isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDoNotDisturbId() {
        return doNotDisturbId;
    }

    public void setDoNotDisturbId(String doNotDisturbId) {
        this.doNotDisturbId = doNotDisturbId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getIsAllowAlarmRings() {
        return isAllowAlarmRings;
    }

    public void setIsAllowAlarmRings(String isAllowAlarmRings) {
        this.isAllowAlarmRings = isAllowAlarmRings;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
