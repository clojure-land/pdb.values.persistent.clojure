/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
 *******************************************************************************/
package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;

public class VectorRelation extends Vector implements IListRelation {
	
	protected VectorRelation(Type et, IPersistentVector xs) {
		super(TypeFactory.getInstance().lrelTypeFromTuple(et), et, xs);
	}

	protected VectorRelation(Type et) {
		this(et, PersistentVector.EMPTY);
	}

	protected VectorRelation(Type et, IValue... values) {
		this(et, PersistentVector.create((Object[])values));
	}	
	
	protected VectorRelation(IValue... values) {
		this(Set.lub(values), values); // TODO: move lub somewhere else
	}	
	
//	@Override
//	public Type getType() {
//		return TypeFactory.getInstance().lrelTypeFromTuple(et);
//	}	
//	
//	@Override
//	public Type getElementType() {
//		return et;
//	}	
	
	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitListRelation(this);
	}

	@Override
	public int arity() {
		return getType().getArity();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IListRelation compose(IListRelation other) throws FactTypeUseException {
		// NOTE: from reference
		Type resultType = getType().compose(other.getType());
		// an exception will have been thrown if the relations are not both binary and
		// have a comparable field to compose.
		IListRelationWriter w = ValueFactory.getInstance().listRelationWriter(resultType.getFieldTypes());

		for (IValue v1 : (Iterable<IValue>) xs) {
			ITuple tuple1 = (ITuple) v1;
			for (IValue t2 : other) {
				ITuple tuple2 = (ITuple) t2;
				
				if (tuple1.get(1).isEqual(tuple2.get(0))) {
					w.insert(new Tuple(tuple1.get(0), tuple2.get(1)));
				}
			}
		}
		return w.done();
	}

	@Override
	public IListRelation closure() throws FactTypeUseException {
		// NOTE: from reference
		getType().closure(); // will throw exception if not binary and reflexive
		IListRelation tmp = this;

		int prevCount = 0;

		while (prevCount != tmp.length()) { // NOTE: uses length instead of size
			prevCount = tmp.length(); // NOTE: uses length instead of size
			tmp = (IListRelation) tmp.concat(tmp.compose(tmp)); // NOTE: uses concat instead of union
		}

		return tmp;
	}

	@Override
	public IListRelation closureStar() throws FactTypeUseException {
		// NOTE: from reference
		Type resultType = getType().closure();
		// an exception will have been thrown if the type is not acceptable

		IListWriter reflex = ValueFactory.getInstance().listRelationWriter(resultType.getElementType());

		for (IValue e: carrier()) {
			reflex.insert(new Tuple(new IValue[] {e, e}));
		}
		
		return closure().concat(reflex.done()); // NOTE: uses concat instead of union
	}

	@Override
	public IList carrier() {
		// NOTE: from reference
		Type newType = getType().carrier();
		IListWriter w = ValueFactory.getInstance().listWriter(newType.getElementType());
		
		for (IValue t : this) {
			w.insertAll((ITuple) t);
		}
		
		return w.done();
	}

	@Override
	public Type getFieldTypes() {
		return getType().getFieldTypes();
	}

	@Override
	public IList domain() {
		// NOTE: from reference
		Type relType = getType();
		IListWriter w = ValueFactory.getInstance().listWriter(relType.getFieldType(0));
		
		for (IValue elem : this) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(0));
		}
		return w.done();
	}
	
	public IList range() {
		// NOTE: from reference
		Type relType = getType();
		int last = relType.getArity() - 1;
		IListWriter w = ValueFactory.getInstance().listWriter(relType.getFieldType(last));
		
		for (IValue elem : this) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(last));
		}
		
		return w.done();
	}	
				
	public IList select(int... fields) {
		// NOTE: from reference
		Type eltType = getFieldTypes().select(fields);
		IListWriter w = ValueFactory.getInstance().listWriter(eltType);
		
		for (IValue v : this) {
			w.insert(((ITuple) v).select(fields));
		}
		
		return w.done();
	}

	@Override
	public IList selectByFieldNames(String... fields)
			throws FactTypeUseException {
		// NOTE: from reference
		int[] indexes = new int[fields.length];
		int i = 0;
		
		if (getFieldTypes().hasFieldNames()) {
			for (String field : fields) {
				indexes[i++] = getFieldTypes().getFieldIndex(field);
			}
			
			return select(indexes);
		}
		
		throw new IllegalOperationException("select with field names", getType());
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof VectorRelation) {
			VectorRelation that = (VectorRelation) other;
			return this.xs.equals(that.xs);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return xs.hashCode();
	}
	
}
