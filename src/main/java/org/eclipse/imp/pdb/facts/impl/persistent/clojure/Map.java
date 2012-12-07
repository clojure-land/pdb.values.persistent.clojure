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
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class Map implements IMap {

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
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IMap put(IValue key, IValue value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue get(IValue key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsKey(IValue key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(IValue value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type getKeyType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getValueType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMap join(IMap other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMap remove(IMap other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMap compose(IMap other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMap common(IMap other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSubMap(IMap other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<IValue> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<IValue> valueIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Entry<IValue, IValue>> entryIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
