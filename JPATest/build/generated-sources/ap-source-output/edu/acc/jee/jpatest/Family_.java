package edu.acc.jee.jpatest;

import edu.acc.jee.jpatest.Person;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-15T11:30:35")
@StaticMetamodel(Family.class)
public class Family_ { 

    public static volatile ListAttribute<Family, Person> members;
    public static volatile SingularAttribute<Family, String> description;
    public static volatile SingularAttribute<Family, Integer> id;

}