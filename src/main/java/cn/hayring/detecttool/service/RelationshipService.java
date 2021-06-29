package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.NodeDao;
import cn.hayring.detecttool.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationshipService {

    private static final String PERSON = Person.class.getSimpleName();
    private static final String THING = Thing.class.getSimpleName();
    private static final String PLACE = Place.class.getSimpleName();
    private static final String EVENT = Event.class.getSimpleName();




    private NodeDao nodeDao;

    public Relationship createRelationship(Long start, Long end, String label) {
        Long id = null;
        switch (label) {
            case Relationship.P2P : {
                if (nodeDao.nodeIsLabel(start, PERSON) == 1 &&
                        nodeDao.nodeIsLabel(end, PERSON) == 1)
                    id =  nodeDao.createP2PBetween(start, end);
                else return null;
            }break;
            case Relationship.P2T : {
                if (nodeDao.nodeIsLabel(start, PERSON) == 1 &&
                        nodeDao.nodeIsLabel(end, THING) == 1)
                    id =  nodeDao.createP2TBetween(start, end);
            }break;
            case Relationship.HAPPENED_IN : {
                if (nodeDao.nodeIsLabel(start, EVENT) == 1 &&
                        nodeDao.nodeIsLabel(end, PLACE) == 1)
                    id =  nodeDao.createHappenedInBetween(start, end);
                else return null;
            }break;
            case Relationship.TAKE_PART : {
                if (nodeDao.nodeIsLabel(start, EVENT) == 1 &&
                        nodeDao.nodeIsLabel(end, PERSON) == 1
                )
                    id =  nodeDao.createTakePartBetween(start, end);
                else return null;
            }break;
            case Relationship.CAUSE : {
                if (nodeDao.nodeIsLabel(start, EVENT) == 1 &&
                        nodeDao.nodeIsLabel(end, EVENT) == 1
                )
                    id =  nodeDao.createCauseBetween(start, end);
                else return null;
            }break;
            case Relationship.APPEAR_AT : {
                if (nodeDao.nodeIsLabel(start, THING) == 1 &&
                        nodeDao.nodeIsLabel(end, PLACE) == 1
                )
                    id =  nodeDao.createAppearAtBetween(start, end);
                else return null;
            }break;
            case Relationship.P2PLACE : {
                if (nodeDao.nodeIsLabel(start, PERSON) == 1 &&
                        nodeDao.nodeIsLabel(end, PLACE) == 1
                )
                    id =  nodeDao.createP2PlaceBetween(start, end);
                else return null;
            }break;
            case Relationship.NEAR : {
                if (nodeDao.nodeIsLabel(start, PLACE) == 1 &&
                        nodeDao.nodeIsLabel(end, PLACE) == 1
                )
                    id =  nodeDao.createNearBetween(start, end);
                else return null;
            }break;
            case Relationship.USE : {
                if (nodeDao.nodeIsLabel(start, EVENT) == 1 &&
                        nodeDao.nodeIsLabel(end, THING) == 1
                )
                    id =  nodeDao.createUseBetween(start, end);
                else return null;
            }break;
            default: throw new IllegalArgumentException("Illegal label:" + label);
        }
        if (id == null) throw new IllegalStateException("id == null");
        Relationship relationship = new Relationship();
        relationship.setId(id);
        relationship.setStart(start);
        relationship.setEnd(end);
        return relationship;

    }


    public int deleteRelationship(Long id) {
        nodeDao.deleteRelationship(id);
        return 0;
    }

    public Relationship getRelationshipBetween(Long start, Long end) {
        return nodeDao.relationshipBetween(start, end);
    }


    public Relationship getRelationshipById(Long id) {
        return nodeDao.selectRelationshipById(id);
    }

    public int updateRelationship(Relationship r) {
        nodeDao.updateRelationship(r.getId(), r.getType(), r.getInfo(), r.getWeight());
        return 0;
    }


    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
