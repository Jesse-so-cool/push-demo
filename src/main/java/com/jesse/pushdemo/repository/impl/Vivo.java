package com.jesse.pushdemo.repository.impl;

import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.HttpProperties;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;
import com.vivo.push.sdk.notofication.Message;
import com.vivo.push.sdk.notofication.Result;
import com.vivo.push.sdk.notofication.TargetMessage;
import com.vivo.push.sdk.server.Sender;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jessehsj
 */
public class Vivo implements Manufacturer {
    @Override
    public ResponseBean sendMessage(PushDto pushDto) throws Exception {
        ResponseBean responseBean = new ResponseBean(false,-1,"请求失败",null);

        String appSecret = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appSecret");
        String appId = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appId");
        String appKey = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appKey");

        Sender sender = new Sender(appSecret);//注册登录开发平台网站获取到的appSecret
        //一天限制调用不超过 10000 次
        Result result = sender.getToken(Integer.parseInt(appId), appKey);//注册登录开发平台网站获取到的appId和appKey
        if (result.getResult() != 0) {
            responseBean.setResponseMsg(result.getDesc());
            return responseBean;
        }
        Sender senderMessage = new Sender(appSecret, result.getAuthToken());

        if (pushDto.getRegIdList().size() == 1) {
            Message singleMessage = buildSingleMessage(pushDto);
            Result resultMessage = senderMessage.sendSingle(singleMessage);
            if (resultMessage.getResult() != 0) {
                responseBean.setResponseMsg(resultMessage.getDesc());
                return responseBean;
            }
        }else {
            /**
             * 超过1个regId
             * 先保存到vivo生成taskId
             * 再根据taskId推送
             */
            Message saveMessage = buildMessage(pushDto);
            Result saveResult = senderMessage.saveListPayLoad(saveMessage);
            if (saveResult.getResult() != 0) {
                responseBean.setResponseMsg(saveResult.getDesc());
                return responseBean;
            }
            TargetMessage targetMessage = buildTargetMessage(pushDto,saveResult.getTaskId());
            Result resultMessage = senderMessage.sendToList(targetMessage);

            if (resultMessage.getResult() != 0) {
                responseBean.setResponseMsg(resultMessage.getDesc());
                return responseBean;
            }
        }


        responseBean.setIsSuccess(true);
        responseBean.setResponseMsg("推送成功");
        responseBean.setResponseCode(200);
        return responseBean;
    }

    private Message buildSingleMessage(PushDto pushDto) {
        Message.Builder builder = getBuilder(pushDto);

        builder = builder.regId(pushDto.getRegIdList().get(0));

        return builder.build();
    }

    private TargetMessage buildTargetMessage(PushDto pushDto, String taskId) {
        TargetMessage.Builder builder = new TargetMessage.Builder();

        Set<String> regIdList = new HashSet<String>();
        for (String regId : pushDto.getRegIdList()) {
            regIdList.add(regId);
        }
        builder = builder.regIds(regIdList)
                         .requestId(pushDto.getUuid())
                         .taskId(taskId);

        return builder.build();
    }

    private Message buildMessage(PushDto pushDto) {
        Message.Builder builder = getBuilder(pushDto);
        return builder.build();
    }

    private Message.Builder getBuilder(PushDto pushDto) {
        Message.Builder builder = new Message.Builder()
                .requestId(pushDto.getUuid())
                .notifyType(3)//震动
                .title(pushDto.getTitle())
                .content(pushDto.getDescription()).timeToLive(3600*24)
                .networkType(-1);
        if (pushDto.getPushType().equals(1)){
            //标记为通知栏 打开app首页
            builder = builder.skipType(1);
        }else if (pushDto.getPushType().equals(3)){
            //打开 app 内指定页面
            builder = builder.skipType(4);
            builder = builder.skipContent(pushDto.getLoadUrl());
        }else if (pushDto.getPushType().equals(4)){
            builder = builder.skipType(2)
                             .skipContent(pushDto.getUrl());// 打开链接
        }
        return builder;
    }

}
