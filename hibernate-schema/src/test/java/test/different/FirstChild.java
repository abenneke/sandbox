package test.different;

import javax.persistence.Entity;
import javax.persistence.Table;

import test.BaseEntity;

@Entity
@Table(schema="FIRST")
public class FirstChild extends BaseEntity {

}
