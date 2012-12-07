package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class RelationWriter implements IRelationWriter {

	@Override
	public void insert(IValue... v) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(IValue v) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IRelation done() {
		// TODO Auto-generated method stub
		return null;
	}

}
