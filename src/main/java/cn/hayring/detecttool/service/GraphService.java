package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.NodeDao;
import cn.hayring.detecttool.domain.*;
import cn.hayring.detecttool.domain.resthttpresponse.GraphResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 图服务
 */
@Service
public class GraphService {


    private NodeDao nodeDao;

    @Value("${cn.hayring.detecttool.graph.maxnodecount}")
    private int maxNodeCountOfSimpleGraph;


    public GraphResponse getGraph(Long caseId) {
        Long count = nodeDao.countAllNodeInCase(caseId);
        if (count > maxNodeCountOfSimpleGraph) return null;
        List<List> nodes = new ArrayList<>();
        List<Person> persons = nodeDao.getAllPersonInCase(caseId);
        nodes.add(persons);
        List<Event> events = nodeDao.getAllEventInCase(caseId);
        nodes.add(events);
        List<Thing> things = nodeDao.getAllThingInCase(caseId);
        nodes.add(things);
        List<Place> places = nodeDao.getAllPlaceInCase(caseId);
        nodes.add(places);

        List<Relationship> edge = nodeDao.getAllRelationshipInCase(caseId);
        return new GraphResponse(nodes, edge);
    }


    @Transactional
    public int removeNodeInCase(Long caseId, Long id) {
        long countInCase = nodeDao.countRelationshipsInCaseAsEnd(caseId, id);
        countInCase += nodeDao.countRelationshipsInCaseAsStart(caseId, id);

        long count = nodeDao.countRelationshipsInCaseAsEnd(id);
        count += nodeDao.countRelationshipsInCaseAsStart(id);
        if (count == 0l) {
            nodeDao.deleteById(id);
            return 0;
        } else if (count > 0l && countInCase == 0l) {
            nodeDao.removeInCase(caseId, id);
            return 0;
        } else return -1;
    }

    public List<List> getRelationshipsByNodeId(Long caseId, Long id) {
        List<List> result = new ArrayList<>();
        result.add(nodeDao.nodeAsEnd(caseId, id));
        result.add(nodeDao.nodeAsStart(caseId, id));
        return result;
    }

    /**
     * 获取邻接结点
     * @param caseId
     * @param id
     * @return neighbours
     */
    public List<Set> getNeighboursByNodeId(Long caseId, Long id) {
        List<Set> result = new ArrayList<>();
        Set<Node> persons = new HashSet<>();
        persons.addAll(nodeDao.neighbourAsPersonEnd(caseId,id));
        persons.addAll(nodeDao.neighbourAsPersonStart(caseId,id));
        result.add(persons);
        Set<Node> events = new HashSet<>();
        events.addAll(nodeDao.neighbourAsEventEnd(caseId,id));
        events.addAll(nodeDao.neighbourAsEventStart(caseId,id));
        result.add(events);
        Set<Node> things = new HashSet<>();
        things.addAll(nodeDao.neighbourAsThingEnd(caseId,id));
        things.addAll(nodeDao.neighbourAsThingStart(caseId,id));
        result.add(things);
        Set<Node> places = new HashSet<>();
        places.addAll(nodeDao.neighbourAsPlaceEnd(caseId,id));
        places.addAll(nodeDao.neighbourAsPlaceStart(caseId,id));
        result.add(places);
        return result;
    }

    /**
     * 获取指定类型的邻接结点
     * @param caseId
     * @param id
     * @return neighbours
     */
    public List<Node> getNeighboursByNodeIdWithType(Long caseId, Long id, String type) {
        List<Node> result = new ArrayList<>();
        switch (type) {
            case Node.PERSON : {
                result.addAll(nodeDao.neighbourAsPersonStart(caseId, id));
                result.addAll(nodeDao.neighbourAsPersonEnd(caseId, id));
            }break;
            case Node.EVENT : {
                result.addAll(nodeDao.neighbourAsEventStart(caseId, id));
                result.addAll(nodeDao.neighbourAsEventEnd(caseId, id));
            }break;
            case Node.THING : {
                result.addAll(nodeDao.neighbourAsThingStart(caseId, id));
                result.addAll(nodeDao.neighbourAsThingEnd(caseId, id));
            }break;
            case Node.PLACE : {
                result.addAll(nodeDao.neighbourAsPlaceStart(caseId, id));
                result.addAll(nodeDao.neighbourAsPlaceEnd(caseId, id));
            }break;
        }
        return result;
    }


    /**
     * 根据 id 获取结点
     * @param caseId
     * @param id
     * @return
     */
    public Node getNode(Long caseId, Long id) {
        return nodeDao.selectById(caseId, id);
    }

    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }


}
