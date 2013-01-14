package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.ISeq;

public class ListOrRel {

	@SuppressWarnings("unchecked")
	public static <IListOrRel extends IList> IListOrRel apply(Type et, ISeq xs) {
		Type elementType = (xs.count() == 0) ? TypeFactory.getInstance().voidType() : et;
		
		if (elementType.isTupleType())
			return (IListOrRel) new ListRelation(elementType, xs);
		else
			return (IListOrRel) new List(elementType, xs);
	}
	
}
