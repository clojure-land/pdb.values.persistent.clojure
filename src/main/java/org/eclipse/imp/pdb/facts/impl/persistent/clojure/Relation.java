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

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.impl.util.collections.ShareableValuesHashSet;
import org.eclipse.imp.pdb.facts.impl.util.collections.ShareableValuesList;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.util.RotatingQueue;
import org.eclipse.imp.pdb.facts.util.ShareableHashMap;
import org.eclipse.imp.pdb.facts.util.ValueIndexedHashMap;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

import clojure.lang.IPersistentSet;
import clojure.lang.PersistentHashSet;

public class Relation extends Set implements IRelation {
	
	protected Relation(Type et, IPersistentSet xs) {
		super(TypeFactory.getInstance().relTypeFromTuple(et), et, xs);
	}

	protected Relation(Type et) {
		this(et, PersistentHashSet.EMPTY);
	}

	protected Relation(Type et, IValue... values) {
		this(et, PersistentHashSet.create((Object[])values));
	}	
	
	protected Relation(IValue... values) {
		this(lub(values), PersistentHashSet.create((Object[])values));
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitRelation(this);
	}

	@Override
	public Type getFieldTypes() {
		return getType().getFieldTypes();
	}	
	
	@Override
	public int arity() {
		return getType().getArity();
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public IRelation compose(IRelation other) throws FactTypeUseException {
//		// NOTE: from reference
//		Type resultType = getType().compose(other.getType());
//		// an exception will have been thrown if the relations are not both binary and
//		// have a comparable field to compose.
//		IRelationWriter w = factory.relationWriter(resultType.getFieldTypes());
//
//		for (IValue v1 : (Iterable<IValue>) xs) {
//			ITuple tuple1 = (ITuple) v1;
//			for (IValue t2 : other) {
//				ITuple tuple2 = (ITuple) t2;
//				
//				if (tuple1.get(1).isEqual(tuple2.get(0))) {
//					w.insert(new Tuple(tuple1.get(0), tuple2.get(1)));
//				}
//			}
//		}
//		return w.done();
//	}

	public IRelation compose(IRelation other){
		// NOTE: from fast
		Type otherTupleType = other.getFieldTypes();
		
		if(et == voidType) return this;
		if(otherTupleType == voidType) return other;
		
		if(et.getArity() != 2 || otherTupleType.getArity() != 2) throw new IllegalOperationException("compose", et, otherTupleType);
		if(!et.getFieldType(1).comparable(otherTupleType.getFieldType(0))) throw new IllegalOperationException("compose", et, otherTupleType);
		
		// Index
		ShareableHashMap<IValue, ShareableValuesList> rightSides = new ShareableHashMap<IValue, ShareableValuesList>();
		
		Iterator<IValue> otherRelationIterator = other.iterator();
		while(otherRelationIterator.hasNext()){
			ITuple tuple = (ITuple) otherRelationIterator.next();
			
			IValue key = tuple.get(0);
			ShareableValuesList values = rightSides.get(key);
			if(values == null){
				values = new ShareableValuesList();
				rightSides.put(key, values);
			}
			
			values.append(tuple.get(1));
		}
		
		// Compute
		ShareableValuesHashSet newData = new ShareableValuesHashSet();
		
		Type[] newTupleFieldTypes = new Type[]{et.getFieldType(0), otherTupleType.getFieldType(1)};
		Type tupleType = typeFactory.tupleType(newTupleFieldTypes);
		
		Iterator<IValue> relationIterator = iterator();
		while(relationIterator.hasNext()){
			ITuple thisTuple = (ITuple) relationIterator.next();
			
			IValue key = thisTuple.get(1);
			ShareableValuesList values = rightSides.get(key);
			if(values != null){
				Iterator<IValue> valuesIterator = values.iterator();
				do{
					IValue value = valuesIterator.next();
					IValue[] newTupleData = new IValue[]{thisTuple.get(0), value};
					newData.add(new Tuple(tupleType, newTupleData));
				}while(valuesIterator.hasNext());
			}
		}
		
		return new FastRelationWriter(tupleType, newData).done();
	}
	
//	@Override
//	public IRelation closure() throws FactTypeUseException {
//		// NOTE: from reference
//		getType().closure(); // will throw exception if not binary and reflexive
//		IRelation tmp = this;
//
//		int prevCount = 0;
//
//		while (prevCount != tmp.size()) {
//			prevCount = tmp.size();
//			tmp = (IRelation) tmp.union(tmp.compose(tmp));
//		}
//
//		return tmp;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ShareableValuesHashSet computeClosure(Type tupleType){
		ShareableValuesHashSet allData = new ShareableValuesHashSet();
		allData.addAll((java.util.Set) xs);
		
		RotatingQueue<IValue> iLeftKeys = new RotatingQueue<IValue>();
		RotatingQueue<RotatingQueue<IValue>> iLefts = new RotatingQueue<RotatingQueue<IValue>>();
		
		ValueIndexedHashMap<RotatingQueue<IValue>> interestingLeftSides = new ValueIndexedHashMap<RotatingQueue<IValue>>();
		ValueIndexedHashMap<ShareableValuesHashSet> potentialRightSides = new ValueIndexedHashMap<ShareableValuesHashSet>();
		
		// Index
		Iterator<IValue> allDataIterator = allData.iterator();
		while(allDataIterator.hasNext()){
			ITuple tuple = (ITuple) allDataIterator.next();

			IValue key = tuple.get(0);
			IValue value = tuple.get(1);
			RotatingQueue<IValue> leftValues = interestingLeftSides.get(key);
			ShareableValuesHashSet rightValues;
			if(leftValues != null){
				rightValues = potentialRightSides.get(key);
			}else{
				leftValues = new RotatingQueue<IValue>();
				iLeftKeys.put(key);
				iLefts.put(leftValues);
				interestingLeftSides.put(key, leftValues);
				
				rightValues = new ShareableValuesHashSet();
				potentialRightSides.put(key, rightValues);
			}
			leftValues.put(value);
			rightValues.add(value);
		}
		
		interestingLeftSides = null;
		
		int size = potentialRightSides.size();
		int nextSize = 0;
		
		// Compute
		do{
			ValueIndexedHashMap<ShareableValuesHashSet> rightSides = potentialRightSides;
			potentialRightSides = new ValueIndexedHashMap<ShareableValuesHashSet>();
			
			for(; size > 0; size--){
				IValue leftKey = iLeftKeys.get();
				RotatingQueue<IValue> leftValues = iLefts.get();
				
				RotatingQueue<IValue> interestingLeftValues = null;
				
				IValue rightKey;
				while((rightKey = leftValues.get()) != null){
					ShareableValuesHashSet rightValues = rightSides.get(rightKey);
					if(rightValues != null){
						Iterator<IValue> rightValuesIterator = rightValues.iterator();
						while(rightValuesIterator.hasNext()){
							IValue rightValue = rightValuesIterator.next();
							if(allData.add(new Tuple(tupleType, new IValue[]{leftKey, rightValue}))){
								if(interestingLeftValues == null){
									nextSize++;
									
									iLeftKeys.put(leftKey);
									interestingLeftValues = new RotatingQueue<IValue>();
									iLefts.put(interestingLeftValues);
								}
								interestingLeftValues.put(rightValue);
								
								ShareableValuesHashSet potentialRightValues = potentialRightSides.get(rightKey);
								if(potentialRightValues == null){
									potentialRightValues = new ShareableValuesHashSet();
									potentialRightSides.put(rightKey, potentialRightValues);
								}
								potentialRightValues.add(rightValue);
							}
						}
					}
				}
			}
			size = nextSize;
			nextSize = 0;
		}while(size > 0);
		
		return allData;
	}
	
	public IRelation closure(){
		if(et == voidType) return this;
		if(!isReflexive()) {
			throw new IllegalOperationException("closure", type);
		}
		
		Type tupleet = et.getFieldType(0).lub(et.getFieldType(1));
		Type tupleType = typeFactory.tupleType(tupleet, tupleet);
		
		return new FastRelationWriter(et, computeClosure(tupleType)).done();
	}	
	
	private boolean isReflexive(){
		if(et.getArity() != 2) throw new RuntimeException("Tuple is not binary");
		
		Type left = et.getFieldType(0);
		Type right = et.getFieldType(1);
			
		return right.comparable(left);
	}	
	
	@Override
	public IRelation closureStar() throws FactTypeUseException {
		// NOTE: from reference
		Type resultType = getType().closure();
		// an exception will have been thrown if the type is not acceptable

		ISetWriter reflex = ValueFactory.getInstance().relationWriter(resultType.getElementType());

		for (IValue e: carrier()) {
			reflex.insert(new Tuple(new IValue[] {e, e}));
		}
		
		return closure().union(reflex.done());
	}

	@Override
	public ISet carrier() {
		// NOTE: from reference
		Type newType = getType().carrier();
		ISetWriter w = ValueFactory.getInstance().setWriter(newType.getElementType());
		
		for (IValue t : this) {
			w.insertAll((ITuple) t);
		}
		
		return w.done();
	}

	@Override
	public ISet domain() {
		// NOTE: from reference
		Type relType = getType();
		ISetWriter w = ValueFactory.getInstance().setWriter(relType.getFieldType(0));
		
		for (IValue elem : this) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(0));
		}
		return w.done();
	}
	
	public ISet range() {
		// NOTE: from reference
		Type relType = getType();
		int last = relType.getArity() - 1;
		ISetWriter w = ValueFactory.getInstance().setWriter(relType.getFieldType(last));
		
		for (IValue elem : this) {
			ITuple tuple = (ITuple) elem;
			w.insert(tuple.get(last));
		}
		
		return w.done();
	}	
				
	public ISet select(int... fields) {
		// NOTE: from reference
		Type eltType = getFieldTypes().select(fields);
		ISetWriter w = ValueFactory.getInstance().setWriter(eltType);
		
		for (IValue v : this) {
			w.insert(((ITuple) v).select(fields));
		}
		
		return w.done();
	}

	@Override
	public ISet selectByFieldNames(String... fields)
			throws FactTypeUseException {
		// NOTE: from reference
		int[] indexes = new int[fields.length];
		int i = 0;
		
		if (getFieldTypes().hasFieldNames()) {
			for (String field : fields) {
				indexes[i++] = getFieldTypes().getFieldIndex(field);
			}
			
			return select(indexes);
		}
		
		throw new IllegalOperationException("select with field names", getType());
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Relation) {
			Relation that = (Relation) other;
			return this.xs.equals(that.xs);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return xs.hashCode();
	}	
	
}
