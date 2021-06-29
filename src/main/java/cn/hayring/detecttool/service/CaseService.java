package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.CaseDao;
import cn.hayring.detecttool.dao.CaseMapperDao;
import cn.hayring.detecttool.domain.Case;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CaseService {

    /**
     * neo4j 的 dao
     */
    private CaseMapperDao caseMapperDao;

    /**
     * mysql 的 dao
     */
    private CaseDao caseDao;


    @Value("${cn.hayring.detecttool.pagesize}")
    private int pageSize;

    public Case getById(String id) {
        return caseMapperDao.selectById(id);
    }




    public List<Case> getCase(int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Case> list = caseMapperDao.selectCases();
        return list;
    }

    public PageInfo<Case> getCaseByPage(int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Case> lists = caseMapperDao.selectCases();
        PageInfo<Case> pageInfo = new PageInfo<>(lists);
        return pageInfo;

    }


    /**
     * 创建案件
     * @param name
     * @return
     */
    @Transactional
    public int createCase(String name) {
        //已经存在该名称
        if (caseMapperDao.countCaseName(name) > 0) return 1;
        //插入 neo4j ，获取其 id，再插入 mysql
        Long id = caseDao.createCaseByName(name);
        Case c = new Case();
        c.setId(id);
        c.setName(name);
        c.setDate(new Date());
        caseMapperDao.insertCase(c);
        return 0;
    }


    @Autowired
    public void setCaseDao(CaseDao caseDao) {
        this.caseDao = caseDao;
    }

    @Autowired
    public void setCaseMapperDao(CaseMapperDao caseMapperDao) {
        this.caseMapperDao = caseMapperDao;
    }

}
