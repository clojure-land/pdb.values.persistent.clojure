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
import java.util.Map;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class Node implements INode {

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
	public IValue get(int i) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int arity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<IValue> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<IValue> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue getAnnotation(String label) throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode setAnnotation(String label, IValue newValue)
			throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAnnotation(String label) throws FactTypeUseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnnotations() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, IValue> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode setAnnotations(Map<String, IValue> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode joinAnnotations(Map<String, IValue> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode removeAnnotation(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode removeAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

}
