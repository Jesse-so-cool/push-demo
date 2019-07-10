package com.jesse.pushdemo.repository.impl;

import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.HttpProperties;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

/**
 * @author jessehsj
 */
public class XiaoMi implements Manufacturer {

    private String APP_SECRET_KEY = "ufFWF6ydsXEDpKL8fBhchA==";

    private String MY_PACKAGE_NAME = "com.jesse.push";

    @Override
    public ResponseBean sendMessage(PushDto pushDto) {
        ResponseBean responseBean = new ResponseBean(false, -1, "推送失败", null);
        try {
            String secretKey = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase() + "_" + pushDto.getAppName(), "appSecret");
            Constants.useOfficial();
            Sender sender = new Sender(secretKey);
            Message message = buildAndroidMessage(pushDto);
            if (pushDto.getRegIdList() == null || pushDto.getRegIdList().size() == 0) {
                responseBean.setResponseMsg("regId为空");
                return responseBean;
            }
            Result result = sender.send(message, pushDto.getRegIdList(), 3);
            if (result.getErrorCode() != ErrorCode.Success) {
                responseBean.setResponseMsg(result.getMessageId() + "消息发送失败:" + result.getReason());
                return responseBean;
            }
            responseBean.setIsSuccess(true);
            responseBean.setResponseMsg("推送成功");
            responseBean.setResponseCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBean;
    }

    private Message buildAndroidMessage(PushDto pushDto) {
        String packageName = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase() + "_" + pushDto.getAppName(), "package");

        Message.Builder builder = new Message.Builder()
                .title(pushDto.getTitle())
                .description(pushDto.getDescription())
                //透传时回传给app
                //.payload(pushDto.getMessagePayload())
                .restrictedPackageName(packageName).notifyType(1);
        if (pushDto.getPushType().equals(2)) {
            //标记为消息透传
            builder = builder.passThrough(1);
        } else if (pushDto.getPushType().equals(1)) {
            //标记为通知栏
            builder = builder.passThrough(0);
        } else if (pushDto.getPushType().equals(3)) {
            builder = builder.passThrough(0);
            builder = builder.extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                    .extra(Constants.EXTRA_PARAM_INTENT_URI, pushDto.getLoadUrl());
            //.extra(Constants.EXTRA_PARAM_INTENT_URI, "intent:#Intent;component=com.xiaomi.mipushdemo/.NewsActivity;end");
        } else if (pushDto.getPushType().equals(4)) {
            builder = builder.passThrough(0);
            builder = builder.extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_WEB)
                    .extra(Constants.EXTRA_PARAM_WEB_URI, pushDto.getUrl());
        }

        return builder.build();
    }
}
