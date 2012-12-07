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
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class Relation implements IRelation {

	@Override
	public Type getElementType() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean contains(IValue element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRelation product(ISet set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSubsetOf(ISet other) {
		// TODO Auto-generated method stub
		return false;
	}

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
	public int arity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IRelation compose(IRelation rel) throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRelation closure() throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRelation closureStar() throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISet carrier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getFieldTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISet domain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISet range() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISet select(int... fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISet selectByFieldNames(String... fields)
			throws FactTypeUseException {
		// TODO Auto-generated method stub
		return null;
	}

}
