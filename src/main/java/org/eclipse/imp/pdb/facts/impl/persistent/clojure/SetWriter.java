package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentSet;
import clojure.lang.PersistentHashSet;

class SetWriter implements ISetWriter {

	protected final Type et;
	protected IPersistentSet xs;
	
	protected SetWriter(Type et){
		super();
		this.et = et;
		this.xs = PersistentHashSet.EMPTY;
	}
	
	@Override
	public void insert(IValue... values) throws FactTypeUseException {
		for (IValue x : values) {
			xs = (IPersistentSet) xs.cons(x);
		}
	}

	@Override
	public void insertAll(Iterable<? extends IValue> values)
			throws FactTypeUseException {
		for (IValue x : values) {
			xs = (IPersistentSet) xs.cons(x);
		}
	}

	@Override
	public void delete(IValue x) {
		xs = xs.disjoin(x);
	}

	@Override
	public ISet done() {
		return SetOrRel.apply(List.lub(xs.seq()), xs);
	}

	@Override
	public int size() {
		return xs.count();
	}

}

class SetWriterWithTypeInference extends SetWriter {

	protected SetWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}

	@Override
	public ISet done() {
		return SetOrRel.apply(List.lub(xs.seq()), xs);
	}
	  
}