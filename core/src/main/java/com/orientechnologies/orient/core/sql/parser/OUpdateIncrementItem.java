/* Generated By:JJTree: Do not edit this line. OUpdateIncrementItem.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.Map;

public class OUpdateIncrementItem extends SimpleNode {
  protected OIdentifier left;
  protected OModifier   leftModifier;
  protected OExpression right;

  public OUpdateIncrementItem(int id) {
    super(id);
  }

  public OUpdateIncrementItem(OrientSql p, int id) {
    super(p, id);
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    left.toString(params, builder);
    if (leftModifier != null) {
      leftModifier.toString(params, builder);
    }
    builder.append(" = ");
    right.toString(params, builder);
  }

  public OUpdateIncrementItem copy() {
    OUpdateIncrementItem result = new OUpdateIncrementItem(-1);
    result.left = left == null ? null : left.copy();
    result.leftModifier = leftModifier == null ? null : leftModifier.copy();
    result.right = right == null ? null : right.copy();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    OUpdateIncrementItem that = (OUpdateIncrementItem) o;

    if (left != null ? !left.equals(that.left) : that.left != null)
      return false;
    if (leftModifier != null ? !leftModifier.equals(that.leftModifier) : that.leftModifier != null)
      return false;
    if (right != null ? !right.equals(that.right) : that.right != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = left != null ? left.hashCode() : 0;
    result = 31 * result + (leftModifier != null ? leftModifier.hashCode() : 0);
    result = 31 * result + (right != null ? right.hashCode() : 0);
    return result;
  }
}
/* JavaCC - OriginalChecksum=94dd82febb904e4e31130bdcbbb48fe3 (do not edit this line) */
