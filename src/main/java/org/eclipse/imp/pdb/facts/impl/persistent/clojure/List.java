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

import java.io.StringReader;
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

import clojure.lang.IFn;
import clojure.lang.IPersistentCollection;
import clojure.lang.ISeq;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentHashSet;
import clojure.lang.PersistentList;
import clojure.lang.RT;
import clojure.lang.Compiler;
import clojure.lang.Symbol;
import clojure.lang.Var;

import static clojure.lang.RT.CLOJURE_NS;
import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureHelper.*;

class List extends Value implements IList {

	/**
	 * Use the Clojure compiler to create functions references code that
	 * is difficult to express purely in Java. 
	 */
	static {
		/**
		 * E.g. (first (keep-indexed #(if (= 15 %2) %1) [0 0 0 15 15])) returns 3.
		 */
	    String str = "(ns pdb-list) (defn firstIndexOf [x coll] (first (keep-indexed #(if (= x %2) %1) coll)))";
	    Compiler.load(new StringReader(str));

	    pdblist$firstIndexOf = RT.var("pdb-list", "firstIndexOf");		
	}
	
	/**
	 * Creates a reference to functions that are contained in "clojure.core".
	 * 
	 * @param functionName name of the function present in "clojure.core"
	 * @return reference to the function
	 */
	static IFn clojure$core_reference(String functionName) {
		return Var.intern(CLOJURE_NS, Symbol.intern(null, functionName));
	}
	
	private final static IFn pdblist$firstIndexOf;
	
	protected final Type et;
	protected final ISeq xs;
	
	protected List(Type et) {
		this(et, PersistentList.EMPTY);
	}
	
	protected List(IValue... values) {
		this(TypeInferenceHelper.lubFromVoid(values), seq(values));
	}
	
	static protected ISeq seq(IValue... values) {
		ISeq result = PersistentList.EMPTY;
		
		for(int i = values.length-1; i >= 0; i--) {
			result = result.cons(values[i]);
		}
		
		return result;
	}		
	
	protected List(Type et, ISeq xs) {
		this(TypeFactory.getInstance().listType(et), et, xs);
	}
	
	protected List(Type t, Type et, ISeq xs) {
		super(t);
		this.et = et;
		this.xs = xs;
		
		if (t == null || et == null || xs == null) throw new NullPointerException();
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
		return ListOrRel.apply(et, core$reverse(xs));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel append(IValue x) {
		return ListOrRel.apply(this.lub(x), appendAtSeq(xs, x));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel insert(IValue x) {
		return ListOrRel.apply(this.lub(x), (ISeq) xs.cons(x));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel concat(IList other) {
		List that = (List) other;
		return ListOrRel.apply(this.lub(that), core$concat(this.xs, that.xs));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel put(int i, IValue x) throws FactTypeUseException,
			IndexOutOfBoundsException {
		/**
		 * Implementation requires (= contract) that i is a valid index. Note, that the
		 * index check doesn't exploit the full potential of lazy sequences. 
		 */
		if (i < 0 || i >= length()) throw new IndexOutOfBoundsException();
		return ListOrRel.apply(this.lub(x), replaceInSeq(xs, i, x));
	}
	
	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= length()) throw new IndexOutOfBoundsException();
//		return (IValue) core$nth.invoke(xs, i);

		ISeq rest = xs;
		for (; i > 0; rest = rest.next(), i--);		
		return (IValue) rest.first();
	}

	@Override
	public <IListOrRel extends IList> IListOrRel sublist(int i, int n) {
		if (i < 0 || n < 0 || i + n > length()) throw new IndexOutOfBoundsException();
		return ListOrRel.apply(et, core$take(n, core$drop(i, xs)));
	}

	@Override
	public boolean isEmpty() {
		return length() == 0;
	}

	@Override
	public boolean contains(IValue x) {
		/**
		 * @see http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/some
		 */
		return !(null == core$some(PersistentHashSet.create(x), xs));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel delete(IValue x) {
		return ListOrRel.apply(et, deleteFromSeq(xs, x));
	}

	@Override
	public <IListOrRel extends IList> IListOrRel delete(int i) {
		if (i < 0 || i >= xs.count()) throw new IndexOutOfBoundsException();
		return ListOrRel.apply(et, deleteFromSeq(xs, i));
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof List) {
			List that = (List) other;
			return this.xs.equiv(that.xs);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return xs.hashCode();
	}


	/*
	 * Static Functions that operate on Clojure sequences.
	 * Are used herein and in ListWriter classes.
	 * 
	 * TODO: separate from this class.
	 */

	protected static Type lub(ISeq xs) {
		Type base = TypeFactory.getInstance().voidType();
		return xs == null ? base : lub(xs, base);
	}
	
	private static Type lub(ISeq xs, Type base) {
		Type result = base;
		
		while (xs != null && xs.first() != null) {
			result = result.lub(((IValue) xs.first()).getType());
			xs = xs.next();
		}

		return result;
	}		
	
	protected static ISeq appendAtSeq(ISeq xs, IValue x) {
		return core$reverse(core$conj(core$reverse(xs), x));		
	}
	
	protected static ISeq deleteFromSeq(ISeq xs, IValue x) {
	    Object result = pdblist$firstIndexOf.invoke(x, xs);	    
	    return (result == null) ? xs : deleteFromSeq(xs, ((Long)result).intValue());		
	}	
	
	protected static ISeq deleteFromSeq(ISeq xs, int i) {
		return core$concat(core$take(i, xs), core$rest(core$nthnext(xs, i)));
	}
	
	protected static ISeq replaceInSeq(ISeq xs, int i, IValue x) {
		IPersistentVector leftRight = core$splitAt(i, xs);
		ISeq newLeft = (ISeq) leftRight.nth(0);	
		ISeq newRight = (ISeq) core$cons.invoke(x, core$next((IPersistentCollection) leftRight.nth(1)));
		return core$concat(newLeft, newRight);
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
