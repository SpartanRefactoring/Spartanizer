package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Alex Kopakzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class ModifiersTest {
  @Test public void modifierUse__01() {
    azzert.that(Modifiers.gt("public", "public"), is(0));
  }

  @Test public void modifierUse__03() {
    azzert.that(Modifiers.gt("private", "public"), greaterThan(0));
  }

  @Test public void modifierUse__04() {
    azzert.that(Modifiers.gt("public", "abstract"), lessThan(0));
  }

  @Test public void modifierUse__05() {
    azzert.that(Modifiers.gt("public", "default"), lessThan(0));
  }

  @Test public void modifierUse__06() {
    azzert.that(Modifiers.gt("public", "static"), lessThan(0));
  }

  @Test public void modifierUse__07() {
    azzert.that(Modifiers.gt("public", "final"), lessThan(0));
  }

  @Test public void modifierUse__08() {
    azzert.that(Modifiers.gt("public", "transient"), lessThan(0));
  }

  @Test public void modifierUse__09() {
    azzert.that(Modifiers.gt("public", "volatile"), lessThan(0));
  }

  @Test public void modifierUse__10() {
    azzert.that(Modifiers.gt("public", "synchronized"), lessThan(0));
  }

  @Test public void modifierUse__11() {
    azzert.that(Modifiers.gt("public", "native"), lessThan(0));
  }

  @Test public void modifierUse__12() {
    azzert.that(Modifiers.gt("public", "strictfp"), lessThan(0));
  }

  @Test public void modifierUse__13() {
    azzert.that(Modifiers.gt("strictfp", "synchronized"), greaterThan(0));
  }

  @Test public void modifierUse__14() {
    azzert.that(Modifiers.gt("transient", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse__15() {
    azzert.that(Modifiers.gt("volatile", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse__16() {
    azzert.that(Modifiers.gt("default", "protected"), greaterThan(0));
  }

  @Test public void modifierUse__17() {
    azzert.that(Modifiers.find("public").compareTo(Modifiers.PUBLIC), is(0));
  }

  @Test public void modifierUse__18() {
    azzert.that(Modifiers.find("protected").compareTo(Modifiers.PROTECTED), is(0));
  }

  @Test public void modifierUse__19() {
    azzert.that(Modifiers.find("private").compareTo(Modifiers.PRIVATE), is(0));
  }

  @Test public void modifierUse__2() {
    azzert.that(Modifiers.gt("protected", "public"), greaterThan(0));
  }

  @Test public void modifierUse__20() {
    azzert.that(Modifiers.find("abstract").compareTo(Modifiers.ABSTRACT), is(0));
  }

  @Test public void modifierUse__21() {
    azzert.that(Modifiers.find("default").compareTo(Modifiers.DEFAULT), is(0));
  }

  @Test public void modifierUse__22() {
    azzert.that(Modifiers.find("static").compareTo(Modifiers.STATIC), is(0));
  }

  @Test public void modifierUse__23() {
    azzert.that(Modifiers.find("final"), is(Modifiers.FINAL));
  }

  @Test public void modifierUse__24() {
    azzert.that(Modifiers.find("transient").compareTo(Modifiers.TRANSIENT), is(0));
  }

  @Test public void modifierUse__25() {
    azzert.that(Modifiers.find("volatile").compareTo(Modifiers.VOLATILE), is(0));
  }

  @Test public void modifierUse__26() {
    azzert.that(Modifiers.find("synchronized").compareTo(Modifiers.SYNCHRONIZED), is(0));
  }

  @Test public void modifierUse__27() {
    azzert.that(Modifiers.find("native").compareTo(Modifiers.NATIVE), is(0));
  }

  @Test public void modifierUse__28() {
    azzert.that(Modifiers.find("strictfp").compareTo(Modifiers.STRICTFP), is(0));
  }
}
