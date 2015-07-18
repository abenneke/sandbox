package test.unidirectional;

import test.BaseParentEntity;
import test.BaseParentOwnedTest;

public class UnidirectionalParentOwnedTest extends BaseParentOwnedTest {

	@Override
	protected BaseParentEntity createParentEntity(String id) {
		UnidirectionalParent parent = new UnidirectionalParent();
		parent.setId(id);
		return parent;
	}

}
