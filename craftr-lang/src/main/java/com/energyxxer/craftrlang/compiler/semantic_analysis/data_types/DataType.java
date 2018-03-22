package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator.*;

/**
 * Created by User on 3/1/2017.
 */
public class DataType {

    private final String name;
    private final boolean primitive;
    private final Unit unit;
    private final boolean nullType;
    private InheritanceValidator inheritanceValidator = null;

    private ReferenceConstructor referenceConstructor;
    private TypeOperationPromise typeOperationPromise;

    public DataType(String name) {
        this(name, false);
    }

    public DataType(String name, boolean primitive, InheritanceValidator inheritanceValidator) {
        this.name = name;
        this.primitive = primitive;
        this.unit = null;
        this.nullType = false;
        this.inheritanceValidator = inheritanceValidator;
    }

    public DataType(String name, boolean primitive) {
        this(name, primitive, null);
    }

    public DataType(Unit unit) {
        this.name = unit.getFullyQualifiedName();
        this.primitive = false;
        this.unit = unit;
        this.nullType = false;
        this.inheritanceValidator = null;
    }

    //Null data type
    public DataType() {
        this.name = "null";
        this.primitive = true;
        this.unit = null;
        this.nullType = true;
        this.inheritanceValidator = t -> true;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    //Default types;
    public static final DataType VOID = new DataType("void", true);
    public static final DataType INT = new DataType("int", true);
    public static final DataType FLOAT = new DataType("float", true);
    public static final DataType DOUBLE = new DataType("double", true);
    public static final DataType CHAR = new DataType("char", true);
    public static final DataType LONG = new DataType("long", true);
    public static final DataType BOOLEAN = new DataType("boolean", true);

    public static final DataType NULL = new DataType();

    public static final DataType OBJECT = new DataType("craftr.lang.Object", false);
    public static final DataType STRING = new DataType("craftr.lang.String", false);
    public static final DataType TEMP_ARRAY = new DataType("TempArray", false);

    public static final DataType[] PRIMITIVES = new DataType[] {VOID, INT, FLOAT, DOUBLE, CHAR, LONG, BOOLEAN};
    public static final DataType[] DEFAULT_TYPES = new DataType[] {OBJECT, STRING};

    static {
        INT.setReferenceConstructor(IntegerValue::new);
        FLOAT.setReferenceConstructor(FloatValue::new);
        DOUBLE.setReferenceConstructor(DoubleValue::new);
        INT.setReferenceConstructor(IntegerValue::new);
        BOOLEAN.setReferenceConstructor(BooleanValue::new);
        //OBJECT.setReferenceConstructor({});

        CHAR.setInheritanceValidator(t -> t == INT || t == FLOAT || t == DOUBLE || t == LONG);
        INT.setInheritanceValidator(t -> t == FLOAT || t == DOUBLE || t == LONG);
        FLOAT.setInheritanceValidator(t -> t == DOUBLE || t == LONG);

        INT.setTypeOperationPromise((op, other)->{
            if(Arrays.asList(INT, FLOAT, DOUBLE, LONG).contains(other)) {
                if(Arrays.asList(ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO).contains(op)) return other;
                else if(Arrays.asList(EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return other;
            }
            return null;
        });
        FLOAT.setTypeOperationPromise((op, other)->{
            if(Arrays.asList(INT, FLOAT, DOUBLE, LONG).contains(other)) {
                if(Arrays.asList(ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO).contains(op)) return ((other == INT) ? FLOAT : other);
                else if(Arrays.asList(EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return other;
            }
            return null;
        });
        BOOLEAN.setTypeOperationPromise((op, other)->{
            if(other == BOOLEAN) {
                if(Arrays.asList(AND, OR, EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return STRING;
            }
            return null;
        });
    }

    public static DataType parseType(List<Token> flatTokens, SymbolTable table, SemanticContext semanticContext) {
        if(flatTokens.size() == 1) {
            for(DataType type : PRIMITIVES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
            for(DataType type : DEFAULT_TYPES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
        }
        if(flatTokens.size() >= 3 && flatTokens.get(1).type == CraftrLang.BRACE) {
            return TEMP_ARRAY;
        }
        Symbol symbol = table.getSymbol(flatTokens, semanticContext);
        if(symbol == null) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Symbol not found:" + flatTokens + " at semanticContext" + semanticContext, flatTokens.get(0)));
            return OBJECT;
        }
        if(!(symbol instanceof Unit)) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "'" + flatTokens.get(flatTokens.size()-1).value + "' is not a unit",flatTokens.get(flatTokens.size()-1)));
            return OBJECT;
        }

        return ((Unit) symbol).getDataType();
    }

    public DataType getReturnType(Operator op, DataType other) {
        return (typeOperationPromise != null) ? typeOperationPromise.check(op, other) : null;
    }

    public void setTypeOperationPromise(TypeOperationPromise typeOperationPromise) {
        this.typeOperationPromise = typeOperationPromise;
    }

    public void setReferenceConstructor(ReferenceConstructor referenceConstructor) {
        this.referenceConstructor = referenceConstructor;
    }

    public InheritanceValidator getInheritanceValidator() {
        return inheritanceValidator;
    }

    public void setInheritanceValidator(InheritanceValidator inheritanceValidator) {
        this.inheritanceValidator = inheritanceValidator;
    }

    public SymbolTable getSubSymbolTable() {
        //DO SOMETHING ABOUT THE FIELD TABLE THING
        return (unit != null) ? unit.getStaticFieldLog() : null;
    }

    public MethodLog getMethodLog() {
        return (unit != null) ? unit.getInstanceMethodLog() : null;
    }

    public boolean isNullType() {
        return nullType;
    }

    public boolean instanceOf(DataType type) {
        return (inheritanceValidator != null && inheritanceValidator.validate(type)) || validateFirst(type);
    }

    private boolean validateFirst(DataType type) {
        if(this.isNullType()) return true; //Yeah yeah null can be anything it wants to be, now get lost.
        if(type.isNullType()) return false; //Whoa whoa there buddy, null is unique; nobody else can be null.

        if(this.primitive || type.primitive) return this.equals(type);
        if(this.unit != null && type.unit != null)
            return this.unit.instanceOf(type.unit);
        else return this.name.equals(type.name);
    }

    public Value create(@NotNull DataReference reference, SemanticContext semanticContext) {
        if(referenceConstructor == null) {
            throw new RuntimeException("No reference constructor found for data type " + this);
        } else return referenceConstructor.create(reference, semanticContext);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataType dataType = (DataType) o;

        return name.equals(dataType.name);
    }

    @Override
    public String toString() {
        return name;
    }
}

interface InheritanceValidator {
    boolean validate(DataType other);
}