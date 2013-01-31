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
		return (IRelation) super.done();
	}

}

class RelationWriterWithTypeInference extends RelationWriter {

	protected RelationWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType());
	}
	
}