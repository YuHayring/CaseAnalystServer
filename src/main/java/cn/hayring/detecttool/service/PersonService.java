package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.PersonDao;
import cn.hayring.detecttool.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Value("${cn.hayring.detecttool.pagesize}")
    private int pageSize;


    private PersonDao personDao;

    public Person getPersonByName(Long caseId, String name) {
        return personDao.findByIdInCase(caseId, name);
    }


    public int updatePerson(Person person) {
        personDao.save(person);
        return 0;
    }

    public Page<Person> findAllPerson(Long id, int pageNo) {
        return personDao.findAllByCaseIdInPage(id, PageRequest.of(pageNo, pageSize));
    }

    public Person createNewPerson(Long caseId) {
        Long id = personDao.createPerson(caseId);
        Person person = new Person();
        person.setId(id);
        return person;
    }



    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

}
