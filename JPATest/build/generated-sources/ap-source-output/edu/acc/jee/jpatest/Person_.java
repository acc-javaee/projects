package edu.acc.jee.jpatest;

import edu.acc.jee.jpatest.Family;
import edu.acc.jee.jpatest.Job;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-15T11:30:35")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> firstName;
    public static volatile SingularAttribute<Person, String> lastName;
    public static volatile SingularAttribute<Person, Integer> id;
    public static volatile SingularAttribute<Person, Family> family;
    public static volatile ListAttribute<Person, Job> jobList;

}