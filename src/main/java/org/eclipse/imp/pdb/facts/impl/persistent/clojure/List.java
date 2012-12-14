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

import java.io.StringReader;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IFn;
import clojure.lang.ISeq;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentHashSet;
import clojure.lang.PersistentList;
import clojure.lang.RT;
import clojure.lang.Compiler;
import clojure.lang.Symbol;
import clojure.lang.Var;

import static clojure.lang.RT.CLOJURE_NS;
import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureCoreHelper.*;

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
	
	private final Type et;
	private final ISeq xs;
	
	protected List(Type et) {
		this(et, PersistentList.EMPTY);
	}
	
	protected List(Type et, ISeq xs) {
		super(TypeFactory.getInstance().listType(et));
		this.et = et;
		this.xs = xs;
	}

	private Type lub(IValue e) {
		return et.lub(e.getType());
	}

	private Type lub(IList e) {
		return et.lub(e.getElementType());
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
	public IList reverse() {
		return new List(et, (ISeq) core$reverse.invoke(xs));
	}

	@Override
	public IList append(IValue x) {
		return new List(this.lub(x), appendAtSeq(xs, x));
	}

	@Override
	public IList insert(IValue x) {
		return new List(this.lub(x), (ISeq) xs.cons(x));
	}

	@Override
	public IList concat(IList other) {
		List that = (List) other;
		return new List(this.lub(that), (ISeq) core$concat.invoke(this.xs, that.xs));
	}

	@Override
	public IList put(int i, IValue x) throws FactTypeUseException,
			IndexOutOfBoundsException {
		/**
		 * Implementation requires (= contract) that i is a valid index. Note, that the
		 * index check doesn't exploit the full potential of lazy sequences. 
		 */
		if (i < 0 || i >= length()) throw new IndexOutOfBoundsException();
		return new List(this.lub(x), replaceInSeq(xs, i, x));
	}
	
	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		return (IValue) core$nth.invoke(xs, i);
	}

	@Override
	public IList sublist(int i, int n) {
		if (i < 0 || n < 0 || i + n > length()) throw new IndexOutOfBoundsException();
		return new List(et, (ISeq) core$take.invoke(n, core$drop.invoke(i, xs)));
	}

	@Override
	public boolean isEmpty() {
		return length() != 0;
	}

	@Override
	public boolean contains(IValue x) {
		/**
		 * @see http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/some
		 */
		return !(Boolean) core$nil_QMARK_.invoke(core$some.invoke(PersistentHashSet.create(x), xs));
	}

	@Override
	public IList delete(IValue x) {
		return new List(et, deleteFromSeq(xs, x));
	}

	@Override
	public IList delete(int i) {
		if (i < 0 || i >= xs.count()) throw new IndexOutOfBoundsException();
		return new List(et, deleteFromSeq(xs, i));
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
	
	protected static ISeq appendAtSeq(ISeq xs, IValue x) {
		return (ISeq) core$reverse.invoke(core$conj.invoke(core$reverse.invoke(xs), x));		
	}
	
	protected static ISeq deleteFromSeq(ISeq xs, IValue x) {
	    Object result = pdblist$firstIndexOf.invoke(x, xs);	    
	    return (result == null) ? xs : deleteFromSeq(xs, ((Long)result).intValue());		
	}	
	
	protected static ISeq deleteFromSeq(ISeq xs, int i) {
		return (ISeq) core$concat.invoke(core$take.invoke(i, xs), core$rest.invoke(core$nthnext.invoke(xs, i)));
	}
	
	protected static ISeq replaceInSeq(ISeq xs, int i, IValue x) {
		IPersistentVector leftRight = (IPersistentVector) core$split_at.invoke(i, xs);
		ISeq newLeft = (ISeq) leftRight.nth(0);	
		ISeq newRight = (ISeq) core$cons.invoke(x, core$next.invoke(leftRight.nth(1)));
		return (ISeq) core$concat.invoke(newLeft, newRight);
	}
	
}
