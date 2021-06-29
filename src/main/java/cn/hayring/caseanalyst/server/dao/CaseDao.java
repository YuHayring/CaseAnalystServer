package cn.hayring.caseanalyst.server.dao;

import cn.hayring.caseanalyst.server.domain.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CaseDao extends Neo4jRepository<Case, Long> {



    @Query(value = "CREATE (c:Case{name:{0}}) return id(c)")
    Long createCaseByName(String name);
}
