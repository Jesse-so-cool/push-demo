package com.jesse.pushdemo.vo;

import java.util.List;


@SuppressWarnings("ALL")
public class PushDto {
    /**
     * 点击后传给应用客户端 JSON格式的参数 和loadUrl不一样
     */
    private String messagePayload;

    private String appName;
    private String title;
    private String description;
    /**
     * 别名
     */
    private List<String> aliasList;
    /**
     * 唯一id
     */
    private List<String> regIdList;
    /**
     * 小米userAccount
     */
    private List<String> userAccountList;
    /**
     * topic
     */
    private List<String> topicList;
    private String platForm;
    /**
     * 不能为空,字典项目 {1：消息通知,2：消息透传,3：消息通知打开应用,4： 消息通知打开网址}
     */
    private Integer pushType;
    /**
     * 当 pushType = 3 时，需要传入参数 loadUrl，当 pushType = 4 时，需要传入参数 url
     */
    private String loadUrl;
    private String url;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 标识此次推送的id

     */
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }




    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAliasList() {
        return aliasList;
    }

    public void setAliasList(List<String> aliasList) {
        this.aliasList = aliasList;
    }

    public List<String> getRegIdList() {
        return regIdList;
    }

    public void setRegIdList(List<String> regIdList) {
        this.regIdList = regIdList;
    }

    public List<String> getUserAccountList() {
        return userAccountList;
    }

    public void setUserAccountList(List<String> userAccountList) {
        this.userAccountList = userAccountList;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
