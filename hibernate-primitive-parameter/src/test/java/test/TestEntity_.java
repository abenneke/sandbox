package test;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TestEntity.class)
public class TestEntity_ {

	public static volatile SingularAttribute<TestEntity, String> id;
	public static volatile SingularAttribute<TestEntity, Boolean> objectBoolean;
	public static volatile SingularAttribute<TestEntity, Boolean> primitiveBoolean;
	public static volatile SingularAttribute<TestEntity, Integer> integerObject;
	public static volatile SingularAttribute<TestEntity, Integer> primitiveInt;

}
