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
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class List implements IList {

	@Override
	public Iterator<IValue> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type getElementType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
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
	public IList insert(IValue e) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
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
