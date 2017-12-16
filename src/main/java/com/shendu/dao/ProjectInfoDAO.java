package com.shendu.dao;

import com.shendu.base.BaseDAO;
import com.shendu.model.ProjectInfo;
import com.shendu.vo.ProjectInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
public interface ProjectInfoDAO extends BaseDAO<ProjectInfo>{

    List<ProjectInfoVO> findProjectInfoListPage(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("projectId") Integer projectId, @Param("projectName") String projectName);

}
