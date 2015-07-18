package test.bidirectionaloptional;

import test.BaseParentEntity;
import test.BaseParentOwnedTest;

public class BidirectionalOptionalParentOwnedTest extends BaseParentOwnedTest {

	@Override
	protected BaseParentEntity createParentEntity(String id) {
		BidirectionalOptionalParent parent = new BidirectionalOptionalParent();
		parent.setId(id);
		return parent;
	}

}
