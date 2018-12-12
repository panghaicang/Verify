package com.orange.verify.admin.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.orange.verify.admin.mapper.SoftMapper;
import com.orange.verify.admin.mapper.SoftVersionsMapper;
import com.orange.verify.api.bean.Soft;
import com.orange.verify.api.bean.SoftVersions;
import com.orange.verify.api.service.SoftVersionsService;
import com.orange.verify.api.vo.SoftVersionsVo;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SoftVersionsProvider extends ServiceImpl<SoftVersionsMapper, SoftVersions> implements SoftVersionsService {

    @Autowired
    private SoftMapper softMapper;

    @Override
    public Page<SoftVersionsVo> getPageBySoftId(String softId, Page page) {

        return page.setRecords(super.baseMapper.getPageBySoftId(softId,page));
    }

    @Override
    public boolean saveLogic(SoftVersions softVersions) {

        //去取软件是否存在
        Soft soft = softMapper.selectById(softVersions.getSoftId());
        if (soft == null) {
            return false;
        }

        // 去取软件id有没有在版本表里面
        // 有 >>> 新增失败 没有 >>> 做新增
        int count = super.count(new QueryWrapper<SoftVersions>().eq("soft_id",softVersions.getSoftId()));
        if (count > 0) {
            return false;
        }

        return super.save(softVersions);
    }

}
