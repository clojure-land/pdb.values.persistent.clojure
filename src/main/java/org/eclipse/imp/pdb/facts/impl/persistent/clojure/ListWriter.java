package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureHelper.*;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.ISeq;
import clojure.lang.PersistentList;
import clojure.lang.RT;

class ListWriter implements IListWriter {

	protected final Type et;
	protected ISeq xs;
	
	protected ListWriter(Type et){
		super();
		this.et = et;
		this.xs = PersistentList.EMPTY;
	}	
	
	@Override
	public int size() {
		return xs.count();
	}
	
	@Override
	public void insertAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		insertSeq(RT.seq(collection));
	}

	@Override
	public void insert(IValue... values) throws FactTypeUseException {
		for (int i = values.length-1; i >= 0; i--) {
			xs = xs.cons(values[i]);
		}
	}

	private void insertSeq(ISeq ys) {
		xs = core$concat(ys, xs);
	}

	@Override
	public void insertAt(int i, IValue... values)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, RT.seq(values));
	}

	@Override
	public void insert(IValue[] values, int i, int n)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, core$take(n, RT.seq(values)));
	}

	@Override
	public void insertAt(int i, IValue[] values, int j, int n)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, core$take(n, core$drop(j, RT.seq(values))));
	}
	
	private void insertAt(int i, ISeq ys) {
		xs = core$concat(core$concat(core$take(i, xs), ys), core$drop(i, xs));
	}	

	// TODO / NOTE: inconsistency in naming; equivalent to List.put(i, x)
	@Override
	public void replaceAt(int i, IValue x) throws FactTypeUseException,
			IndexOutOfBoundsException {
		xs = List.replaceInSeq(xs, i, x);
	}

	@Override
	public void append(IValue... values) throws FactTypeUseException {
		xs = core$concat(xs, RT.seq(values));
	}

	@Override
	public void appendAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		xs = core$concat(xs, RT.seq(collection));
	}

	@Override
	public void delete(IValue x) {
		xs = List.deleteFromSeq(xs, x);
	}

	@Override
	public void delete(int i) {
		xs = List.deleteFromSeq(xs, i);
	}

	@Override
	public IList done() {
		return ListOrRel.apply(et, xs);
	}

}

class ListWriterWithTypeInference extends ListWriter {

	protected ListWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}

	@Override
	public IList done() {
		return ListOrRel.apply(List.lub(xs), xs);
	}
	  
}