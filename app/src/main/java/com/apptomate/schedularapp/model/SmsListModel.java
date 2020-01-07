package com.apptomate.schedularapp.model;

public class SmsListModel
{
    private String userSmsSettingId;
    private String userId;
    private  String name;
    private  String phoneNumber;
    private String smsSettingId;
    private String smsUserText;
    private String timeDurationforUser;
    private String isDefaultText;
    private String isDurationSettingApplicable;

    public String getIsDurationSettingApplicable() {
        return isDurationSettingApplicable;
    }

    public void setIsDurationSettingApplicable(String isDurationSettingApplicable) {
        this.isDurationSettingApplicable = isDurationSettingApplicable;
    }

    public String getIsDefaultText() {
        return isDefaultText;
    }

    public void setIsDefaultText(String isDefaultText) {
        this.isDefaultText = isDefaultText;
    }

    public String getUserSmsSettingId() {
        return userSmsSettingId;
    }

    public void setUserSmsSettingId(String userSmsSettingId) {
        this.userSmsSettingId = userSmsSettingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsSettingId() {
        return smsSettingId;
    }

    public void setSmsSettingId(String smsSettingId) {
        this.smsSettingId = smsSettingId;
    }

    public String getSmsUserText() {
        return smsUserText;
    }

    public void setSmsUserText(String smsUserText) {
        this.smsUserText = smsUserText;
    }

    public String getTimeDurationforUser() {
        return timeDurationforUser;
    }

    public void setTimeDurationforUser(String timeDurationforUser) {
        this.timeDurationforUser = timeDurationforUser;
    }
}
