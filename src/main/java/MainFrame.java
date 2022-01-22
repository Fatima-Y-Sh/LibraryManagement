import java.awt.Color;
import java.awt.GridLayout;
import java.sql.*;
import javax.swing.*;

public class MainFrame {

    public static void loginFn(){
        new LoginFrame();
    }

    public static void user_frame(String UID) { new UserFrame(UID); }

    public static void librarian_frame() { new LibrarianFrame(); }

    public static class LoginFrame extends JFrame {

        LoginFrame(){

            setTitle("Login");

            JLabel l1 = new JLabel("Username", SwingConstants.CENTER);
            JLabel l2 = new JLabel("Password", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);
            JTextField usernameTF = new JTextField();
            usernameTF.setBackground(Utils.BACKGROUND_COLOR);
            usernameTF.setForeground(Utils.FOREGROUND_COLOR);
            JPasswordField passwordTF = new JPasswordField();
            passwordTF.setBackground(Utils.BACKGROUND_COLOR);
            passwordTF.setForeground(Utils.FOREGROUND_COLOR);
            JButton loginBtn = new JButton("Login");
            loginBtn.setBackground(Utils.BUTTON_COLOR);
            loginBtn.setForeground(Utils.FOREGROUND_COLOR);
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setBackground(Utils.BUTTON_COLOR);
            cancelBtn.setForeground(Utils.FOREGROUND_COLOR);

            loginBtn.addActionListener(e -> {

                String username = usernameTF.getText();
                String password = passwordTF.getText();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username");
                }
                else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter password");
                }
                else {
                    //Connect to the database
                    Connection connection = BddComm.getInstance().connect();
                    try {
                        Statement stmt = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
                        String st = ("SELECT * FROM USERS WHERE USERNAME='" + username + "' AND PASSWORD='" + password + "'");
                        ResultSet rs = stmt.executeQuery(st);
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "Invalid Username/Password!");
                        } else {
                            dispose();
                            rs.beforeFirst();
                            while (rs.next()) {
                                String admin = rs.getString("user_type");
                                System.out.println(admin);
                                String UID = rs.getString("UID");
                                if (admin.equals("1")) {
                                    //Redirecting to Librarian Frame
                                    librarian_frame();
                                } else {
                                    //Redirecting to User Frame for that user ID
                                    user_frame(UID);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            cancelBtn.addActionListener(e -> dispose());

            this.add(l1);
            this.add(usernameTF);
            this.add(l2);
            this.add(passwordTF);
            this.add(loginBtn);
            this.add(cancelBtn);

            this.setSize(330, 180);
            this.setLayout(new GridLayout(3, 2));
            this.setVisible(true);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void main(String[] args) {

        MainFrame mf = new MainFrame();

        mf.loginFn();
    }
}