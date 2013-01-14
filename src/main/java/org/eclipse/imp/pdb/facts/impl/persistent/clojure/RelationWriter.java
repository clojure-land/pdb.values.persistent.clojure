package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

class RelationWriter extends SetWriter implements IRelationWriter {

	protected RelationWriter(Type et) {
		super(et);
	}

	@Override
	public IRelation done() {
		return SetOrRel.apply(et, xs);
	}

}

class RelationWriterWithTypeInference extends RelationWriter {

	protected RelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}