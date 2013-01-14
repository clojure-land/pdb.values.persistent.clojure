package org.eclipse.imp.pdb.test.persistent.clojure;

import org.eclipse.imp.pdb.facts.impl.persistent.clojure.ValueFactory;
import org.eclipse.imp.pdb.test.BaseTestBasicValues;

public class TestBasicValues extends BaseTestBasicValues {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp(new ValueFactory());
	}
}
