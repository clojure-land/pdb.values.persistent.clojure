/*******************************************************************************
 * Copyright (c) 2012 CWI
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

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.ISeq;
import clojure.lang.IPersistentVector;
import clojure.lang.ITransientVector;
import clojure.lang.PersistentList;
import clojure.lang.PersistentVector;
import clojure.lang.RT;

class Vector extends Value implements IList {
	
	protected final Type et;
	protected final IPersistentVector xs;
	
	protected Vector(Type et) {
		this(et, PersistentVector.EMPTY);
	}
	
	protected Vector(IValue... values) {
		this(TypeInferenceHelper.lubFromVoid(values), PersistentVector.create((Object[])values));
	}

	protected Vector(Type et, IPersistentVector xs) {
		this(TypeFactory.getInstance().listType(et), et, xs);
	}
	
	protected Vector(Type t, Type et, IPersistentVector xs) {
		super(t);
		this.et = et;
		this.xs = xs;
	}	
	
	static protected ISeq seq(IValue... values) {
		ISeq result = PersistentList.EMPTY;
		
		for(int i = values.length-1; i >= 0; i--) {
			result = result.cons(values[i]);
		}
		
		return result;
	}
	
	private Type lub(IValue x) {
		return et.lub(x.getType());
	}

	private Type lub(IList xs) {
		return et.lub(xs.getElementType());
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> iterator() {
		return ((Iterable<IValue>) xs).iterator();
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitList(this);
	}

	@Override
	public Type getElementType() {
		return et;
	}

	@Override
	public int length() {
		return xs.count();
	}

	@Override
	public <IListOrRel extends IList> IListOrRel reverse() {
		return VectorOrRel.apply(et, PersistentVector.create(xs.rseq()));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel append(IValue newItem) {
		ITransientVector result = PersistentVector.EMPTY.asTransient();
		for(Object item : (Iterable) xs)
			result = (ITransientVector) result.conj(item);
		result = (ITransientVector) result.conj(newItem);
		
		return VectorOrRel.apply(this.lub(newItem), (IPersistentVector) result.persistent());
	}

	@Override
	public <IListOrRel extends IList> IListOrRel insert(IValue newItem) {
		ITransientVector result = PersistentVector.EMPTY.asTransient();
		result = (ITransientVector) result.conj(newItem);
		for(Object item : (Iterable) xs)
			result = (ITransientVector) result.conj(item);
		
		return VectorOrRel.apply(this.lub(newItem), (IPersistentVector) result.persistent());
	}

	@Override
	public <IListOrRel extends IList> IListOrRel concat(IList other) {
		Vector that = (Vector) other;
		
		ITransientVector result = PersistentVector.EMPTY.asTransient();
		
		for(Object item : (Iterable) this.xs)
			result = (ITransientVector) result.conj(item);

		for(Object item : (Iterable) that.xs)
			result = (ITransientVector) result.conj(item);
		
		return VectorOrRel.apply(this.lub(that), (IPersistentVector) result.persistent());
	}

	@Override
	public <IListOrRel extends IList> IListOrRel put(int i, IValue x) throws FactTypeUseException,
			IndexOutOfBoundsException {
		/**
		 * Implementation requires (= contract) that i is a valid index. Note, that the
		 * index check doesn't exploit the full potential of lazy sequences. 
		 */
		if (i < 0 || i >= length()) throw new IndexOutOfBoundsException();
		return VectorOrRel.apply(this.lub(x), xs.assocN(i, x));
	}
	
	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {	
		return (IValue) xs.nth(i);
	}

	@Override
	public <IListOrRel extends IList> IListOrRel sublist(int i, int n) {
		if (i < 0 || n < 0 || i + n > length()) throw new IndexOutOfBoundsException();
		return VectorOrRel.apply(et, RT.subvec(xs, i, i+n));
	}

	@Override
	public boolean isEmpty() {
		return length() == 0;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean contains(IValue x) {
		return ((java.util.Collection) xs).contains(x);
	}

	@Override
	public <IListOrRel extends IList> IListOrRel delete(IValue x) {
		ITransientVector result = PersistentVector.EMPTY.asTransient();
		
		boolean skipped = false;
		for(Object item : (Iterable) xs) {
			if (!skipped && item.equals(x)) {
				skipped = true;
			} else {
				result = (ITransientVector) result.conj(item);
			}
		}
				
		return VectorOrRel.apply(et, (IPersistentVector) result.persistent());
	}

	@Override
	public <IListOrRel extends IList> IListOrRel delete(int i) {
		if (i < 0 || i >= xs.count()) throw new IndexOutOfBoundsException();
		ITransientVector result = PersistentVector.EMPTY.asTransient();
		
		boolean skipped = false;
		int idx = 0;
		for (Iterator it = ((Iterable) xs).iterator(); it.hasNext(); idx++) {
			Object item = it.next();
			
			if (!skipped && i == idx) {
				skipped = true;
			} else {
				result = (ITransientVector) result.conj(item);
			}
		}		
		
		return VectorOrRel.apply(et, (IPersistentVector) result.persistent());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector) {
			Vector that = (Vector) other;
			return this.xs.equiv(that.xs);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return xs.hashCode();
	}
	
	@Override
	public IListRelation product(IList other) {
		// NOTE: copied from fast list
		Type resultType = TypeFactory.getInstance().tupleType(getElementType(), other.getElementType());
		IListRelationWriter w = ValueFactory.getInstance().listRelationWriter(resultType);

		for(IValue t1 : this){
			for(IValue t2 : other){
				IValue vals[] = {t1, t2};
				ITuple t3 = new Tuple(resultType, vals);
				w.insert(t3);
			}
		}

		return (IListRelation) w.done();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <IListOrRel extends IList> IListOrRel intersect(IList other) {
		// NOTE: copied from fast list
		IListWriter w = ValueFactory.getInstance().listWriter(other.getElementType().lub(getElementType()));
		for (IValue v: this) {
			if (other.contains(v)) {
				other = other.delete(v);
			} else
				w.append(v);
		}
		return (IListOrRel) w.done();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <IListOrRel extends IList> IListOrRel subtract(IList other) {
		// NOTE: copied from fast list
		IListWriter w = ValueFactory.getInstance().listWriter(other.getElementType().lub(getElementType()));
		for (IValue v: this) {
			if (other.contains(v)) {
				other = other.delete(v);
			} else
				w.append(v);
		}
		return (IListOrRel) w.done();
	}

	@Override
	public boolean isSubListOf(IList other) {
		// NOTE: copied from fast list
		int j = 0;
		nextchar:
			for(IValue elm : this){
				while(j < other.length()){
					if(elm.isEqual(other.get(j))){
						j++;
						continue nextchar;
					} else
						j++;
				}
				return false;
			}
		return true;
	}
	
}
