/*
 * Created by JFormDesigner on Fri Nov 30 12:46:09 CST 2018
 */

package org.jordon.security.view;

import org.jordon.security.core.crypto.assymmetry.RSACipherService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Jasion
 */
public class RSABigIntegerView extends JFrame {
    private RSACipherService service = new RSACipherService(2 << 10);
    private final static SecureRandom random = new SecureRandom();
    private static final int N = 1024;
    RSACipherService key = new RSACipherService(N);

    public static void main(String[] args) {
        RSABigIntegerView view = new RSABigIntegerView();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    public RSABigIntegerView() {
        initComponents();
        eField.setText(key.getPublicKey().toString(10));
        dArea.setText(key.getPrivateKey().toString(10));
        nArea.setText(key.getModulus().toString(10));
    }

    private void encButtonActionPerformed(ActionEvent e) {
        BigInteger message = new BigInteger(ptArea.getText(), 10);
        BigInteger encrypt = key.encrypt(message);
        cArea.setText(encrypt.toString(10));
    }

    private void decButtonActionPerformed(ActionEvent e) {
        BigInteger decrypt = key.decrypt(new BigInteger(cArea.getText(), 10));
        ptArea.setText(decrypt.toString(10));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Jasion
        label1 = new JLabel();
        eField = new JTextField();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        dArea = new JTextArea();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        nArea = new JTextArea();
        label4 = new JLabel();
        scrollPane = new JScrollPane();
        ptArea = new JTextArea();
        label5 = new JLabel();
        scrollPane4 = new JScrollPane();
        cArea = new JTextArea();
        encButton = new JButton();
        decButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        setTitle("RSA - jordonyang");
        //---- label1 ----
        label1.setText("\u516c\u94a5e");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- eField ----
        eField.setEditable(false);

        //---- label2 ----
        label2.setText("\u79c1\u94a5d");
        label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane1 ========
        {

            //---- dArea ----
            dArea.setEditable(false);
            scrollPane1.setViewportView(dArea);
        }
        dArea.setLineWrap(true);  // 激活自动换行功能
        dArea.setWrapStyleWord(true);   // 激活断行不断字功能

        //---- label3 ----
        label3.setText("\u6a21n");
        label3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane2 ========
        {

            //---- nArea ----
            nArea.setEditable(false);
            scrollPane2.setViewportView(nArea);
        }
        nArea.setLineWrap(true);  // 激活自动换行功能
        nArea.setWrapStyleWord(true);   // 激活断行不断字功能

        //---- label4 ----
        label4.setText("\u660e\u6587");
        label4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane ========
        {
            scrollPane.setViewportView(ptArea);
        }
        ptArea.setLineWrap(true);  // 激活自动换行功能
        ptArea.setWrapStyleWord(true);   // 激活断行不断字功能

        //---- label5 ----
        label5.setText("\u5bc6\u6587");
        label5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane4 ========
        {
            scrollPane4.setViewportView(cArea);
        }
        cArea.setLineWrap(true);  // 激活自动换行功能
        cArea.setWrapStyleWord(true);   // 激活断行不断字功能

        //---- encButton ----
        encButton.setText("\u52a0\u5bc6");
        encButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        encButton.addActionListener(e -> encButtonActionPerformed(e));

        //---- decButton ----
        decButton.setText("\u89e3\u5bc6");
        decButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        decButton.addActionListener(e -> decButtonActionPerformed(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(label1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(eField, GroupLayout.PREFERRED_SIZE, 648, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(label3)
                                        .addComponent(label4)
                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 647, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 647, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 647, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 647, GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(242, 242, 242)
                            .addComponent(encButton)
                            .addGap(119, 119, 119)
                            .addComponent(decButton)))
                    .addContainerGap(38, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label1)
                        .addComponent(eField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label2)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
                    .addGap(34, 34, 34)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label3)
                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addComponent(label4))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label5)
                        .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                    .addGap(40, 40, 40)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(decButton)
                        .addComponent(encButton))
                    .addGap(41, 41, 41))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Jasion
    private JLabel label1;
    private JTextField eField;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea dArea;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea nArea;
    private JLabel label4;
    private JScrollPane scrollPane;
    private JTextArea ptArea;
    private JLabel label5;
    private JScrollPane scrollPane4;
    private JTextArea cArea;
    private JButton encButton;
    private JButton decButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
