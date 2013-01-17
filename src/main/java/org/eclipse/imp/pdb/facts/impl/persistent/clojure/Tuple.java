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

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentVector;
import clojure.lang.ITransientVector;
import clojure.lang.PersistentVector;

public class Tuple extends Value implements ITuple {

	protected final IPersistentVector xs;	

	protected Tuple(Type type, IPersistentVector xs) {
		super(type);
		this.xs = xs;
		if (!type.isTupleType())
			throw new RuntimeException();
	}
	
	protected Tuple(Type type, IValue... values) {
		super(type);
		xs = PersistentVector.create((Object[])values);
		if (!type.isTupleType())
			throw new RuntimeException();
	}	
	
	protected Tuple(IValue... values) {
		this(TypeFactory.getInstance().tupleType(values), values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> iterator() {
		return ((Iterable<IValue>) xs).iterator();
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitTuple(this);
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		return (IValue) xs.nth(i);
	}

	@Override
	public IValue get(String l) throws FactTypeUseException {
		return get(getType().getFieldIndex(l));
	}

	@Override
	public ITuple set(int i, IValue x) throws IndexOutOfBoundsException {
		return new Tuple(getType(), xs.assocN(i, x));
	}

	@Override
	public ITuple set(String l, IValue x) throws FactTypeUseException {
		return set(getType().getFieldIndex(l), x);
	}

	@Override
	public int arity() {
		return xs.length();
	}

	@Override
	public IValue select(int... fields) throws IndexOutOfBoundsException {
		Type resultType = getType().select(fields);
		
		if (resultType.isTupleType()) {
	    	ITransientVector resultVector = PersistentVector.EMPTY.asTransient();
	    	for (int i : fields) {
	    		resultVector = (ITransientVector) resultVector.conj(xs.nth(i));
	    	}
	    	
	    	return new Tuple(resultType, (IPersistentVector) resultVector.persistent());
	    } else {
	    	return get(fields[0]);
	    } 
	}
	
	@Override
	public IValue selectByFieldNames(String... fields)
			throws FactTypeUseException {
		int[] indices = new int[fields.length];
		
		int current = 0;
		for (String l : fields) {
			indices[current] = getType().getFieldIndex(l);
			current = current + 1;
		}
		
		return select(indices);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Tuple) {
			Tuple that = (Tuple) other;
			return this.xs.equiv(that.xs);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return xs.hashCode();
	}

}
