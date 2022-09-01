package com.ruoyi.test.service.impl;

import com.ruoyi.test.domain.ExportInfo;
import com.ruoyi.test.mapper.ExportInfoMapper;
import com.ruoyi.test.service.IExportInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportInfoServiceImplService implements IExportInfoService {

    @Autowired
    private ExportInfoMapper exportInfoMapper;

    @Override
    public List<ExportInfo> selectExportInfo() {
        return exportInfoMapper.selectExportInfo();
    }
}
