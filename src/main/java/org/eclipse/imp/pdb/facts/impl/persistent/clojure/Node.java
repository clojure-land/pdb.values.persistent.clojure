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
import java.util.Map;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentMap;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentHashMap;
import clojure.lang.PersistentVector;

import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureHelper.*;

public class Node extends Value implements INode {

	protected final String name;
	protected final IPersistentVector children;
	protected final IPersistentMap annotations;
	protected int hash = 0;
	
	protected Node(Type type, String name, IPersistentVector children, IPersistentMap annotations) {
		super(type);
		this.name = name;
		this.children = children;
		this.annotations = annotations;
	}

	protected Node(String name) {
		this(TypeFactory.getInstance().nodeType(), name, PersistentVector.EMPTY, PersistentHashMap.EMPTY);
	}
	
	protected Node(String name, IValue... children) {
		this(TypeFactory.getInstance().nodeType(), name, PersistentVector.create((Object[])children), PersistentHashMap.EMPTY);
	}	

	protected Node(String name, java.util.Map<String, IValue> newAnnotationsMap, IValue... children) {
		this(TypeFactory.getInstance().nodeType(), name, PersistentVector.create((Object[])children), PersistentHashMap.create(newAnnotationsMap));
	}
	
	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitNode(this);
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		return (IValue) children.nth(i);
	}

	@Override
	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		return new Node(type, name,  children.assocN(i, newChild), annotations);
	}

	@Override
	public int arity() {
		return children.length();
	}

	@Override
	public String getName() {
		return name;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterable<IValue> getChildren() {
		return (Iterable) children;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator<IValue> iterator() {
		return ((Iterable) children).iterator();
	}

	@Override
	public IValue getAnnotation(String label) throws FactTypeUseException {
		return (IValue) annotations.valAt(label, null);
	}
		
	@Override
	public INode setAnnotation(String label, IValue newValue)
			throws FactTypeUseException {
		return new Node(type, name, children, annotations.assoc(label, newValue));
	}

	@Override
	public boolean hasAnnotation(String label) throws FactTypeUseException {
		return annotations.containsKey(label);
	}

	@Override
	public boolean hasAnnotations() {
		return !(annotations.count() == 0); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, IValue> getAnnotations() {
		return (Map<String, IValue>) annotations;
	}

	@Override
	public INode setAnnotations(Map<String, IValue> newAnnotations) {
		return new Node(type, name, children, PersistentHashMap.create(newAnnotations));
	}

	@Override
	public INode joinAnnotations(Map<String, IValue> newAnnotationsMap) {
		IPersistentMap newAnnotations = core$merge(annotations, PersistentHashMap.create(newAnnotationsMap));
		return new Node(type, name, children, newAnnotations);
	}

	@Override
	public INode removeAnnotation(String key) {
		return new Node(type, name, children, annotations.without(key));
	}

	@Override
	public INode removeAnnotations() {
		return new Node(type, name, children, (IPersistentMap) annotations.empty());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Node) {
			Node that = (Node) other;
			return this.name.equals(that.name) 
					&& this.children.equiv(that.children);
//					&& this.annotations.equiv(that.annotations);
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	protected int computeHashCode() {
		int hash = name != null ? name.hashCode() : 0;

		for (Object child : (Iterable) children) {
			hash = (hash << 1) ^ (hash >> 1) ^ child.hashCode();
		}
		return hash;
	}

	@Override
	public int hashCode() {
		if (hash == 0) {
			hash = computeHashCode();
		}
		return hash;
	}
	
}
