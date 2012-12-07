package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class ListWriter implements IListWriter {

	@Override
	public void insertAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(IValue... value) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertAt(int index, IValue... value)
			throws FactTypeUseException, IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(IValue[] elems, int start, int length)
			throws FactTypeUseException, IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertAt(int index, IValue[] elems, int start, int length)
			throws FactTypeUseException, IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void replaceAt(int index, IValue elem) throws FactTypeUseException,
			IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void append(IValue... value) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(IValue elem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public IList done() {
		// TODO Auto-generated method stub
		return null;
	}

}
