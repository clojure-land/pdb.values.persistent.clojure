package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class MapWriter implements IMapWriter {

	@Override
	public void insert(IValue... value) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(IValue key, IValue value) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putAll(IMap map) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putAll(Map<IValue, IValue> map) throws FactTypeUseException {
		// TODO Auto-generated method stub

	}

	@Override
	public IMap done() {
		// TODO Auto-generated method stub
		return null;
	}

}
