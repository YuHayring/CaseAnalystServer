package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.domain.Thing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingDao extends Neo4jRepository<Thing, Long> {

    /**
     * 根据name获取学生 in 友谊关系
     * @param name
     * @return
     */
    @Query("MATCH (p:Person)-[r] -> (t:Thing) where p.name = {0} return t")
    List<Thing> findByOwnerName(String name);


    @Query(value = "match (c:Case)-[]->(t:Thing) where id(c) = {0} return t",
            countQuery=  "match (c:Case)-[]->(t:Thing) where id(c) = {0} return count(t)")
    Page<Thing> findAllByCaseIdInPage(Long id, Pageable pageable);

    @Query("match (c:Case) where id (c) = {0} create (c)-[:INCLUDE]->(t:Thing) return id(t)")
    Long createThing(Long caseId);
}
