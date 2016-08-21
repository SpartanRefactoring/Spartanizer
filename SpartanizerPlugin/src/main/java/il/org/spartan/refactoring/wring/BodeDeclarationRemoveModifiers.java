package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** convert
 *
 * <pre>
 * <b>abstract</b> <b>interface</b> a
 * {}
 * </pre>
 *
 * to
 *
 * <pre>
 * <b>interface</b> a {}
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public class BodeDeclarationRemoveModifiers<N extends BodyDeclaration> extends Wring.ReplaceCurrentNode<N> implements Kind.SyntacticBaggage {
  // @formatter:off
  public static class OfAnnotation extends BodeDeclarationRemoveModifiers<AnnotationTypeDeclaration> { /* empty */ }
  public static class OfEnumConstant extends BodeDeclarationRemoveModifiers<EnumConstantDeclaration> { /* empty */ }
  public static class OfEnum extends BodeDeclarationRemoveModifiers<TypeDeclaration> { /* empty */ }
  public static class OfField extends BodeDeclarationRemoveModifiers<FieldDeclaration> { /* empty */ }
  public static class OfMethod extends BodeDeclarationRemoveModifiers<MethodDeclaration> { /* empty */ }
  public static class OfType extends BodeDeclarationRemoveModifiers<TypeDeclaration> { /* empty */ }
  // @formatter:on

  private static Set<Modifier> matches(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier m : modifiers(¢))
      if (test(m, ps))
        $.add((Modifier) m);
    return $;
  }

  private static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier m : ms)
      if (test(m, ps))
        $.add((Modifier) m);
    return $;
  }

  private static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ps) {
    return matches(modifiers(¢), ps);
  }

  private static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ps) {
    for (final Iterator<IExtendedModifier> ¢ = modifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ps))
        ¢.remove();
    return $;
  }

  private static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (modifiers(¢).isEmpty())
      return $;
    if (Is.isEnumDeclaration(¢))
      $.add(Modifier::isStatic);
    if (Is.isInterface(¢) || ¢ instanceof AnnotationTypeDeclaration) {
      $.add(Modifier::isStatic);
      $.add(Modifier::isAbstract);
    }
    if (Is.isMethodDeclaration(¢) && (Is.isPrivate(¢) || Is.isStatic(¢)))
      $.add(Modifier::isFinal);
    final ASTNode container = extract.containerType(¢);
    if (container == null)
      return $;
    if (Is.isAbstractTypeDeclaration(container) && Is.isFinal(asAbstractTypeDeclaration(container)) && Is.isMethodDeclaration(¢))
      $.add(Modifier::isFinal);
    if (Is.isInterface(container)) {
      $.add(Modifier::isPublic);
      $.add(Modifier::isPrivate);
      $.add(Modifier::isProtected);
      if (Is.isMethodDeclaration(¢))
        $.add(Modifier::isAbstract);
    }
    if (Is.isEnumDeclaration(container))
      $.add(Modifier::isProtected);
    if (Is.isAnonymousClassDeclaration(container)) {
      $.add(Modifier::isPrivate);
      if (Is.isMethodDeclaration(¢))
        $.add(Modifier::isFinal);
      if (Is.isEnumConstantDeclaration(extract.containerType(container)))
        $.add(Modifier::isProtected);
    }
    return $;
  }

  private static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }

  private static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ps) {
    return m instanceof Modifier && test((Modifier) m, ps);
  }

  private static boolean test(final Modifier m, final Set<Predicate<Modifier>> ps) {
    for (final Predicate<Modifier> p : ps)
      if (p.test(m))
        return true;
    return false;
  }

  @Override String description(final BodyDeclaration ¢) {
    return "Remove redundant " + redundants(¢) + " modifier(s) from declaration";
  }

  @Override BodyDeclaration replacement(final BodyDeclaration $) {
    return prune(duplicate($), redundancies($));
  }

  @Override boolean scopeIncludes(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> ps = redundancies(¢);
    return !ps.isEmpty() && !matchess(¢, ps).isEmpty();
  }
}
