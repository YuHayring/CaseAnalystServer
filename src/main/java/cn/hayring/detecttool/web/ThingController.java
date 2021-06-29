package cn.hayring.detecttool.web;

import cn.hayring.detecttool.domain.Place;
import cn.hayring.detecttool.domain.Thing;
import cn.hayring.detecttool.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/thing")
public class ThingController {
    private ThingService thingService;


    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ResponseEntity getThingsByOwner(@PathVariable String name) {
        List<Thing> things = thingService.getThings(name);
        if (things.size() > 0) return ResponseEntity.ok(things);
        else return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateThing(@PathVariable Long id, Thing thing) {
        if (!id.equals(thing.getId())) return ResponseEntity.badRequest().build();
        thingService.updateThing(thing);
        return ResponseEntity.noContent().build();
    }


    @Autowired
    public void setThingService(ThingService thingService) {
        this.thingService = thingService;
    }
}
