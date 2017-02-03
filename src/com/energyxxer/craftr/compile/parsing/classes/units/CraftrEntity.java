package com.energyxxer.craftr.compile.parsing.classes.units;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.parsing.classes.fields.CraftrField;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrFile;
import com.energyxxer.craftr.compile.exceptions.CraftrParserException;
import com.energyxxer.craftr.minecraft.util.Selector;
import com.energyxxer.craftr.util.Range;
import com.energyxxer.craftr.util.vprimitives.VInteger;

import java.util.ArrayList;

public class CraftrEntity extends CraftrUnit {
	
	public int id;
	protected ArrayList<String> promises = new ArrayList<String>();
	protected ArrayList<CraftrField> fields = new ArrayList<CraftrField>();
	public ArrayList<CraftrEntity> subEntities = new ArrayList<CraftrEntity>();
	public String entityExtends = null;
	public String entityType = "armor_stand";
	
	public CraftrEntity(CraftrFile file, TokenPattern unit) throws CraftrParserException {
		super(file, unit);
	}
	public CraftrEntity newField(CraftrField f) {
		this.fields.add(f);
		return this;
	}
	public CraftrEntity setPromise(String p) {
		promises.add(p);
		return this;
	}
	public CraftrEntity setID(VInteger id) {
		this.id = id.value;
		id.value++;
		for(CraftrEntity e : subEntities) {
			e.setID(id);
		}
		return this;
	}
	public CraftrEntity propagateType() {
		propagateType(this.entityExtends);
		return this;
	}
	private void propagateType(String type) {
		this.entityType = type;
		for(CraftrEntity e : subEntities) {
			e.propagateType(type);
		}
	}
	public Range getIdRange() {
		Range r = new Range(id,id);
		for(CraftrEntity e : subEntities) {
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
		for(CraftrEntity e : subEntities) {
			o += e.toString().replace("\n", "\n\t");
		}
		return o;
	}
}
