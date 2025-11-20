package com.smart.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.biz.domain.BizTaskItem;
import com.smart.biz.mapper.BizTaskItemMapper;
import com.smart.biz.service.IBizTaskItemService;
import org.springframework.stereotype.Service;

@Service
public class BizTaskItemServiceImpl extends ServiceImpl<BizTaskItemMapper, BizTaskItem> implements IBizTaskItemService {
}
