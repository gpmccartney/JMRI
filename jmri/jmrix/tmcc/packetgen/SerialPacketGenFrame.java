// SerialPacketGenFrame.java

package jmri.jmrix.tmcc.packetgen;

import jmri.util.StringUtil;
import jmri.jmrix.tmcc.SerialMessage;
import jmri.jmrix.tmcc.SerialReply;
import jmri.jmrix.tmcc.SerialTrafficController;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Frame for user input of serial messages
 * @author	Bob Jacobsen   Copyright (C) 2002, 2003, 2006
 * @version	$Revision: 1.1 $
 */
public class SerialPacketGenFrame extends javax.swing.JFrame implements jmri.jmrix.tmcc.SerialListener {

    // member declarations
    javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    javax.swing.JButton sendButton = new javax.swing.JButton();
    javax.swing.JTextField packetTextField = new javax.swing.JTextField(12);

    public SerialPacketGenFrame() {
    }

    public void initComponents() throws Exception {
        // the following code sets the frame's initial state

        jLabel1.setText("Command:");
        jLabel1.setVisible(true);

        sendButton.setText("Send");
        sendButton.setVisible(true);
        sendButton.setToolTipText("Send packet");

        packetTextField.setText("");
        packetTextField.setToolTipText("Enter command as hexadecimal bytes separated by a space");
        packetTextField.setMaximumSize(
                                       new Dimension(packetTextField.getMaximumSize().width,
                                                     packetTextField.getPreferredSize().height
                                                     )
                                       );

        setTitle("Send TMCC command");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        getContentPane().add(jLabel1);
        getContentPane().add(packetTextField);
        getContentPane().add(sendButton);


        sendButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    sendButtonActionPerformed(e);
                }
            });

        getContentPane().add(new JSeparator(JSeparator.HORIZONTAL));

        addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    thisWindowClosing(e);
                }
            });

        // pack for display
        pack();
    }

    public void sendButtonActionPerformed(java.awt.event.ActionEvent e) {
        SerialTrafficController.instance().sendSerialMessage(createPacket(packetTextField.getText()), this);
    }

    SerialMessage createPacket(String s) {
        // gather bytes in result
        byte b[] = StringUtil.bytesFromHexString(s);
        if (b.length != 3) return null;  // no such thing as message of other than 3 bytes
        SerialMessage m = new SerialMessage();
        for (int i=0; i<b.length; i++) m.setElement(i, b[i]);
        return m;
    }

    public void  message(SerialMessage m) {}  // ignore replies
    public void  reply(SerialReply r) {} // ignore replies

    private boolean mShown = false;

    public void addNotify() {
        super.addNotify();

        if (mShown)
            return;

        // resize frame to account for menubar
        JMenuBar jMenuBar = getJMenuBar();
        if (jMenuBar != null) {
            int jMenuBarHeight = jMenuBar.getPreferredSize().height;
            Dimension dimension = getSize();
            dimension.height += jMenuBarHeight;
            setSize(dimension);
        }

        mShown = true;
    }

    // Close the window when the close box is clicked
    void thisWindowClosing(java.awt.event.WindowEvent e) {
        setVisible(false);
        dispose();
    }
}
