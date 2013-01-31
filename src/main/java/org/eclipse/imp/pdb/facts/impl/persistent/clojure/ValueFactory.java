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

import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.fast.FastBaseValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class ValueFactory extends FastBaseValueFactory implements IValueFactory {

	/*package*/ ValueFactory() {
		super();
	}	
	
	private static class InstanceKeeper {
		public final static ValueFactory instance = new ValueFactory();
	}
	
	public static ValueFactory getInstance(){
		return InstanceKeeper.instance;
	}	
	
	@Override
	public IConstructor constructor(Type constructorType) {
		return new Constructor(constructorType);
	}

	@Override
	public IConstructor constructor(Type constructorType, IValue... children)
			throws FactTypeUseException {
		return new Constructor(constructorType, children);
	}
	
	@Override
	public IConstructor constructor(Type constructorType,
			Map<String, IValue> annotations, IValue... children)
			throws FactTypeUseException {
		return new Constructor(constructorType, annotations, children);	
	}

	@Override
	public IList list(IValue... values) {
		IListWriter writer = listWriter();
		writer.insert(values);
		return writer.done();
	}

	@Override
	public IList list(Type et) {
		return listWriter(et).done();
	}
		
	@Override
	public IListRelation listRelation(IValue... values) {
		return (IListRelation) list(values);
	}

	@Override
	public IListRelation listRelation(Type tupleType) {
		return listRelationWriter(tupleType).done();
	}

	@Override
	public IListRelationWriter listRelationWriter() {
//		return new ListRelationWriterWithTypeInference();
		return new VectorRelationWriterWithTypeInference();
	}

	@Override
	public IListRelationWriter listRelationWriter(Type et) {
//		return new ListRelationWriter(et);
		return new VectorRelationWriter(et);	
	}

	@Override
	public IListWriter listWriter() {
//		return new ListWriterWithTypeInference();
//		return new VectorWriterWithTypeInference();
		return new FastListWriter();
	}

	@Override
	public IListWriter listWriter(Type et) {
//		return new ListWriter(et);
//		return new VectorWriter(et);
		return new FastListWriter(et);		
	}

	@Override
	public IMap map(Type mapType) {
		return map(mapType.getKeyType(), mapType.getValueType());
	}

	@Override
	public IMap map(Type kt, Type vt) {
		return mapWriter(TypeFactory.getInstance().voidType(), TypeFactory.getInstance().voidType()).done();
	}

	@Override
	public IMapWriter mapWriter() {
		return new MapWriterWithTypeInference();
	}

	@Override
	public IMapWriter mapWriter(Type mapType) {
		return mapWriter(mapType.getKeyType(), mapType.getValueType());
	}

	@Override
	public IMapWriter mapWriter(Type kt, Type vt) {
		return new MapWriter(kt, vt);
	}

	@Override
	public INode node(String name) {
		return new Node(name);
	}

	@Override
	public INode node(String name, IValue... children) {
		return new Node(name, children);
	}

	@Override
	public INode node(String name, Map<String, IValue> annotations,
			IValue... children) throws FactTypeUseException {
		return new Node(name, annotations, children);
	}

	@Override
	public IRelation relation(IValue... values) {
		return new Relation(values);
	}

	@Override
	public IRelation relation(Type et) {
//		return new Relation(et);
		return relationWriter(TypeFactory.getInstance().voidType()).done();
	}

	@Override
	public IRelationWriter relationWriter() {
		return new RelationWriterWithTypeInference();
	}

	@Override
	public IRelationWriter relationWriter(Type et) {
		return new RelationWriter(et);
	}

	@Override
	public ISet set(IValue... values) {
		Type et = TypeInferenceHelper.lub(values, TypeFactory.getInstance().voidType());
		return et.isTupleType() ? new Relation(values) : new Set(values);
	}

	@Override
	public ISet set(Type et) {
		return setWriter(TypeFactory.getInstance().voidType()).done();
	}

	@Override
	public ISetWriter setWriter() {
//		return new SetWriterWithTypeInference();
		return new FastSetWriter();
	}

	@Override
	public ISetWriter setWriter(Type et) {
//		return et.isTupleType() ? new RelationWriter(et) : new SetWriter(et);
		return new FastSetWriter(et);
	}

	@Override
	public ITuple tuple() {
		return new Tuple();
	}

	@Override
	public ITuple tuple(IValue... values) {
		return new Tuple(values);
	}

	@Override
	public ITuple tuple(Type type, IValue... values) {
		return new Tuple(type, values);
	}
	
	@Override
	public String toString() {
		return "VF_CLOJURE";
	}
	
}
