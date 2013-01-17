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
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.APersistentMap;
import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;

import static org.eclipse.imp.pdb.facts.impl.persistent.clojure.ClojureHelper.*;

public class Map extends Value implements IMap {

	protected final Type kt, vt;
	protected final IPersistentMap xs;		
	
	protected Map(Type kt, Type vt, IPersistentMap xs) {
		super(TypeFactory.getInstance().mapType(kt, vt));
		this.kt = kt;
		this.vt = vt;
		this.xs = xs;
	}
	
	protected Map(Type kt, Type vt) {
		this(kt, vt, PersistentHashMap.EMPTY);
	}	
	
	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitMap(this);
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
	public IMap put(IValue key, IValue value) {
		return new Map(kt.lub(key.getType()), vt.lub(value.getType()), (IPersistentMap) xs.assoc(key, value));
	}

	@Override
	public IValue get(IValue key) {
		return (IValue) xs.valAt(key, null);
	}

	@Override
	public boolean containsKey(IValue key) {
		return xs.containsKey(key);
	}

	@Override
	public boolean containsValue(IValue value) {
		Iterator<IValue> it = valueIterator();
		
		while (it.hasNext()) {
			if (it.next().equals(value)) return true;
		}
		
		return false;
	}

	@Override
	public Type getKeyType() {
		return kt;
	}

	@Override
	public Type getValueType() {
		return vt;
	}

	@Override
	public IMap join(IMap other) {
		Map that = (Map) other;
		return new Map(kt.lub(that.kt), vt.lub(that.vt), core$merge(this.xs, that.xs));
	}

	@Override
	public IMap remove(IMap other) {
		IPersistentMap result = xs;
		
		for (IValue key: other) {
			result = result.without(key);
		}

		return new Map(kt, vt, result);
	}

	@Override
	public IMap compose(IMap other) {
		Map that = (Map) other;
		IMapWriter writer = ValueFactory.getInstance().mapWriter(this.kt, that.vt);
		
		for (IValue key : this) {
			if (that.containsKey(key)) writer.put(key, that.get(key));
		}
		
		return writer.done();
	}

	@Override
	public IMap common(IMap other) {
		Map that = (Map) other;
		IMapWriter writer = ValueFactory.getInstance().mapWriter(this.kt, that.vt);
		
		for (IValue key : this) {
			if (that.containsKey(key) && this.get(key).equals(that.get(key))) {
				writer.put(key, this.get(key));
			}
		}
		
		return writer.done();
	}

	@Override
	public boolean isSubMap(IMap other) {
		Map that = (Map) other;
		
		for (IValue key: this) {
			if (that.containsKey(key) == false) return false;
			if (that.get(key).isEqual(this.get(key)) == false) return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> iterator() {
		return ((APersistentMap) xs).keySet().iterator();
//		ISeq keys = core$keys(xs);
//		if (keys == null) 
//			return ((Iterable<IValue>) PersistentList.EMPTY).iterator();
//		else
//			return ((Iterable<IValue>) keys).iterator();			
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IValue> valueIterator() {
		return ((APersistentMap) xs).values().iterator();
//		ISeq vals = core$vals(xs);
//		if (vals == null) 
//			return ((Iterable<IValue>) PersistentList.EMPTY).iterator();
//		else
//			return ((Iterable<IValue>) vals).iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Entry<IValue, IValue>> entryIterator() {
		return ((APersistentMap) xs).entrySet().iterator();
//		return new Iterator<java.util.Map.Entry<IValue,IValue>>() {
//			
//			@SuppressWarnings("unchecked")
//			final Iterator<MapEntry> innerIterator = xs.iterator(); 
//			
//			@Override
//			public void remove() {
//				innerIterator.remove();
//			}
//			
//			@Override
//			public Entry<IValue, IValue> next() {
//				return new Entry<IValue, IValue>() {
//
//					final MapEntry innerEntry = innerIterator.next();
//					
//					@Override
//					public IValue getKey() {
//						return (IValue) innerEntry.getKey();
//					}
//
//					@Override
//					public IValue getValue() {
//						return (IValue) innerEntry.getValue();
//					}
//
//					@Override
//					public IValue setValue(IValue value) {
//						throw new UnsupportedOperationException();
//					}
//				};
//			}
//			
//			@Override
//			public boolean hasNext() {
//				return innerIterator.hasNext();
//			}
//		};
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Map) {
			Map that = (Map) other;
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
