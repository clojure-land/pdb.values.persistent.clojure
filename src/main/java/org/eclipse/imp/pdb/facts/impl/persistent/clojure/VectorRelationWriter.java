package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentVector;

class VectorRelationWriter extends VectorWriter implements IListRelationWriter {

	protected VectorRelationWriter(Type et) {
		super(et);
	}

	@Override
	public IListRelation done() {
		return VectorOrRel.apply(et, (IPersistentVector) xs.persistent());
	}

}

class VectorRelationWriterWithTypeInference extends VectorRelationWriter {

	protected VectorRelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}