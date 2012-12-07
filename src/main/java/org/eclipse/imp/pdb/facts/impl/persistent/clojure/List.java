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
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentList;
import clojure.lang.PersistentList;

public class List extends Value implements IList {

	private final Type et;
	private final IPersistentList xs;
	
	protected List(Type et) {
		this(et, PersistentList.EMPTY);
	}
	
	protected List(Type et, IPersistentList xs) {
		super(TypeFactory.getInstance().listType(et));
		this.et = et;
		this.xs = xs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> iterator() {
		return ((PersistentList) xs).iterator();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IList append(IValue e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IList insert(IValue x) {
		return new List(et, (IPersistentList) xs.cons(x));
	}

	@Override
	public IList concat(IList o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IList put(int i, IValue e) throws FactTypeUseException,
			IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IList sublist(int offset, int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		return length() != 0;
	}

	@Override
	public boolean contains(IValue e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IList delete(IValue e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IList delete(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
