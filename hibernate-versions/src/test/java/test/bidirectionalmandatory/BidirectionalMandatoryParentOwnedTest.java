package test.bidirectionalmandatory;

import test.BaseParentEntity;
import test.BaseParentOwnedTest;

public class BidirectionalMandatoryParentOwnedTest extends BaseParentOwnedTest {

	@Override
	protected BaseParentEntity createParentEntity(String id) {
		BidirectionalMandatoryParent parent = new BidirectionalMandatoryParent();
		parent.setId(id);
		return parent;
	}

}
