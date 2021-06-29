package cn.hayring.detecttool;

import cn.hayring.detecttool.dao.NodeDao;
import cn.hayring.detecttool.dao.PersonDao;
import cn.hayring.detecttool.dao.ThingDao;
import cn.hayring.detecttool.domain.Node;
import cn.hayring.detecttool.domain.Relationship;
import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.domain.Thing;
import cn.hayring.detecttool.service.CaseService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Neo4jTest {


    @Autowired
    private PersonDao personDao;

    @Autowired
    private ThingDao thingDao;

    @Autowired
    private CaseService caseService;


    @Autowired
    private Gson gson;

    @Test
    public void personDaoTest() {
        Person p = personDao.findByIdInCase(Long.valueOf(62),"余海宁");
        System.out.println(p);


    }

    @Test
    public void thingDaoTest() {
        List<Thing> things = thingDao.findByOwnerName("余海宁");
        System.out.println(things.size());
        for (Thing thing : things) {
            System.out.println(thing.toString());
        }


    }

    @Test
    public void caseDaoTest() {
        List<Relationship> graph = personDao.findByIdInCase(Long.valueOf(62), Long.valueOf(41));
        for (Relationship relationship : graph) System.out.println(relationship);
    }


    @Test
    public void allPersonTest() {
        Page<Person> persons = personDao.findAllByCaseIdInPage(Long.valueOf(62), PageRequest.of(0, 10));
        Gson gson = new Gson();
        System.out.println(gson.toJson(persons));
    }


    @Test
    public void relationshipTest() {
        List<Relationship> start = nodeDao.nodeAsEnd(62L, 1L);
        List<Relationship> end = nodeDao.nodeAsStart(62L, 1L);
        for (Relationship r : start) {
            System.out.println(r);
        }
        for (Relationship r : end) {
            System.out.println(r);
        }
    }


    @Test
    public void createCaseTest() {
        String name = "案件1";
        int result = caseService.createCase(name);
        System.out.println(result);
    }



    @Autowired
    private NodeDao nodeDao;


    @Test
    public void nodeIdSelectTest() {
        Node node = nodeDao.selectById(62L, 2L);
        System.out.println(gson.toJson(node));
        node = nodeDao.selectById(62L, 20L);
        System.out.println(gson.toJson(node));
    }


    @Test
    public void saveTest() {
        Person person = personDao.findById(65l).get();
        person.setName(null);
        personDao.save(person);
        person = personDao.findById(65l).get();
        assert "案件1的人物test".equals(person.getName());
    }

//    @Test
//    public void relationshipQueryTest() {
//        long l = nodeDao.queryRelationship("INCLUDE");
//        System.out.println(l);
//    }

}
