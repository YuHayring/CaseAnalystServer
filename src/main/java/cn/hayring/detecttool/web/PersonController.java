package cn.hayring.detecttool.web;

import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.domain.Thing;
import cn.hayring.detecttool.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private PersonService personService;



    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updatePerson(@PathVariable Long id, Person person) {
        if (!id.equals(person.getId())) return ResponseEntity.badRequest().build();
        personService.updatePerson(person);
        return ResponseEntity.noContent().build();
    }







    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
