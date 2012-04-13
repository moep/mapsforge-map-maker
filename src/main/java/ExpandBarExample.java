import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ExpandBarExample {
    public static void main(String[] args) {
        Shell shell = new Shell(SWT.DIALOG_TRIM | SWT.MIN
                | SWT.APPLICATION_MODAL);
        shell.setLayout(new FormLayout());
        shell.setText("Expand Bar");
        final ExpandBar bar = new ExpandBar(shell, SWT.NONE);
        FormData fd = new FormData();
        fd.top = new FormAttachment(0);
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        fd.bottom = new FormAttachment(100);
        bar.setLayoutData(fd);

        bar.addExpandListener(new ExpandListener() {

            private void resize(final ExpandEvent event, final boolean expand){

                final Display display = Display.getCurrent();

                new Thread(new Runnable() {
                    public void run() {

                        final int[] orgSize = new int[1];
                        final int[] currentSize = new int[1];

                        final Object lock = new Object();

                        if (display.isDisposed() || bar.isDisposed()){
                            return;
                        }

                        display.syncExec(new Runnable() {
                            public void run() {
                                if (bar.isDisposed() || bar.getShell().isDisposed()){
                                    return;
                                }

                                synchronized(lock){
                                    bar.getShell().pack(true);
                                    orgSize[0] = bar.getShell().getSize().y;
                                    currentSize[0] = orgSize[0];
                                }
                            }
                        });     

                        while (currentSize[0] == orgSize[0]){
                            if (display.isDisposed() || bar.isDisposed()){
                                return;
                            }

                            display.syncExec(new Runnable() {
                                public void run() {

                                    synchronized(lock){
                                        if (bar.isDisposed() || bar.getShell().isDisposed()){
                                            return;
                                        }

                                        currentSize[0] = bar.getShell().getSize().y;

                                        if (currentSize[0] != orgSize[0]){
                                            return;
                                        }
                                        else{
                                            bar.getShell().layout(true);
                                            bar.getShell().pack(true);
                                        }
                                    }
                                }
                            });                             
                        }
                    }
                }).start();
        }

        public void itemCollapsed(ExpandEvent event) {
            resize(event, false);
        }

        public void itemExpanded(ExpandEvent event) {        
            resize(event, true);
        }

        });

        Composite composite = new Composite(bar, SWT.NONE);
        fd = new FormData();
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        composite.setLayoutData(fd);

        FormLayout layout = new FormLayout();
        layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 8;

        composite.setLayout(layout);
        Label label = new Label(composite, SWT.NONE);
        label.setText("This is Bar 1");
        ExpandItem item1 = new ExpandItem(bar, SWT.NONE, 0);
        item1.setText("Bar 1");
        item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        item1.setControl(composite);
        item1.setExpanded(true);

        composite = new Composite(bar, SWT.NONE);
        fd = new FormData();
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        composite.setLayoutData(fd);

        layout = new FormLayout();
        layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 8;
        composite.setLayout(layout);
        label = new Label(composite, SWT.NONE);
        label.setText("This is Bar2");
        ExpandItem item2 = new ExpandItem(bar, SWT.NONE, 1);
        item2.setText("Bar 2");
        item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        item2.setControl(composite);
        item2.setExpanded(true);

        composite = new Composite(bar, SWT.NONE);
        fd = new FormData();
        fd.left = new FormAttachment(0);
        fd.right = new FormAttachment(100);
        composite.setLayoutData(fd);

        layout = new FormLayout();
        layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 8;
        composite.setLayout(layout);
        label = new Label(composite, SWT.NONE);
        label.setText("This is Bar3");
        ExpandItem item3 = new ExpandItem(bar, SWT.NONE, 2);
        item3.setText("Bar 3");
        item3.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        item3.setControl(composite);
        item3.setExpanded(true);

        bar.setSpacing(6);
        shell.pack();
        shell.open();
        Display display = shell.getDisplay();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}