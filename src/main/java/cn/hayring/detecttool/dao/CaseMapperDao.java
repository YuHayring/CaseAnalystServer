package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Case;
import cn.hayring.detecttool.domain.Investigator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaseMapperDao {
    /**
     * 根据 id 查询案件
     * @param  id 案件 id
     * @return 案件信息
     */
    Case selectById(@Param("id") String id);

    /**
     * 分页查询案件列表
     * @return 案件列表a
     */
    List<Case> selectCases();


    /**
     * 插入案件
     * @param c
     */
    void insertCase(Case c);

    /**
     * 查询案件名是否存在
     * @param name
     * @return
     */
    Integer countCaseName(String name);
}
