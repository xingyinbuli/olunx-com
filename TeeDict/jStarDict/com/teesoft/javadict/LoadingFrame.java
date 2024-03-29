/*
 * LoadingFrame.java
 *
 * Created on March 16, 2008, 2:16 PM
 */

package com.teesoft.javadict;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 *
 * @author  wind
 */
public class LoadingFrame extends javax.swing.JFrame {
    private static LoadingFrame frame=null;
    
    /** Creates new form LoadingFrame */
    public LoadingFrame() {
        initComponents();
        centerScreen();
        
    }
public void centerScreen() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2,
            (dim.height - abounds.height) / 2);
        requestFocus();
    }
public static void showLoading()
{
    if (frame == null)
        frame = new LoadingFrame();
    frame.setVisible(true);
}
public static void hideLoading()
{
if (frame != null)
{
    frame.setVisible(false);
    frame.dispose();
    frame = null;
}    
}
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLoading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/teesoft/javadict/resources/JavaDict"); // NOI18N
        lblLoading.setText(bundle.getString("LoadingFrame.lblLoading.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(72, 72, 72)
                .add(lblLoading, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 282, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(28, 28, 28)
                .add(lblLoading)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadingFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblLoading;
    // End of variables declaration//GEN-END:variables
    
}
