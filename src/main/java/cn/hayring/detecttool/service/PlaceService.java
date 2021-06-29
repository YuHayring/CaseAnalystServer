package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.PlaceDao;
import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.domain.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    private PlaceDao placeDao;

    @Value("${cn.hayring.detecttool.pagesize}")
    private int pageSize;

    public Page<Place> findAllPlace(Long id, int pageNo) {
        return placeDao.findAllByCaseIdInPage(id, PageRequest.of(pageNo, pageSize));
    }


    public int updatePlace(Place place) {
        placeDao.save(place);
        return 0;
    }

    public Place createNewPlace(Long caseId) {
        Long id = placeDao.createPlace(caseId);
        Place place = new Place();
        place.setId(id);
        return place;
    }

    @Autowired
    public void setPlaceDao(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }
}