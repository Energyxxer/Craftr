package com.energyxxer.cbe.compile.parsing.classes.units;

import java.util.ArrayList;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEFile;
import com.energyxxer.cbe.compile.parsing.classes.fields.Field;
import com.energyxxer.cbe.compile.parsing.exceptions.CBEParserException;
import com.energyxxer.cbe.minecraft.util.Selector;
import com.energyxxer.cbe.util.Range;
import com.energyxxer.cbe.util.vprimitives.VInteger;

public class CBEEntity extends CBEUnit {
	
	public int id;
	protected ArrayList<String> promises = new ArrayList<String>();
	protected ArrayList<Field> fields = new ArrayList<Field>();
	public ArrayList<CBEEntity> subEntities = new ArrayList<CBEEntity>();
	public String entityExtends = null;
	public String entityType = "armor_stand";
	
	public CBEEntity(CBEFile file, TokenPattern unit) throws CBEParserException {
		super(file, unit);
	}
	public CBEEntity newField(Field f) {
		this.fields.add(f);
		return this;
	}
	public CBEEntity setPromise(String p) {
		promises.add(p);
		return this;
	}
	public CBEEntity setID(VInteger id) {
		this.id = id.value;
		id.value++;
		for(CBEEntity e : subEntities) {
			e.setID(id);
		}
		return this;
	}
	public CBEEntity propagateType() {
		propagateType(this.entityExtends);
		return this;
	}
	private void propagateType(String type) {
		this.entityType = type;
		for(CBEEntity e : subEntities) {
			e.propagateType(type);
		}
	}
	public Range getIdRange() {
		Range r = new Range(id,id);
		for(CBEEntity e : subEntities) {
			r = Range.union(r, e.getIdRange());
		}
		return r;
	}
	public Selector getSelector() {
		Selector s = new Selector("@e");
		Range r = getIdRange();
		s.args.put("type", entityType);
		s.args.put("score_" + file.getProject().getPrefix() + "_eid_min", "" +((int) r.min));
		s.args.put("score_" + file.getProject().getPrefix() + "_eid", "" + "" + ((int) r.max));
		return s;
	}
	
	@Override
	public String toString() {
		String o = "\n" + name + " (" + entityType + ", ID " + id + ")\t" + getSelector();
		for(CBEEntity e : subEntities) {
			o += e.toString().replace("\n", "\n\t");
		}
		return o;
	}
}
