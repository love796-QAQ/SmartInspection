package com.smart.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.biz.domain.BaseTemplate;

public interface IBaseTemplateService extends IService<BaseTemplate> {
    boolean insertTemplate(BaseTemplate template);
    boolean updateTemplate(BaseTemplate template);
    java.util.List<Long> selectTemplateItemIds(Long templateId);
}
