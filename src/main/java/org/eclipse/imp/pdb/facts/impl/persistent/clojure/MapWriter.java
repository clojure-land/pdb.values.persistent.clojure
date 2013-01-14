package org.eclipse.imp.pdb.facts.impl.persistent.clojure;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

import clojure.lang.IPersistentMap;
import clojure.lang.ITransientMap;
import clojure.lang.PersistentHashMap;

class MapWriter implements IMapWriter {

	protected final Type kt, vt;
	protected ITransientMap xs;

	protected MapWriter(Type kt, Type vt){
		super();
		this.kt = kt;
		this.vt = vt;
		this.xs = PersistentHashMap.EMPTY.asTransient();
	}
	
	@Override
	public void insert(IValue... values) throws FactTypeUseException {
		for (IValue x : values) {
			ITuple t = (ITuple) x;
			xs = (ITransientMap) xs.assoc(t.get(0), t.get(1));
		}
	}

	@Override
	public void insertAll(Iterable<? extends IValue> values)
			throws FactTypeUseException {
		for (IValue x : values) {
			ITuple t = (ITuple) x;
			xs = (ITransientMap) xs.assoc(t.get(0), t.get(1));
		}
	}
	
	@Override
	public void put(IValue key, IValue value) throws FactTypeUseException {
		xs = (ITransientMap) xs.assoc(key, value);	
	}

	@Override
	public void putAll(IMap map) throws FactTypeUseException {
		for (IValue k: map) {
			xs = (ITransientMap) xs.assoc(k, map.get(k));
		}
	}

	@Override
	public void putAll(java.util.Map<IValue, IValue> map) throws FactTypeUseException {
		for (IValue k: map.keySet()) {
			xs = (ITransientMap) xs.assoc(k, map.get(k));
		}
	}

	@Override
	public IMap done() {
		IPersistentMap resultMap = xs.persistent();
		Type resultKt = (resultMap.count() == 0) ? TypeFactory.getInstance().voidType() : kt;
		Type resultVt = (resultMap.count() == 0) ? TypeFactory.getInstance().voidType() : vt;
		return new Map(resultKt, resultVt, resultMap);
	}

}

class MapWriterWithTypeInference extends MapWriter {

	protected MapWriterWithTypeInference() {
		super(TypeFactory.getInstance().voidType(), TypeFactory.getInstance().voidType());
	}
	
	@Override
	public IMap done() {
		IPersistentMap resultMap = xs.persistent();
		Type resultKt = List.lub(ClojureHelper.core$keys(resultMap));
		Type resultVt = List.lub(ClojureHelper.core$vals(resultMap));
		
		return new Map(resultKt, resultVt, resultMap);
	}	
	
}