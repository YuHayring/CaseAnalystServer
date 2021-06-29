package cn.hayring.detecttool.dao;


import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.domain.Relationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDao extends Neo4jRepository<Person, Long> {

    /**
     * 根据name获取学生 in 友谊关系
     * @param name
     * @return
     */
    @Query("match (c:Case)-[]->(p:Person) where id(c) = {0} p.name = {1} return p")
    Person findByIdInCase(Long id, String name);


    /**
     * 根据name获取学生 in 友谊关系
     * @param name
     * @return
     */
    @Query("match (c:Case)-[]->(p:Person) where id(c) = {0} return p")
    Person findByIdInCase(Long id);


    @Query(value = "match (c:Case)-[]->(p:Person) where id(c) = {0} return p",
            countQuery=  "match (c:Case)-[]->(p:Person) where id(c) = {0} return count(p)")
    Page<Person> findAllByCaseIdInPage(Long id, Pageable pageable);




    /**
     * 根据personId获取学生 in 友谊关系
     * @param personId
     * @return
     */
    @Query("match (p2:Person)<-[r2]-(c:Case)-[r1]->(p1:Person), (p1)-[r]->(p2)" +
            " where id(c) = {0} and id(p1) = {1} return id(r) as id, id(p2) as end, r.type as type")
    List<Relationship> findByIdInCase(Long caseId, Long personId);



    @Query("match (c:Case) where id (c) = {0} create (c)-[:INCLUDE]->(p:Person) return id(p)")
    Long createPerson(Long caseId);


}