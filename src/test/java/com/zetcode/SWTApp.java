package com.zetcode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * ZetCode Java SWT tutorial
 *
 * This program uses the Label widget to
 * show lyrics of a song
 *
 * @author jan bodnar
 * website zetcode.com
 * last modified June 2009
 */


public class SWTApp {

    Shell shell;

    String lyrics =
"And I know that he knows I'm unfaithful\n"+
"And it kills him inside\n"+
"To know that I am happy with some other guy\n"+
"I can see him dyin'\n"+
"\n"+
"I don't wanna do this anymore\n"+
"I don't wanna be the reason why\n"+
"Every time I walk out the door\n"+
"I see him die a little more inside\n"+
"I don't wanna hurt him anymore\n"+
"I don't wanna take away his life\n"+
"I don't wanna be...A murderer";



    public SWTApp(Display display) {

        shell = new Shell(display);

        shell.setText("Unfaithful");

        initUI();

        
        shell.pack();
        shell.setLocation(300, 300);
        shell.open();
        

        while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
            display.sleep();
          }
        }
    }


    public void initUI() {

        Label label = new Label(shell, SWT.LEFT);
        label.setText(lyrics);
        
        Point p = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        label.setBounds(5, 5, p.x+5, p.y+5);
    }


    public static void main(String[] args) {
        Display display = new Display();
        Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i = 0; i < 100; i++) {
					System.out.println(i);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
			}
        	
        });
        t.start();
        
        new SWTApp(display);
        display.dispose();
    }
}