package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EventDao extends Neo4jRepository<Event, Long> {


    @Query(value = "match (c:Case)-[]->(e:Event) where id(c) = {0} return e",
            countQuery=  "match (c:Case)-[]->(e:Event) where id(c) = {0} return count(e)")
    Page<Event> findAllByCaseIdInPage(Long id, Pageable pageable);


    @Query("match (c:Case) where id (c) = {0} create (c)-[:INCLUDE]->(e:Event) return id(e)")
    Long createEvent(Long caseId);
}
