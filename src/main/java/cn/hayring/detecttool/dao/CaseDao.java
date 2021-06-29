package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Case;
import cn.hayring.detecttool.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CaseDao extends Neo4jRepository<Case, Long> {



    @Query(value = "CREATE (c:Case{name:{0}}) return id(c)")
    Long createCaseByName(String name);
}
