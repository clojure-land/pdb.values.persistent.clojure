package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListRelationWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

class ListRelationWriter extends ListWriter implements IListRelationWriter {

	protected ListRelationWriter(Type et) {
		super(et);
	}

	@Override
	public IListRelation done() {
		return ListOrRel.apply(et, xs);
	}

}

class ListRelationWriterWithTypeInference extends ListRelationWriter {

	protected ListRelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}