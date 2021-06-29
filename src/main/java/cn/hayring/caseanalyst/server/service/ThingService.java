package cn.hayring.caseanalyst.server.service;

import cn.hayring.caseanalyst.server.dao.ThingDao;
import cn.hayring.caseanalyst.server.domain.Thing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingService {

    private ThingDao thingDao;


    @Value("${cn.hayring.caseanalyst.server.pagesize}")
    private int pageSize;

    public List<Thing> getThings(String name) {
       return thingDao.findByOwnerName(name);
    }

    public Page<Thing> findAllThing(Long id, int pageNo) {
        return thingDao.findAllByCaseIdInPage(id, PageRequest.of(pageNo, pageSize));
    }


    public int updateThing(Thing thing) {
        thingDao.save(thing);
        return 0;
    }

    public Thing createNewThing(Long caseId) {
        Long id = thingDao.createThing(caseId);
        Thing thing = new Thing();
        thing.setId(id);
        return thing;
    }

    @Autowired
    public void setThingDao(ThingDao thingDao) {
        this.thingDao = thingDao;
    }
}
