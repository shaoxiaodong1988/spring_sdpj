package com.shendu.service.impl;

import com.shendu.dao.ModlePrototypeDAO;
import com.shendu.dao.ProjectDao;
import com.shendu.dao.ProjectInfoDAO;
import com.shendu.model.ModlePrototype;
import com.shendu.model.Project;
import com.shendu.model.ProjectInfo;
import com.shendu.service.ModlePrototypeService;
import com.shendu.service.ProjectService;
import com.shendu.utils.DateUtil;
import com.shendu.vo.ProjectInfoVO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by huangxinguang on 2017/5/11.
 */
@Service
public class ModlePrototypeServiceImpl implements ModlePrototypeService {

    @Autowired
    ModlePrototypeDAO modlePrototypeDAO;

    @Override
    public PageInfo<ModlePrototype> findModlePrototypeListPage(Integer pageNo, Integer pageSize, String modlePrototypeName) {
        List<ModlePrototype> list = modlePrototypeDAO.findModlePrototypeListPage(pageNo, pageSize, modlePrototypeName);
        return new PageInfo<>(list);
    }


    @Transactional
    public void saveModlePrototype(ModlePrototype modlePrototype) {
        modlePrototype.setOperateTime(DateUtil.getDateTime(new Date()));
        if(modlePrototype.getId() == null){
            modlePrototypeDAO.save(modlePrototype);
        }else{
            modlePrototypeDAO.update(modlePrototype);
        }
    }

    @Override
    public void delModlePrototype(Integer modelProId) {
        modlePrototypeDAO.delete(modelProId);
    }

    public ModlePrototype findModlePrototypeById(Integer id) {
        return modlePrototypeDAO.find(id);
    }

    @Override
    public List<ModlePrototype> findAllModlePrototypeList() {
        return modlePrototypeDAO.findAllModlePrototypeList();
    }

}
