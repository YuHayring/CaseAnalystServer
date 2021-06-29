package cn.hayring.detecttool.dao;

import cn.hayring.detecttool.domain.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDao extends Neo4jRepository<Node, Long> {

    @Query("MATCH (e2)<-[]-(c)-[]->(e1), (e1)-[r]->(e2) WHERE id(c) = {0} " +
            " return id(r) as id, id(e1) as start, id(e2) as end, r.type as type ")
    List<Relationship> getAllRelationshipInCase(Long caseId);

    @Query("MATCH (c)-[]->(e) WHERE id(c) = {0} return count(e)")
    Long countAllNodeInCase(Long caseId);

    @Query("MATCH (c)-[]->(e) WHERE id(c) = {0} return e")
    List<Node> getAllNodeInCase(Long caseId);

    @Query("MATCH (c)-[]->(p:Person) WHERE id(c) = {0} return p")
    List<Person> getAllPersonInCase(Long caseId);

    @Query("MATCH (c)-[]->(e:Event) WHERE id(c) = {0} return e")
    List<Event> getAllEventInCase(Long caseId);

    @Query("MATCH (c)-[]->(t:Thing) WHERE id(c) = {0} return t")
    List<Thing> getAllThingInCase(Long caseId);

    @Query("MATCH (c)-[]->(p:Place) WHERE id(c) = {0} return p")
    List<Place> getAllPlaceInCase(Long caseId);




    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2) where id(c) = {0} " +
            "and id(e2) = {1} return id(r) as id, id(e1) as start, r.type as type ")
    List<Relationship> nodeAsEnd(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2) where id(c) = {0} " +
            "and id(e1) = {1} return id(r) as id, id(e2) as end, r.type as type ")
    List<Relationship> nodeAsStart(Long caseId, Long id);







    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2:Person) where id(c) = {0} " +
            "and id(e1) = {1} return e2 ")
    List<Node> neighbourAsPersonEnd(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1:Person)-[r]->(e2) where id(c) = {0} " +
            "and id(e2) = {1} return e1")
    List<Node> neighbourAsPersonStart(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2:Event) where id(c) = {0} " +
            "and id(e1) = {1} return e2 ")
    List<Node> neighbourAsEventEnd(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1:Event)-[r]->(e2) where id(c) = {0} " +
            "and id(e2) = {1} return e1")
    List<Node> neighbourAsEventStart(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2:Thing) where id(c) = {0} " +
            "and id(e1) = {1} return e2 ")
    List<Node> neighbourAsThingEnd(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1:Thing)-[r]->(e2) where id(c) = {0} " +
            "and id(e2) = {1} return e1")
    List<Node> neighbourAsThingStart(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2:Place) where id(c) = {0} " +
            "and id(e1) = {1} return e2 ")
    List<Node> neighbourAsPlaceEnd(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1:Place)-[r]->(e2) where id(c) = {0} " +
            "and id(e2) = {1} return e1")
    List<Node> neighbourAsPlaceStart(Long caseId, Long id);



//    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), where id(c) = {2} and id(e1) = {0} and id (e2) = {1} " +
//            " return id(r) as id, id(e1) as start, id(e2) as end,  r.type as type ")
//    void updateRelationshipBetween(Long start, Long end, Long caseId, );


    @Query("MATCH (c)-[r]->(e) where id(c) = {0} and id(e) = {1} return e")
    Node selectById(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2) where id(c) = {0} and id(e1) = {1} " +
            " return count(r)")
    Long countRelationshipsInCaseAsStart(Long caseId, Long id);

    @Query("MATCH (e2)<-[r2]-(c)-[r1]->(e1), (e1)-[r]->(e2) where id(c) = {0} and id(e2) = {1} " +
            " return count(r)")
    Long countRelationshipsInCaseAsEnd(Long caseId, Long id);

    @Query("MATCH (e1)-[r]->(e2) where id(e1) = {0} return count(r)")
    Long countRelationshipsInCaseAsStart(Long id);

    @Query("MATCH (e1)-[r]->(e2) where id(e2) = {0} return count(r)")
    Long countRelationshipsInCaseAsEnd(Long id);


    @Query("MATCH (c)-[r:INCLUDE]->(e) where id(c) = {0} and id(e) = {1} delete r")
    void removeInCase(Long caseId, Long id);



    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:P2P]->(e2) return id(r)")
    Long createP2PBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:P2T]->(e2) return id(r)")
    Long createP2TBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:HAPPENED_IN]->(e2) return id(r)")
    Long createHappenedInBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:TAKE_PART]->(e2) return id(r)")
    Long createTakePartBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:CAUSE]->(e2) return id(r)")
    Long createCauseBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:APPEAR_AT]->(e2) return id(r)")
    Long createAppearAtBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:P2PLACE]->(e2) return id(r)")
    Long createP2PlaceBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:NEAR]->(e2) return id(r)")
    Long createNearBetween(Long start, Long end);

    @Query("MATCH (e1),(e2) where id(e1) = {0} and id(e2) = {1} create (e1)-[r:USE]->(e2) return id(r)")
    Long createUseBetween(Long start, Long end);

    @Query("MATCH ()-[r]->() where id(r) = {0} delete r")
    void deleteRelationship(Long id);


    @Query("MATCH (e1)-[r]->(e2) where id(e1) = {0} and id (e2) = {1} " +
            " return id(r) as id, id(e1) as start, id(e2) as end,  r.type as type , r.info as info, r.weight as weight")
    Relationship relationshipBetween(Long start, Long end);

    @Query("MATCH (e1)-[r]->(e2) where id(r) = {0} " +
            " return id(r) as id, id(e1) as start, id(e2) as end,  r.type as type , r.info as info, r.weight as weight")
    Relationship selectRelationshipById(Long id);


    @Query("MATCH ()-[r]->() where id(r) = {0} set r.type = {1} , r.info = {2} , r.weight = {3}")
    void updateRelationship(Long id, String type, String info, Integer weight);

    @Query("MATCH (p) where id(p) = {0} and {1} in labels(p) return count(p)")
    int nodeIsLabel(Long id, String label);

}
