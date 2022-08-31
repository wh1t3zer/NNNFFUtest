package com.ruoyi.web.controller.test;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.test.domain.Testersoure;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName：PushController
 * @Description：微信推送
 * @Author：chenyb
 * @Date：2020/11/27 10:33 上午
 * @Versiion：1.0
 */
@RestController
public class PushController extends BaseController {
    /*
     * 微信测试账号推送
     * */
    @PostMapping("/push")
    public String push(@RequestBody Testersoure testersoure ) {
        String openid = testersoure.getOpenId();
        String reason = testersoure.getReason1();
        System.out.println(reason);
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId("wxd04a21bf789bf6fa");
        wxStorage.setSecret("352add706489309f69247e0b9cd97a4c");
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId("MRYhAWll_4lyns0kEwpKXqYu3MW0JMOqRkqDf_SmAqA")//模版id
                .url("https://h5.nfuca.com/ncg/tester.html")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
                templateMessage.addData(new WxMpTemplateData("keyword1", reason, "#FF00FF"));
                templateMessage.addData(new WxMpTemplateData("keyword2", dateFormat.format(date), "#FF00FF"));
                templateMessage.addData(new WxMpTemplateData("remark", "", "#FF00FF"));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            return "推送成功";
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
            return "推送失败";
        }
    }
}
