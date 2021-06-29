package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Investigator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mybatis Mapper 接口
 */
@Mapper
public interface InvestigatorMapperDao {
    /**
     * 根据 id 查询密码信息
     * @param id 账户
     * @return
     */
    Investigator selectById(@Param("id") String id);

    /**
     * 根据 id 查询其他信息
     * @param id 账户
     * @return
     */
    Investigator selectByIdNormal(@Param("id") String id);
}
