package test.bidirectionaloptimisticlock;

import test.BaseParentEntity;
import test.BaseParentOwnedTest;

public class BidirectionalOptimisticLockParentOwnedTest extends
		BaseParentOwnedTest {

	@Override
	protected BaseParentEntity createParentEntity(String id) {
		BidirectionalOptimisticLockParent parent = new BidirectionalOptimisticLockParent();
		parent.setId(id);
		return parent;
	}

}
