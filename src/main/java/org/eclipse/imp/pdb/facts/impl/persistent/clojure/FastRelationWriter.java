package org.eclipse.imp.pdb.facts.impl.persistent.clojure;
/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/


import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.util.collections.ShareableValuesHashSet;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentSet;
import clojure.lang.ITransientCollection;
import clojure.lang.PersistentHashSet;

// TODO Add checking.
/**
 * Implementation of IRelationWriter.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class FastRelationWriter implements IRelationWriter{
	protected Type tupleType;
	
	protected final ShareableValuesHashSet data;
	
	protected IRelation constructedRelation;

	protected final boolean inferred;
	
	/*package*/ FastRelationWriter(Type tupleType){
		super();
		
		if (!tupleType.isTupleType()) {
			throw new IllegalArgumentException("should be a tuple type");
		}
		
		this.tupleType = tupleType;
		this.inferred = false;
		
		data = new ShareableValuesHashSet();
		
		constructedRelation = null;
	}
	
	/*package*/ FastRelationWriter(){
		super();
		
		this.tupleType = TypeFactory.getInstance().voidType();
		
		data = new ShareableValuesHashSet();
		
		constructedRelation = null;
		this.inferred = true;
	}
	
	/*package*/ FastRelationWriter(Type tupleType, ShareableValuesHashSet data){
		super();
		
		this.tupleType = tupleType;
		this.data = new ShareableValuesHashSet(data);
		this.inferred = false;
		
		constructedRelation = null;
	}
	
	public void insert(IValue element){
		checkMutation();
		updateType(element);
		data.add(element);
	}
	
	private void updateType(IValue element) {
		if (inferred) {
			tupleType = tupleType.lub(element.getType());
			if (!tupleType.isTupleType()) {
				throw new IllegalArgumentException("relations can only contain tuples of the same arity");
			} 
		}
	}

	public void insert(IValue... elements){
		checkMutation();
		
		for(int i = elements.length - 1; i >= 0; i--){
			updateType(elements[i]);
			data.add(elements[i]);
		}
	}
	
	public void insertAll(Iterable<? extends IValue> collection){
		checkMutation();
		
		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while(collectionIterator.hasNext()){
			IValue next = collectionIterator.next();
			updateType(next);
			data.add(next);
		}
	}
	
	public void delete(IValue element){
		checkMutation();
		
		data.remove(element);
	}
	
	public int size(){
		return data.size();
	}

	protected void checkMutation(){
		if(constructedRelation != null) throw new UnsupportedOperationException("Mutation of a finalized map is not supported.");
	}
	
	public IRelation done(){
		ITransientCollection ret = PersistentHashSet.EMPTY.asTransient();
		for(Object item : data)
			ret = ret.conj(item);
		
		IPersistentSet persistentData = (IPersistentSet) ret.persistent();
		
		if (constructedRelation == null) {
		  constructedRelation = new Relation(data.isEmpty() ? TypeFactory.getInstance().voidType() : tupleType, persistentData);
		}
		
		return constructedRelation;
	}
}