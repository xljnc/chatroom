//package com.wt.content.chatroom.util;
//
//import ai.ii.ipark.cloud.shield.talent.biz.EnterpriseBiz;
//import ai.ii.ipark.cloud.shield.talent.biz.EnterpriseStService;
//import ai.ii.ipark.cloud.shield.talent.biz.TalentEnterpriseBiz;
//import ai.ii.ipark.cloud.shield.talent.dto.req.EnterpriseSaveDTO;
//import ai.ii.ipark.cloud.shield.talent.dto.req.TalentEnterpriseQueryDTO;
//import ai.ii.ipark.cloud.shield.talent.dto.req.TalentEnterpriseSaveDTO;
//import ai.ii.ipark.cloud.shield.talent.entity.Enterprise;
//import ai.ii.ipark.cloud.shield.talent.entity.EnterpriseStDO;
//import ai.ii.ipark.cloud.shield.talent.entity.TalentEnterprise;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wt.content.chatroom.core.RocketMQConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @auther: 一贫
// * @date: 2019/9/11 11:04
// * @description:
// */
//@Component
//@Slf4j
//public class TalentEnterpriseMQConsumer {
//
//    @Autowired
//    private EnterpriseBiz enterpriseBiz;
//
//    @Autowired
//    private TalentEnterpriseBiz talentEnterpriseBiz;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Value("${enterprise.synchronize.fields}")
//    private List<String> enterpriseSynchronizeFields;
//
//    @Autowired
//    private EnterpriseStService enterpriseStService;
//
//    /**
//     * 更新企业信息消息,tag为update_enterprise
//     **/
//    @StreamListener(RocketMQConfig.UPDATE_ENTERPRISE_INPUT)
//    public void updateEnterprise(@Payload EnterpriseSaveDTO saveDTO) throws Exception {
//        try {
//            Enterprise currE = enterpriseBiz.getEnterpriseByKey(saveDTO.getTenantId(), saveDTO.getId());
//            if (currE != null && !currE.getName().equals(saveDTO.getName())) {
//                log.info("试图更新企业名称，放弃。");
//                return;
//            }
//            Map<String, Object> info = new HashMap<>();
//            for (String key : saveDTO.getInfo().keySet()) {
//                if (enterpriseSynchronizeFields.contains(key))
//                    info.put(key, saveDTO.getInfo().get(key));
//            }
//            saveDTO.setInfo(info);
//            enterpriseBiz.saveEnterpriseMQ(saveDTO);
//        } catch (Exception e) {
//            log.error("更新企业信息失败,参数{}", objectMapper.writeValueAsString(saveDTO), e);
//            throw e;
//        }
//    }
//
//    /**
//     * 更新人才企业信息消息,tag为update_talent
//     **/
//    @StreamListener(RocketMQConfig.UPDATE_TALENT_ENTERPRISE_INPUT)
//    public void updateTalentEnterprise(@Payload TalentEnterpriseSaveDTO saveDTO) throws Exception {
//        try {
//            TalentEnterpriseQueryDTO queryDTO = TalentEnterpriseQueryDTO.builder()
//                    .tenantId(saveDTO.getTenantId()).userCode(saveDTO.getUserCode()).build();
//            TalentEnterprise te = talentEnterpriseBiz.getByTidAndUcode(queryDTO);
//            if (te != null && !te.getInfo().get("企业名称").equals(saveDTO.getInfo().get("企业名称"))) {
//                log.info("试图更新人才信息的企业名称，放弃。");
//                return;
//            }
//            talentEnterpriseBiz.saveTalentEnterpriseMQ(saveDTO);
//        } catch (Exception e) {
//            log.error("更新人才企业信息失败,参数{}", objectMapper.writeValueAsString(saveDTO), e);
//            throw e;
//        }
//    }
//
//
//    /**
//     * 企业统计消息,tag为enterprise_statistic
//     **/
//    @StreamListener(RocketMQConfig.ENTERPRISE_STATISTIC_INPUT)
//    public void updateTalentEnterprise(@Payload EnterpriseStDO enterpriseStDO) throws Exception {
//        try {
//            enterpriseStService.updateEnterprise(enterpriseStDO.getTenantId(), enterpriseStDO.getEnterpriseId(),
//                    enterpriseStDO.getIndustry(), enterpriseStDO.getParkLocation());
//        } catch (Exception e) {
//            log.error("企业统计消息消费失败,参数{}", objectMapper.writeValueAsString(enterpriseStDO), e);
//            throw e;
//        }
//    }
//
//}
