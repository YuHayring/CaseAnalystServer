package cn.hayring.detecttool.web.graph;

import cn.hayring.detecttool.domain.*;
import cn.hayring.detecttool.domain.resthttpresponse.GraphResponse;
import cn.hayring.detecttool.domain.resthttpresponse.NodeResponse;
import cn.hayring.detecttool.domain.resthttpresponse.SimpleGraphResponse;
import cn.hayring.detecttool.service.*;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/case")
public class CaseController {
    private GraphService graphService;
    private PersonService personService;
    private EventService eventService;
    private PlaceService placeService;
    private ThingService thingService;
    private CaseService caseService;
    private RelationshipService relationshipService;



//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity getPerson(@PathVariable Long id) {
//        SimpleGraphResponse response = graphService.getGraphByCaseId(id);
//        if (response == null) return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
//        else return ResponseEntity.ok(response);
//    }

//    @RequestMapping(value = "/{id}/persons", method = RequestMethod.GET)
//    public ResponseEntity getPerson(@PathVariable Long id) {
//
//    }


    /**
     * 根据人名获取人的信息
     * @param caseId 案件id
     * @param name 名字
     * @return
     */
    @RequestMapping(value = "/{caseId}/person/{name}", method = RequestMethod.GET)
    public ResponseEntity getPerson(@PathVariable Long caseId, @PathVariable String name) {
        Person p = personService.getPersonByName(caseId, name);
        if (p == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(p);
    }

    /**
     * 获取人物列表
     * @param id 案件 id
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/{id}/persons", method = RequestMethod.GET)
    public ResponseEntity getPersons(@PathVariable Long id, @RequestParam(required = false) Integer page) {
        if (page == null) page = 0;
        Page<Person> result = personService.findAllPerson(id, page);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取事件列表
     * @param id 案件 id
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/{id}/events", method = RequestMethod.GET)
    public ResponseEntity getEvents(@PathVariable Long id, @RequestParam(required = false) Integer page) {
        if (page == null) page = 0;
        Page<Event> result = eventService.findAllEvent(id, page);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取地点列表
     * @param id 案件 id
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/{id}/places", method = RequestMethod.GET)
    public ResponseEntity getPlaces(@PathVariable Long id, @RequestParam(required = false) Integer page) {
        if (page == null) page = 0;
        Page<Place> result = placeService.findAllPlace(id, page);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取物品列表
     * @param id 案件 id
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/{id}/things", method = RequestMethod.GET)
    public ResponseEntity getThings(@PathVariable Long id, @RequestParam(required = false) Integer page) {
        if (page == null) page = 0;
        Page<Thing> result = thingService.findAllThing(id, page);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }


    /**
     * 获取结点的所有关系
     * @param id 结点id
     * @param caseId 案件 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/node/{id}/relationships", method = RequestMethod.GET)
    public ResponseEntity getRelationships(@PathVariable Long caseId, @PathVariable Long id) {
        List<List> result = graphService.getRelationshipsByNodeId(caseId, id);
        if (result.get(0).size() == 0 && result.get(1).size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);

    }

    /**
     * 获取结点的所有邻接结点
     * @param id 结点id
     * @param caseId 案件 id
     * @return neightbours
     */
    @RequestMapping(value = "/{caseId}/node/{id}/neighbours", method = RequestMethod.GET)
    public ResponseEntity getNeighbours(@PathVariable Long caseId, @PathVariable Long id) {
        List<Set> result = graphService.getNeighboursByNodeId(caseId, id);
        boolean empty = true;
        for (Set l : result) {
            empty = (l.size() == 0) && empty;
        }
        if (empty) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取结点的指定类型的邻接结点
     * @param id 结点id
     * @param caseId 案件 id
     * @return neightbours
     */
    @RequestMapping(value = "/{caseId}/node/{id}/neighbours/{type}", method = RequestMethod.GET)
    public ResponseEntity getNeighbours(@PathVariable Long caseId, @PathVariable Long id, @PathVariable String type) {
        //首字母大写
        char[] cs=type.toCharArray();
        cs[0]-=32;
        type = String.valueOf(cs);

        List<Node> result = graphService.getNeighboursByNodeIdWithType(caseId, id, type);
        if (result.size() == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取案件列表
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/cases", method = RequestMethod.GET)
    public ResponseEntity getCases(@RequestParam(required = false) Integer page) {
        if (page == null) page = 0;
        PageInfo data = caseService.getCaseByPage(page);
        if (data.getList().size() == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(data);
    }


    /**
     * 根据名字创建案件
     * @param name 案件名
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createCaseByName(@RequestParam String name) {
        int result = caseService.createCase(name);
        if (result == 0) return ResponseEntity.noContent().build();
        else  return ResponseEntity.status(403).build();
    }

    /**
     * 获取结点
     * @param caseId 案件 ID
     * @param id 结点 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/node/{id}",method = RequestMethod.GET)
    public ResponseEntity getNode(@PathVariable("caseId") Long caseId, @PathVariable("id") Long id) {
        Node node = graphService.getNode(caseId, id);
        if (node == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new NodeResponse(node));
    }

    /**
     * 创建人物
     * @param caseId 案件 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/person",method = RequestMethod.POST)
    public ResponseEntity createPerson(@PathVariable("caseId") Long caseId) {
        Person person = personService.createNewPerson(caseId);
        return ResponseEntity.ok(person);
    }

    /**
     * 创建地点
     * @param caseId 案件 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/place",method = RequestMethod.POST)
    public ResponseEntity createPlace(@PathVariable("caseId") Long caseId) {
        Place place = placeService.createNewPlace(caseId);
        return ResponseEntity.ok(place);
    }

    /**
     * 创建事件
     * @param caseId 案件 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/event",method = RequestMethod.POST)
    public ResponseEntity createEvent(@PathVariable("caseId") Long caseId) {
        Event event = eventService.createNewEvent(caseId);
        return ResponseEntity.ok(event);
    }

    /**
     * 创建物品
     * @param caseId 案件 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/thing",method = RequestMethod.POST)
    public ResponseEntity createThing(@PathVariable("caseId") Long caseId) {
        Thing thing = thingService.createNewThing(caseId);
        return ResponseEntity.ok(thing);
    }


    /**
     * 删除结点
     * @param caseId 案件 id
     * @param id 结点 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/node/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteNode(@PathVariable("caseId") Long caseId, @PathVariable("id") Long id) {
        int result = graphService.removeNodeInCase(caseId, id);
        if (result == -1) return ResponseEntity.status(403).build();
        else if (result == 0) return ResponseEntity.noContent().build();
        throw new IllegalStateException();

    }


    /**
     * 创建关系
     * @param start 起点
     * @param end 指向结点
     * @param label 标签
     * @return
     */
    @RequestMapping(value = "/{caseId}/node/{start}/relationship",method = RequestMethod.POST)
    public ResponseEntity createRelationship(@PathVariable("start") Long start, @RequestParam Long end, @RequestParam String label) {
        Relationship relationship = relationshipService.createRelationship(start, end, label);
        if (relationship == null) return ResponseEntity.badRequest().build();
        else return ResponseEntity.ok(relationship);
    }

    /**
     * 获取关系
     * @param start 起点
     * @param end 指向结点
     * @return
     */
    @RequestMapping(value = "/{caseId}/node/{start}/relationship",method = RequestMethod.GET)
    public ResponseEntity getRelationshipBetween(@PathVariable("start") Long start, @RequestParam Long end) {
        Relationship relationship = relationshipService.getRelationshipBetween(start, end);
        if (relationship == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(relationship);
    }

    /**
     * 根据 id获取关系
     * @param id 关系 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/relationship/{id}",method = RequestMethod.GET)
    public ResponseEntity getRelationshipById(@PathVariable("id") Long id) {
        Relationship relationship = relationshipService.getRelationshipById(id);
        if (relationship == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(relationship);
    }

    /**
     * 根据关系 id 删除
     * @param id 关系 id
     * @return
     */
    @RequestMapping(value = "/{caseId}/relationship/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteRelationshipById(@PathVariable("id") Long id) {
        relationshipService.deleteRelationship(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据关系 id 更新
     * @param id
     * @return
     */
    @RequestMapping(value = "/{caseId}/relationship/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateRelationshipById(@PathVariable("id") Long id, Relationship relationship) {
        relationshipService.updateRelationship(relationship);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据案件ID查询整个图
     * @param caseId 案件 id
     * @return GraphResponse
     */
    @RequestMapping(value = "/{caseId}", method = RequestMethod.GET)
    public ResponseEntity getGraph(@PathVariable("caseId") Long caseId) {
        GraphResponse response = graphService.getGraph(caseId);
        if (response == null) return ResponseEntity.status(403).build();
        else return ResponseEntity.ok(response);
    }


    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setPlaceService(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Autowired
    public void setThingService(ThingService thingService) {
        this.thingService = thingService;
    }

    @Autowired
    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setRelationshipService(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }
}
