/* Generated By:JJTree: Do not edit this line. OSuffixIdentifier.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.common.util.OResettable;
import com.orientechnologies.orient.core.collate.OCollate;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.id.OContextualRecordId;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.AggregationContext;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.*;

public class OSuffixIdentifier extends SimpleNode {

  protected OIdentifier      identifier;
  protected ORecordAttribute recordAttribute;
  protected boolean          star = false;

  public OSuffixIdentifier(int id) {
    super(id);
  }

  public OSuffixIdentifier(OrientSql p, int id) {
    super(p, id);
  }

  public OSuffixIdentifier(OIdentifier identifier) {
    this.identifier = identifier;
  }

  public OSuffixIdentifier(ORecordAttribute attr) {
    this.recordAttribute = attr;
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    if (identifier != null) {
      identifier.toString(params, builder);
    } else if (recordAttribute != null) {
      recordAttribute.toString(params, builder);
    } else if (star) {
      builder.append("*");
    }
  }

  public Object execute(OIdentifiable iCurrentRecord, OCommandContext ctx) {
    if (star) {
      return iCurrentRecord;
    }
    if (identifier != null) {
      String varName = identifier.getStringValue();
      if (ctx != null && varName.equalsIgnoreCase("$parent")) {
        return ctx.getParent();
      }
      if (varName.startsWith("$") && ctx != null && ctx.getVariable(varName) != null) {
        return ctx.getVariable(varName);
      }

      if (iCurrentRecord != null) {
        if (iCurrentRecord instanceof OContextualRecordId) {
          Map<String, Object> meta = ((OContextualRecordId) iCurrentRecord).getContext();
          if (meta != null && meta.containsKey(varName)) {
            return meta.get(varName);
          }
        }
        OElement rec = iCurrentRecord.getRecord();
        if (rec == null) {
          return null;
        }
        Object result = rec.getProperty(varName);
        if (result == null && ctx != null) {
          result = ctx.getVariable(varName);
        }
        return result;
      }
      return null;
    }
    if (recordAttribute != null && iCurrentRecord != null) {
      OElement rec = iCurrentRecord instanceof OElement ? (OElement) iCurrentRecord : iCurrentRecord.getRecord();
      if (rec != null) {
        return recordAttribute.evaluate(rec, ctx);
      }
    }

    return null;
  }

  public Object execute(OResult iCurrentRecord, OCommandContext ctx) {
    if (star) {
      return iCurrentRecord;
    }
    if (identifier != null) {
      String varName = identifier.getStringValue();
      if (ctx != null && varName.equalsIgnoreCase("$parent")) {
        return ctx.getParent();
      }
      if (ctx != null && varName.startsWith("$") && ctx.getVariable(varName) != null) {
        Object result = ctx.getVariable(varName);
        if (result instanceof OResettable) {
          ((OResettable) result).reset();
        }
        return result;
      }
      if (iCurrentRecord != null) {
        if (iCurrentRecord.hasProperty(varName)) {
          return iCurrentRecord.getProperty(varName);
        }
        if (iCurrentRecord.getMetadataKeys().contains(varName)) {
          return iCurrentRecord.getMetadata(varName);
        }
        if (iCurrentRecord instanceof OResultInternal && ((OResultInternal) iCurrentRecord).getTemporaryProperties()
            .contains(varName)) {
          return ((OResultInternal) iCurrentRecord).getTemporaryProperty(varName);
        }
      }
      return null;
    }

    if (iCurrentRecord != null && recordAttribute != null) {
      return recordAttribute.evaluate(iCurrentRecord, ctx);
    }

    return null;
  }

  public Object execute(Map iCurrentRecord, OCommandContext ctx) {
    if (star) {
      OResultInternal result = new OResultInternal();
      if (iCurrentRecord != null) {
        for (Map.Entry<Object, Object> x : ((Map<Object, Object>) iCurrentRecord).entrySet()) {
          result.setProperty("" + x.getKey(), x.getValue());
        }
        return result;
      }
      return iCurrentRecord;
    }
    if (identifier != null) {
      String varName = identifier.getStringValue();
      if (ctx != null && varName.equalsIgnoreCase("$parent")) {
        return ctx.getParent();
      }
      if (ctx != null && ctx.getVariable(varName) != null) {
        return ctx.getVariable(varName);
      }
      if (iCurrentRecord != null) {
        return iCurrentRecord.get(varName);
      }
      return null;
    }
    if (recordAttribute != null) {
      return iCurrentRecord.get(recordAttribute.name);
    }
    return null;
  }

  public Object execute(Iterable iterable, OCommandContext ctx) {
    if (star) {
      return null;
    }
    List<Object> result = new ArrayList<>();
    for (Object o : iterable) {
      result.add(execute(o, ctx));
    }
    return result;
  }

  public Object execute(Iterator iterator, OCommandContext ctx) {
    if (star) {
      return null;
    }
    List<Object> result = new ArrayList<>();
    while (iterator.hasNext()) {
      result.add(execute(iterator.next(), ctx));
    }
    if (iterator instanceof OResultSet) {
      try {
        ((OResultSet) iterator).reset();
      } catch (Exception ignore) {
      }
    }
    return result;
  }

  public Object execute(OCommandContext iCurrentRecord) {
    if (star) {
      return null;
    }
    if (identifier != null) {
      String varName = identifier.getStringValue();
      if (iCurrentRecord != null) {
        return iCurrentRecord.getVariable(varName);
      }
      return null;
    }
    if (recordAttribute != null && iCurrentRecord != null) {
      return iCurrentRecord.getVariable(recordAttribute.name);
    }
    return null;
  }

  public Object execute(Object currentValue, OCommandContext ctx) {
    if (currentValue instanceof OResult) {
      return execute((OResult) currentValue, ctx);
    }
    if (currentValue instanceof OIdentifiable) {
      return execute((OIdentifiable) currentValue, ctx);
    }
    if (currentValue instanceof Map) {
      return execute((Map) currentValue, ctx);
    }
    if (currentValue instanceof OCommandContext) {
      return execute((OCommandContext) currentValue);
    }
    if (currentValue instanceof Iterable) {
      return execute((Iterable) currentValue, ctx);
    }
    if (currentValue instanceof Iterator) {
      return execute((Iterator) currentValue, ctx);
    }
    if (currentValue == null) {
      return execute((OResult) null, ctx);
    }

    return null;
    // TODO other cases?
  }

  public boolean isBaseIdentifier() {
    return identifier != null;
  }

  public boolean needsAliases(Set<String> aliases) {
    if (identifier != null) {
      return aliases.contains(identifier.getStringValue());
    }
    if (recordAttribute != null) {
      for (String s : aliases) {
        if (s.equalsIgnoreCase(recordAttribute.name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isAggregate() {
    return false;
  }

  public boolean isCount() {
    return false;
  }

  public OSuffixIdentifier splitForAggregation(AggregateProjectionSplit aggregateProj) {
    return this;
  }

  public boolean isEarlyCalculated(OCommandContext ctx) {
    if (identifier != null && identifier.isEarlyCalculated(ctx)) {
      return true;
    }
    return false;
  }

  public void aggregate(Object value, OCommandContext ctx) {
    throw new UnsupportedOperationException("this operation does not support plain aggregation: " + toString());
  }

  public AggregationContext getAggregationContext(OCommandContext ctx) {
    throw new UnsupportedOperationException("this operation does not support plain aggregation: " + toString());
  }

  public OSuffixIdentifier copy() {
    OSuffixIdentifier result = new OSuffixIdentifier(-1);
    result.identifier = identifier == null ? null : identifier.copy();
    result.recordAttribute = recordAttribute == null ? null : recordAttribute.copy();
    result.star = star;
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    OSuffixIdentifier that = (OSuffixIdentifier) o;

    if (star != that.star)
      return false;
    if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
      return false;
    if (recordAttribute != null ? !recordAttribute.equals(that.recordAttribute) : that.recordAttribute != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = identifier != null ? identifier.hashCode() : 0;
    result = 31 * result + (recordAttribute != null ? recordAttribute.hashCode() : 0);
    result = 31 * result + (star ? 1 : 0);
    return result;
  }

  public void extractSubQueries(SubQueryCollector collector) {

  }

  public boolean refersToParent() {
    if (identifier != null && identifier.getStringValue().equalsIgnoreCase("$parent")) {
      return true;
    }
    return false;
  }

  public void setValue(Object target, Object value, OCommandContext ctx) {
    if (target instanceof OResult) {
      setValue((OResult) target, value, ctx);
    } else if (target instanceof OIdentifiable) {
      setValue((OIdentifiable) target, value, ctx);
    } else if (target instanceof Map) {
      setValue((Map) target, value, ctx);
    }
  }

  public void setValue(OIdentifiable target, Object value, OCommandContext ctx) {
    if (target == null) {
      return;
    }
    OElement doc = null;
    if (target instanceof OElement) {
      doc = (OElement) target;
    } else {
      ORecord rec = target.getRecord();
      if (rec instanceof OElement) {
        doc = (OElement) rec;
      }
    }
    if (doc != null) {
      doc.setProperty(identifier.getStringValue(), value);
    } else {
      throw new OCommandExecutionException("Cannot set record attribute " + recordAttribute + " on existing document");
    }
  }

  public void setValue(Map target, Object value, OCommandContext ctx) {
    if (target == null) {
      return;
    }
    if (identifier != null) {
      target.put(identifier.getStringValue(), value);
    } else if (recordAttribute != null) {
      target.put(recordAttribute.getName(), value);
    }
  }

  public void setValue(OResult target, Object value, OCommandContext ctx) {
    if (target == null) {
      return;
    }
    if (target instanceof OResultInternal) {
      OResultInternal intTarget = (OResultInternal) target;
      if (identifier != null) {
        intTarget.setProperty(identifier.getStringValue(), value);
      } else if (recordAttribute != null) {
        intTarget.setProperty(recordAttribute.getName(), value);
      }
    } else {
      throw new OCommandExecutionException("Cannot set property on unmodifiable target: " + target);
    }
  }

  public void applyRemove(Object currentValue, OCommandContext ctx) {
    if (currentValue == null) {
      return;
    }
    if (identifier != null) {
      if (currentValue instanceof OResultInternal) {
        ((OResultInternal) currentValue).removeProperty(identifier.getStringValue());
      } else if (currentValue instanceof OElement) {
        ((OElement) currentValue).removeProperty(identifier.getStringValue());
      } else if (currentValue instanceof Map) {
        ((Map) currentValue).remove(identifier.getStringValue());
      }
    }
  }

  public OResult serialize() {
    OResultInternal result = new OResultInternal();
    if (identifier != null) {
      result.setProperty("identifier", identifier.serialize());
    }
    if (recordAttribute != null) {
      result.setProperty("recordAttribute", recordAttribute.serialize());
    }
    result.setProperty("star", star);
    return result;
  }

  public void deserialize(OResult fromResult) {
    if (fromResult.getProperty("identifier") != null) {
      identifier = OIdentifier.deserialize(fromResult.getProperty("identifier"));
    }
    if (fromResult.getProperty("recordAttribute") != null) {
      recordAttribute = new ORecordAttribute(-1);
      recordAttribute.deserialize(fromResult.getProperty("recordAttribute"));
    }
    star = fromResult.getProperty("star");
  }

  public boolean isDefinedFor(OResult currentRecord) {
    if (identifier != null) {
      return currentRecord.hasProperty(identifier.getStringValue());
    }
    return true;
  }

  public boolean isDefinedFor(OElement currentRecord) {
    if (identifier != null) {
      return ((ODocument) currentRecord.getRecord()).containsField(identifier.getStringValue());
    }
    return true;
  }

  public OCollate getCollate(OResult currentRecord, OCommandContext ctx) {
    if (identifier != null && currentRecord != null) {
      return currentRecord.getRecord().map(x -> (OElement) x).flatMap(elem -> elem.getSchemaType())
          .map(clazz -> clazz.getProperty(identifier.getStringValue())).map(prop -> prop.getCollate()).orElse(null);
    }
    return null;
  }

  public OIdentifier getIdentifier() {
    return identifier;
  }

  public boolean isCacheable() {
    return true;
  }

  public boolean isStar() {
    return star;
  }
}
/* JavaCC - OriginalChecksum=5d9be0188c7d6e2b67d691fb88a518f8 (do not edit this line) */
