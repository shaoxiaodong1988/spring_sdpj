package com.shendu.service.impl;

import com.shendu.dao.ModleDAO;
import com.shendu.dao.ModlePrototypeDAO;
import com.shendu.dao.ProjectDao;
import com.shendu.model.Modle;
import com.shendu.model.ModlePrototype;
import com.shendu.model.Project;
import com.shendu.service.ProjectService;
import com.shendu.utils.DateUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangxinguang on 2017/5/11.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ModlePrototypeDAO modlePrototypeDAO;

    @Autowired
    private ModleDAO modleDAO;

    @Transactional
    public void saveProject(Project project,List<Integer> modleIdList) {
        project.setOperateTime(DateUtil.getDateTime(new Date()));

        if(project.getId() != null) {
            projectDao.update(project);
        }else {
            projectDao.save(project);

            /*批量组装保存模块*/
            List<ModlePrototype> modlePrototypeList = modlePrototypeDAO.findModlePrototypeList(modleIdList);
            List<Modle> modleList = new ArrayList<>();
            Modle modle = null;
            for(ModlePrototype modlePrototype:modlePrototypeList) {
                modle = new Modle();
                modle.setModleDescribe(modlePrototype.getModlePrototypeDescribe());
                modle.setModleName(modlePrototype.getModlePrototypeName());
                modle.setModleState("0");
                modle.setProjectId(project.getId());
                modleList.add(modle);
            }
            modleDAO.batchSave(modleList);

        }
    }

    @Override
    public void updateProject(Project project) {
        project.setOperateTime(DateUtil.getDateTime(new Date()));
        projectDao.update(project);
    }

    @Override
    public Project queryProject(Integer projectId) {
        return projectDao.find(projectId);
    }

    @Override
    public List<Project> queryAllProjectList() {
        return projectDao.findAllProjectList();
    }


    @Override
    public PageInfo<Project> findProjectListPage(Integer pageNo, Integer pageSize, String projectStatus, String projectName, String projectLeader) {
        List<Project> list = projectDao.findProjectListPage(pageNo,pageSize,projectStatus,projectName,projectLeader);
        return new PageInfo<>(list);
    }

    @Override
    public void deleteProject(Integer id) {
        projectDao.delete(id);
    }
}
