package com.shendu.service.impl;

import com.shendu.dao.DemandDAO;
import com.shendu.dao.ModleDAO;
import com.shendu.dao.ModleDemandDAO;
import com.shendu.dao.VersionDAO;
import com.shendu.model.Demand;
import com.shendu.model.Modle;
import com.shendu.model.ModleDemand;
import com.shendu.model.Version;
import com.shendu.service.DemandService;
import com.shendu.utils.DateUtil;
import com.shendu.vo.DemandVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangxinguang on 2017/5/22 上午9:29.
 */
@Service
public class DemandServiceImpl implements DemandService {

    @Autowired
    private ModleDemandDAO modleDemandDAO;

    @Autowired
    private DemandDAO demandDAO;

    @Autowired
    private ModleDAO modleDAO;

    @Autowired
    private VersionDAO versionDAO;

    @Transactional
    public PageInfo<DemandVO> queryDemandListPage(Integer pageNo,Integer pagesSize,Integer projectId,String projectName,String demandName) {
        List<DemandVO> demandVOList = demandDAO.findDemandListPage(pageNo,pagesSize,projectId,projectName,demandName);
        return new PageInfo<>(demandVOList);
    }

    @Transactional
    public void saveDemand(Demand demand,List<Integer> modleIdList) {
        /**保存需求*/
        demand.setDemandStatus("0");
        demand.setPutTime(DateUtil.getDateTime(new Date()));
        demandDAO.save(demand);

        /**组装模块并批量保存*/
        List<ModleDemand> modleDemandList = new ArrayList<>();

        ModleDemand modleDemand = null;
        for(Integer modleId:modleIdList) {
            modleDemand = new ModleDemand();
            modleDemand.setModleId(modleId);
            modleDemand.setDemandId(demand.getId());
            modleDemandList.add(modleDemand);
        }
        if(!CollectionUtils.isEmpty(modleDemandList)) {
            modleDemandDAO.batchSave(modleDemandList);
        }

        /**组装并修改项目模块状态 为开发中*/
        List<Modle> modleList = new ArrayList<>();
        Modle modle = null;
        for(Integer modleId:modleIdList) {
            modle = new Modle();
            modle.setId(modleId);
            modle.setModleState("0");
            modleList.add(modle);
        }
        if(!CollectionUtils.isEmpty(modleList)) {
            modleDAO.batchUpdateState(modleList);
        }
    }

    @Transactional
    public void updateDemand(Demand demand,List<Integer> modleIdList) {
        /**更新需求信息*/
        demandDAO.update(demand);

        /**删除所有关联模块*/
        modleDemandDAO.deleteModleDemand(demand.getId());


        /**组装并批量增加 选择的模块*/
        List<ModleDemand> modleDemandList = new ArrayList<>();
        ModleDemand modleDemand = null;
        for(Integer modleId:modleIdList) {
            modleDemand = new ModleDemand();
            modleDemand.setDemandId(demand.getId());
            modleDemand.setModleId(modleId);
            modleDemandList.add(modleDemand);
        }
        if(!CollectionUtils.isEmpty(modleDemandList)) {
            modleDemandDAO.batchSave(modleDemandList);
        }
    }

    @Transactional
    public Demand queryDemand(Integer demandId) {
        return demandDAO.find(demandId);
    }

    @Transactional
    public void delDemand(Integer demandId) {
        demandDAO.delete(demandId);
    }

    @Transactional
    public void completeDemand(Demand demand) {
        /**修改需求信息*/
        List<ModleDemand> modleDemandList = modleDemandDAO.findModleDemandList(demand.getId());
        Demand demandOld = demandDAO.find(demand.getId());
        demandOld.setActualEndTime(demand.getActualEndTime());
        demandOld.setCompleteUserId(demand.getCompleteUserId());
        demandOld.setDemandStatus("1");//完成状态
        demandDAO.update(demandOld);

        /**组装并批量修改模块状态 为已完成*/
        List<Modle> modleList = new ArrayList<>();
        Modle modle = null;
        for(ModleDemand modleDemand:modleDemandList) {
            modle = new Modle();
            modle.setId(modleDemand.getModleId());
            modle.setModleState("1");
            modleList.add(modle);
        }

        if(!CollectionUtils.isEmpty(modleList)) {
            modleDAO.batchUpdateState(modleList);
        }

        /**批量停用旧模块版本*/
        List<Version> oldVersionList = versionDAO.queryVersionList(demand.getId());
        for(Version oldVersion:oldVersionList) {
            oldVersion.setVersionState(0);
        }

        if(!CollectionUtils.isEmpty(oldVersionList)) {
            versionDAO.batchUpdateState(oldVersionList);
        }

        /**组装版本并批量保存*/
        List<Version> versionList = new ArrayList<>();
        Version version = null;
        for(ModleDemand modleDemand:modleDemandList) {
            version = new Version();
            version.setDemandId(demand.getId());
            version.setModleId(modleDemand.getModleId());
            version.setUpTime(DateUtil.getDateTime(new Date()));
            version.setVersion(demandOld.getVersion());
            version.setUpUserId( demand.getPutUserId());
            version.setVersionState(1);
            version.setVersionId(-1);
            versionList.add(version);
        }

        if(!CollectionUtils.isEmpty(versionList)) {
            versionDAO.batchSave(versionList);
        }
    }
}
