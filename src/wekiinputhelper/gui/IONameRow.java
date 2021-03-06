/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

/**
 *
 * @author rebecca
 */
public class IONameRow extends javax.swing.JPanel {
    private int num;
    private String name;
   
    
    /**
     * Creates new form InputNameRow
     */
    public IONameRow() {
        initComponents();
    }
    
    public IONameRow(int num, String name) {
        initComponents();
        setNum(num);
        setIOName(name);
    }
    
    public void setNum(int num) {
        this.num = num;
        labelNum.setText(num + ".");
    }
    
    public void setIOName(String name) {
        this.name = name;
        fieldName.setText(name);
    }
    
    public int getNum() {
        return num;
    }
    
    public String getIOName() {
        return fieldName.getText().trim();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelNum = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        labelNum.setText("100.");

        fieldName.setText("Input 100");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelNum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNum)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelNum;
    // End of variables declaration//GEN-END:variables
}
