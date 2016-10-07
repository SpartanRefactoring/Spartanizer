package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.annotations.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.Environment.*;
import il.org.spartan.spartanizer.utils.*;

/** Abstract class for implementing specific Environment annotation based
 * testers.
 * @see EnvFlatHandler
 * @see EnvNestedHandler
 * @author Dan Greenstein
 * @author Alex Kopzon */
public abstract class ENVTestEngineAbstract {
  // Sets that will be generated by the test engine.
  protected static LinkedHashSet<Entry<String, Information>> testSet;
  // Optionally provided by the user, in order to test the test engine.
  protected static LinkedHashSet<Entry<String, Information>> userProvidedSet;

  /** Adds a new Entry to testSet from the inner annotation.
   * @param ps JD. */
  public static void addTestSet(final List<MemberValuePair> ps) {
    final String s1 = wizard.condense(first(ps).getValue());
    final String s2 = wizard.condense(second(ps).getValue());
    if (testSetContainsVarName(s1.substring(1, s1.length() - 1)))
      azzert.fail("Bad test file - an entity appears twice.");
    testSet.add(new MapEntry<>(s1.substring(1, s1.length() - 1), new Information(type.baptize(s2.substring(1, s2.length() - 1)))));
  }

  /** Check if the testSet is contained in the generated or provided (manual
   * mode) Set. First checks in unordered fashion, and then checks inorder.
   * @param ¢ */
  static void compare(final LinkedHashSet<Entry<String, Information>> ¢) {
    compareOutOfOrder(¢, testSet);
    compareInOrder(¢, testSet);
  }

  /** Compares the given {@link LinkedHashSet} with the inner testSet.
   * Comparison done in-order. Assertion fails <b>iff</b> testSet is not
   * contained in the same order in the provided set.
   * @param contains JD
   * @return true iff the sets specified, are equally the same. */
  private static void compareInOrder(final LinkedHashSet<Entry<String, Information>> contains,
      final LinkedHashSet<Entry<String, Information>> contained) {
    assert contained != null;
    assert contains != null;
    final Iterator<Entry<String, Information>> s = contains.iterator();
    for (final Entry<String, Information> ¢ : contained) {
      boolean entryFound = false;
      while (s.hasNext())
        if (¢.equals(s.next())) {
          entryFound = true;
          break;
        }
      if (!entryFound) {
        // Without the inner reset, one failing test will cause other, working,
        // tests, to fail.
        testSetsReset();
        assert false : "some entry not found in order!";
      }
    }
  }

  /** Compares the given {@link LinkedHashSet} with the inner testSet.
   * Comparison done out-of-order. Assertion fails <b>iff</b> testSet is not
   * contained in the provided set.
   * @param contains JD
   * @return true iff the specified {@link LinkedHashSet} contains testSet. */
  private static void compareOutOfOrder(final LinkedHashSet<Entry<String, Information>> contains,
      final LinkedHashSet<Entry<String, Information>> contained) {
    assert contains != null;
    assert contained != null;
    if (contains.containsAll(contained))
      return;
    // Without the inner reset, one failing test will cause other, working,
    // tests, to fail.
    testSetsReset();
    assert false : "some entry not found out of order!";
  }

  protected static LinkedHashSet<Entry<String, Environment.Information>> generateSet() {
    return new LinkedHashSet<>();
  }

  /** @param from - file path
   * @return CompilationUnit of the code written in the file specified. */
  public static ASTNode getCompilationUnit(final String from) {
    assert from != null;
    final String ROOT = "./src/test/java/il/org/spartan/spartanizer/java/";
    assert ROOT != null;
    final File f = new File(ROOT + from);
    assert f != null;
    assert f.exists();
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(f);
    assert $ != null;
    return $;
  }

  /** Determines that we have got to the correct Annotation
   * @param n1
   * @return */
  public static boolean isNameId(final Name n1) {
    assert !"@Id".equals(n1 + ""); // To find the bug, if it appears as @Id, and
                                   // not Id.
    return "Id".equals(n1 + "");
  }

  private static boolean testSetContainsVarName(final String s) {
    for (final Entry<String, Information> ¢ : testSet)
      if (¢.getKey().equals(s))
        return true;
    return false;
  }

  public static void testSetsReset() {
    if (testSet != null)
      testSet.clear();
  }

  protected boolean foundTestedAnnotation; // Global flag, used to
  // determine when to system the
  // test on a node with
  // potential annotations.
  protected ASTNode n;

  protected abstract LinkedHashSet<Entry<String, Information>> buildEnvironmentSet(BodyDeclaration $);

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  protected abstract void handler(final Annotation ¢);

  /** define: outer annotation = OutOfOrderNestedENV, InOrderFlatENV, Begin,
   * End. define: inner annotation = Id. ASTVisitor that goes over the ASTNodes
   * in which annotations can be defined, and checks if the annotations are of
   * the kind that interests us. An array of inner annotations is defined inside
   * of each outer annotation of interest. I think it will be less error prone
   * and more scalable to implement another, internal, ASTVisitor that goes over
   * each inner annotation node, and send everything to an outside function to
   * add to the Sets as required. That means that each inner annotation will be
   * visited twice from the same outer annotation, but that should not cause
   * worry, since the outside visitor will do nothing. */
  public void runTest() {
    n.accept(new ASTVisitor() {
      /** Iterate over outer annotations of the current declaration and dispatch
       * them to handlers. otherwise */
      void checkAnnotations(final List<Annotation> as) {
        for (final Annotation ¢ : as)
          handler(¢);
      }

      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final Initializer ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      void visitNodesWithPotentialAnnotations(final BodyDeclaration $) {
        checkAnnotations(extract.annotations($));
        if (!foundTestedAnnotation)
          return;
        if (userProvidedSet != null) {
          // We're in manual mode, test against provided Set, not the generated
          // one.
          compare(userProvidedSet);
          testSetsReset();
          return;
        }
        final LinkedHashSet<Entry<String, Information>> enviromentSet = buildEnvironmentSet($);
        if (enviromentSet == null)
          return;
        compare(enviromentSet);
        testSetsReset();
        foundTestedAnnotation = false;
      }
    });
  }
}