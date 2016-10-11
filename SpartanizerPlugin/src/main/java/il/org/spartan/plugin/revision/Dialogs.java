package il.org.spartan.plugin.revision;

import java.net.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2016 */
public class Dialogs {
  static final String NAME = "Laconic";
  private static boolean iconInitialized;
  static org.eclipse.swt.graphics.Image icon;
  
  static org.eclipse.swt.graphics.Image icon() {
    if (!iconInitialized) {
      iconInitialized = true;
      try {
        icon = new org.eclipse.swt.graphics.Image(null,
            ImageDescriptor.createFromURL(new URL("platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif")).getImageData());
      } catch (final MalformedURLException x) {
        monitor.log(x);
      }
    }
    return icon;
  }
  
  static MessageDialog message(final String message) {
    return new MessageDialog(null, NAME, icon(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }
    };
  }

  static ProgressMonitorDialog progress(final boolean openOnRun) {
    final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.MODELESS);
      }
    };
    $.setBlockOnOpen(false);
    $.setCancelable(true);
    $.setOpenOnRun(openOnRun);
    return $;
  }
}
