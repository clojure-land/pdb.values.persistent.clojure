package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureCoreHelper.*;

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
	public void insertAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		insertSeq(RT.seq(collection));
	}

	@Override
	public void insert(IValue... values) throws FactTypeUseException {
		insertSeq(RT.seq(values));
	}

	private void insertSeq(ISeq ys) {
		xs = (ISeq) core$concat.invoke(ys, xs);
	}

	@Override
	public void insertAt(int i, IValue... values)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, RT.seq(values));
	}

	@Override
	public void insert(IValue[] values, int i, int n)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, (ISeq) core$take.invoke(n, RT.seq(values)));
	}

	@Override
	public void insertAt(int i, IValue[] values, int j, int n)
			throws FactTypeUseException, IndexOutOfBoundsException {
		insertAt(i, (ISeq) core$take.invoke(n, core$drop.invoke(j, RT.seq(values))));
	}
	
	private void insertAt(int i, ISeq ys) {
		xs = (ISeq) core$concat.invoke(core$concat.invoke(core$take.invoke(i, xs), ys), core$drop.invoke(i, xs));
	}	

	// TODO / NOTE: inconsistency in naming; equivalent to List.put(i, x)
	@Override
	public void replaceAt(int i, IValue x) throws FactTypeUseException,
			IndexOutOfBoundsException {
		xs = List.replaceInSeq(xs, i, x);
	}

	@Override
	public void append(IValue... values) throws FactTypeUseException {
		xs = (ISeq) core$concat.invoke(xs, RT.seq(values));
	}

	@Override
	public void appendAll(Iterable<? extends IValue> collection)
			throws FactTypeUseException {
		xs = (ISeq) core$concat.invoke(xs, RT.seq(collection));
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
		return new List(et, xs);
	}

}

class ListWriterWithTypeInference extends ListWriter {

	protected ListWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}

	// TODO: Move to a (more) proper place.
	private Type lub(ISeq xs) {
		Type result = et;

		while (xs.more() != null) {
			result = result.lub(((IValue) xs.first()).getType());
			xs = xs.next();
		}

		return result;
	}

	@Override
	public IList done() {
		return new List(lub(xs), xs);
	}
	  
}