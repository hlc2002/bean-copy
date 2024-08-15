package com.runjing.runjingbeancopy;

import com.runjing.runjingbeancopy.orika.service.BeanCopyService;
import com.runjing.runjingbeancopy.orika.test.Address;
import com.runjing.runjingbeancopy.orika.test.Person;
import com.runjing.runjingbeancopy.orika.test.PersonBo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RunjingBeanCopyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private BeanCopyService beanCopyService;
    @Test
    public void copy(){
        Person person = new Person();
        person.setAge(1);
        person.setName("hlc");
        Address address = new Address();
        address.setAddress("河南省信阳市");
        address.setAddressCode(100);
        person.setAddress(address);
        person.setFood("西瓜");
        PersonBo personBo = beanCopyService.copy(person, PersonBo.class);
        System.out.println( personBo.getAddressInfo() == person.getAddress());
        person.getAddress().setAddressCode(99);
        person.setAge(12);
        System.out.println(person.toString());
        System.out.println(personBo.toString());
    }

}
