package cn.hayring.detecttool.web;

import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Place;
import cn.hayring.detecttool.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
public class PlaceController {

    private PlaceService placeService;


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updatePlace(@PathVariable Long id, Place place) {
        if (!id.equals(place.getId())) return ResponseEntity.badRequest().build();
        placeService.updatePlace(place);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    public void setPlaceService(PlaceService placeService) {
        this.placeService = placeService;
    }
}
