package com.apptomate.schedularapp.model;

public class BusinessHoursModel
{
    private String name;
    private String day;
    private String businesshourId;
    private String userId;
    private String openTime;
    private String closeTime;
    private String isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBusinesshourId() {
        return businesshourId;
    }

    public void setBusinesshourId(String businesshourId) {
        this.businesshourId = businesshourId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
