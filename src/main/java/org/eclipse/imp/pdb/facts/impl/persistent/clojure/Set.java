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

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentSet;
import clojure.lang.PersistentHashSet;

import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureHelper.*;

class Set extends Value implements ISet {

	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	protected final static Type voidType = typeFactory.voidType();	
	
	protected final Type et;
	protected final IPersistentSet xs;	

	protected Set(Type et, IPersistentSet xs) {
		super(TypeFactory.getInstance().setType(et));
		this.et = et;
		this.xs = xs;
	}
	
	// for inheritence
	protected Set(Type t, Type et, IPersistentSet xs) {
		super(t);
		this.et = et;
		this.xs = xs;
	}	
		
	protected Set(Type et) {
		this(et, PersistentHashSet.EMPTY);
	}

	protected Set(Type et, IValue... values) {
		this(et, PersistentHashSet.create((Object[])values));
	}	
	
	protected Set(IValue... values) {
		this(lub(values), PersistentHashSet.create((Object[])values));
	}
	
	protected static Type lub(IValue... xs) {
		return lub(xs, TypeFactory.getInstance().voidType());
	}

	protected static Type lub(IValue[] xs, Type base) {
		Type result = base;

		for (IValue x : xs) {
			result = result.lub(x.getType());			
		}
		
		return result;
	}

	protected Type lub(IValue x) {
		return et.lub(x.getType());
	}

	protected Type lub(ISet xs) {
		return et.lub(xs.getElementType());
	}		
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> iterator() {
		return ((Iterable<IValue>) xs).iterator();
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitSet(this);
	}

	@Override
	public Type getElementType() {
		return getType().getElementType();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return xs.count();
	}

	@Override
	public boolean contains(IValue x) {
		return xs.contains(x);
	}

	@Override
	public <ISetOrRel extends ISet> ISetOrRel insert(IValue x) {
		return SetOrRel.apply(lub(x), (IPersistentSet) xs.cons(x));
	}

	@Override
	public <ISetOrRel extends ISet> ISetOrRel union(ISet other) {
		Set that = (Set) other;
		return SetOrRel.apply(this.lub(that), set$union(this.xs, that.xs));
	}
	
	@Override
	public <ISetOrRel extends ISet> ISetOrRel intersect(ISet other) {
		Set that = (Set) other;
		return SetOrRel.apply(this.lub(that), set$intersection(this.xs, that.xs));
	}

	@Override
	public <ISetOrRel extends ISet> ISetOrRel subtract(ISet other) {
		Set that = (Set) other;
		return SetOrRel.apply(et, set$difference(this.xs, that.xs));
	}

	@Override
	public <ISetOrRel extends ISet> ISetOrRel delete(IValue x) {
		return SetOrRel.apply(et, xs.disjoin(x));
	}

	@Override
	public IRelation product(ISet other) {
		Set that = (Set) other;
		
		Type resultType = TypeFactory.getInstance().tupleType(this.et, that.et);
		IRelationWriter result = ValueFactory.getInstance().relationWriter(resultType);
		
		for (IValue x : this) {
			for (IValue y : that) {
				result.insert(new Tuple(x, y));
			}
		}

		return result.done();
	}

	@Override
	public boolean isSubsetOf(ISet other) {
		Set that = (Set) other;
		return set$isSubset(this.xs, that.xs); 
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Set) {
			Set that = (Set) other;
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
