package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PlaceDao extends Neo4jRepository<Place, Long> {

    @Query(value = "match (c:Case)-[]->(p:Place) where id(c) = {0} return p",
            countQuery=  "match (c:Case)-[]->(p:Place) where id(c) = {0} return count(p)")
    Page<Place> findAllByCaseIdInPage(Long id, Pageable pageable);


    @Query("match (c:Case) where id (c) = {0} create (c)-[:INCLUDE]->(p:Place) return id(p)")
    Long createPlace(Long caseId);

}
