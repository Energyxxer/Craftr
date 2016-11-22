package com.energyxxer.cbe.compile.parsing.classes;

import java.io.File;
import java.util.ArrayList;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.fields.Field;
import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.minecraft.Entity;
import com.energyxxer.cbe.minecraft.util.Selector;
import com.energyxxer.cbe.util.Range;
import com.energyxxer.cbe.util.vprimitives.VInteger;

public class CBEEntity extends Entity {
	private final Project project;
	
	public int id;
	protected boolean isPublic;
	protected ArrayList<String> promises = new ArrayList<String>();
	protected ArrayList<Field> fields = new ArrayList<Field>();
	public ArrayList<CBEEntity> subEntities = new ArrayList<CBEEntity>();
	public String entityExtends = null;
	public String entityType = "armor_stand";
	public TokenPattern<?> declaration = null;
	public File file = null;
	
	public CBEEntity(String name, Project project, File file) {
		this.name = name;
		this.project = project;
		this.file = file;
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
	public CBEEntity setDeclaration(TokenPattern<?> p) {
		this.declaration = p;
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
		s.args.put("score_" + project.getPrefix() + "_eid_min", "" +((int) r.min));
		s.args.put("score_" + project.getPrefix() + "_eid", "" + "" + ((int) r.max));
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
