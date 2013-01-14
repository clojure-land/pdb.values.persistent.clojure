package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentVector;

public class VectorOrRel {	
	
	@SuppressWarnings("unchecked")
	public static <IListOrRel extends IList> IListOrRel apply(Type et, IPersistentVector xs) {
		Type elementType = (xs.count() == 0) ? TypeFactory.getInstance().voidType() : et;
		
		if (elementType.isTupleType())
			return (IListOrRel) new VectorRelation(elementType, xs);
		else
			return (IListOrRel) new Vector(elementType, xs);
	}
	
}
