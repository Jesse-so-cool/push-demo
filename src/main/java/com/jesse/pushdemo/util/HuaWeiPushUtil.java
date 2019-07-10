package com.jesse.pushdemo.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jesse.pushdemo.vo.PushDto;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

@SuppressWarnings("ALL")
public class HuaWeiPushUtil {
    private static String appSecret = "";
    private static String appId = "";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）
    private static String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token"; //获取认证Token的URL
    private static String apiUrl = "https://api.push.hicloud.com/pushsend.do"; //应用级消息下发API
    private static String accessToken = "CF0daUb31UG+a4AEA30w0Rok2a914BtXvzTeoqrs5Ac/DGWnyvDMp5tziuAGhW/o4LQkcZ8gLV5BkRru09eHNw==";//下发通知消息的认证Token 一小时一换
    private static long tokenExpiredTime = 1562208584717L;  //accessToken的过期时间


    public static ResponseBean sendPushMessage(PushDto pushDto) throws IOException {
        if (appId == "") appId = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appId");
        if (appSecret == "") appSecret = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appSecret");


        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshToken(pushDto);
        }
        /*PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败*/
        /*PushToken不支持手动编写，需使用客户端的onToken方法获取*/
        JSONArray deviceTokens = JSONArray.parseArray(JSON.toJSONString(pushDto.getRegIdList()));

        JSONObject body = new JSONObject();//仅通知栏消息需要设置标题和内容，透传消息key和value为用户自定义
        body.put("title", pushDto.getTitle());//消息标题
        body.put("content", pushDto.getDescription());//消息内容体

        JSONObject param = new JSONObject();
        String packageName = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "package");
        param.put("appPkgName", packageName);//定义需要打开的appPkgName
        param.put("url", pushDto.getUrl());

        JSONObject action = new JSONObject();
        JSONObject msg = new JSONObject();
        /*
         * 1 透传异步消息
         * 3 系统通知栏异步
         */
        msg.put("type", 3);
        /*
         *  action.type
         * 1 自定义行为：行为由参数intent定义
         * 2 打开URL：URL地址由参数url定义
         * 3 打开APP：默认值，打开App的首页
         */
        action.put("type", 3);//不赋值不显示  故默认显示app首页
        if (pushDto.getPushType() == 4) {
            action.put("type", 2);//2 打开URL：URL地址由参数url定义
        } else if (pushDto.getPushType() == 3) {
            action.put("type", 1);//类型3为打开APP：默认值，打开App的首页
            param.put("intent", pushDto.getLoadUrl());//打开APP自定义行为
        }
        if (pushDto.getPushType() == 2) {
            msg.put("type", 1);
        }
        action.put("param", param);//消息点击动作参数

        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容

        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        //ext.put("icon", "http://pubfile.bluemoon.com.cn/group1/M00/3D/8A/wKgwb1vIKEeAe6NpAAANSWshvpA091.jpg");//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL

        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);
        hps.put("ext", ext);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload.toString(), "UTF-8"));
        System.out.println(JSON.toJSONString(payload));
        System.out.println(postBody);
        String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        String result = HttpUtil.httpPost(postUrl, postBody, 15000, 15000);
        HuaWeiResultBean resultBean = JSON.parseObject(result, HuaWeiResultBean.class);
        ResponseBean responseBean = new ResponseBean(false, -1, "请求失败", null);
        responseBean.setResponseMsg(resultBean.getMsg());
        if (resultBean.getCode()==80000000){
            responseBean.setIsSuccess(true);
            responseBean.setResponseCode(200);
        }
        return responseBean;
    }

    static void refreshToken(PushDto pushDto) throws IOException {

        String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(appSecret, "UTF-8"), appId);
        String response = HttpUtil.httpPost(tokenUrl, msgBody, 5000, 5000);
        JSONObject obj = JSONObject.parseObject(response);
        accessToken = obj.getString("access_token");
        System.out.println("accessToken:"+accessToken);
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") * 1000;
        System.out.println("tokenExpiredTime: "+tokenExpiredTime);

    }


}
