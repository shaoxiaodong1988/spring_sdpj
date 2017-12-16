package com.shendu.service.impl;

import com.shendu.dao.ModleDAO;
import com.shendu.dao.ModlePrototypeDAO;
import com.shendu.dao.VersionDAO;
import com.shendu.model.Modle;
import com.shendu.model.ModlePrototype;
import com.shendu.model.Version;
import com.shendu.service.ModleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
@Service
public class ModleServiceImpl implements ModleService {

    @Autowired
    private ModleDAO modleDAO;
    @Autowired
    private VersionDAO versionDAO;

    /**
     * 新增项目模块
     */
    @Transactional
    public void saveModle(Modle modle){
        if(modle.getId() != null) {
            modleDAO.update(modle);
        }else {
            modleDAO.save(modle);
        }
    }

    @Override
    public Modle queryModleById(Integer modleId) {
        return modleDAO.find(modleId);
    }

    @Override
    public List<Modle> queryModleList(List<Integer> modleIdList) {
        return modleDAO.queryModleListByIds(modleIdList);
    }

    /**
     * 修改项目模块
     * @param modle
     */
    public void updateModle(Modle modle){
        modleDAO.update(modle);
    }

    @Override
    public void delModle(Integer id) {
        modleDAO.delete(id);
    }

    /**
     * 项目模块分页条件查询
     * @param pageNo
     * @param pageSize
     * @param projectId
     * @param modleName
     * @param modleState
     * @return pageInfo
     */
    public PageInfo<Modle> queryModleList(Integer pageNo, Integer pageSize, Integer projectId, String modleName, String modleState){
        List<Modle> list = modleDAO.queryModle(pageNo,pageSize,projectId,modleName,modleState);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Version> queryModleVersionList(Integer pageNo, Integer pageSize, Integer modleId) {
        List<Version> versionList = versionDAO.queryModleVersionList(pageNo,pageSize,modleId);
        return new PageInfo<>(versionList);
    }

    /**
     * 获取项目模块列表
     * @param projectId
     * @return list
     */
    public List<Modle> findProjectModleList(Integer projectId){
        return modleDAO.queryModleList(projectId);
    }

}
