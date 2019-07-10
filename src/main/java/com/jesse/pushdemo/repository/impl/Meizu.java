package com.jesse.pushdemo.repository.impl;

import com.jesse.pushdemo.repository.Manufacturer;
import com.jesse.pushdemo.util.HttpProperties;
import com.jesse.pushdemo.util.ResponseBean;
import com.jesse.pushdemo.vo.PushDto;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ErrorCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;

import java.util.List;
import java.util.Map;

/**
 * @author jesse hsj
 */
public class Meizu implements Manufacturer {
    //一批最多不能超过1000个 多个英文逗号分割必填
    @Override
    public ResponseBean sendMessage(PushDto pushDto) throws Exception {
        ResponseBean responseBean = new ResponseBean(false, -1, "请求失败", null);

        String appSecret = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appSecret");
        String appId = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appId");
        String appKey = HttpProperties.getPrefixVal(pushDto.getManufacturer().toLowerCase()+"_"+pushDto.getAppName(), "appKey");

        IFlymePush push = new IFlymePush(appSecret);
        if (pushDto.getPushType() != 2) {
            VarnishedMessage.Builder builder = new VarnishedMessage.Builder();
            builder = builder.appId(Long.parseLong(appId))
                   .title(pushDto.getTitle())
                   .content(pushDto.getDescription());
            if (pushDto.getPushType() == 3) {
                builder = builder.clickType(2)
                                 .url(pushDto.getLoadUrl());
            } else if (pushDto.getPushType() == 4) {
                builder = builder.clickType(2)
                        .url(pushDto.getUrl());
            }
            VarnishedMessage message = builder.build();
            ResultPack<PushResult> result = push.pushMessage(message, pushDto.getRegIdList(), 3);


            if (result.isSucceed()) {
                // 2 调用推送服务成功 （其中map为设备的具体推送结果，一般业务针对超速的code类型做处理）
                PushResult pushResult = result.value();
                String msgId = pushResult.getMsgId();//推送消息ID，用于推送流程明细排查
                Map<String, List<String>> targetResultMap = pushResult.getRespTarget();//推送结果，全部推送成功，则map为empty
                System.out.println("push msgId:" + msgId);
                System.out.println("push targetResultMap:" + targetResultMap);
                if (targetResultMap != null && !targetResultMap.isEmpty()) {
                    // 3 判断是否有获取超速的target
//                    if (targetResultMap.containsKey(PushResponseCode.RSP_SPEED_LIMIT.getValue())) {
//                        // 4 获取超速的target
//                        List<String> rateLimitTarget = targetResultMap.get(PushResponseCode.RSP_SPEED_LIMIT.getValue());
//                        System.out.println("rateLimitTarget is :" + rateLimitTarget);
//                    }
                }else {
                    responseBean.setIsSuccess(true);
                    responseBean.setResponseMsg("推送成功");
                    responseBean.setResponseCode(200);
                }
            } else {
                //全部超速 应用请求频率过快
                if (String.valueOf(ErrorCode.APP_REQUEST_EXCEED_LIMIT.getValue()).equals(result.code())) {
                    responseBean.setResponseMsg(ErrorCode.APP_REQUEST_EXCEED_LIMIT.getDescription());//TODO 5 业务处理，重推......
                }
                System.out.println(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
                responseBean.setResponseMsg(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));//TODO 5 业务处理，重推......

            }

        }

        return responseBean;
    }
}
