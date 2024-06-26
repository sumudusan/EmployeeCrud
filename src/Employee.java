import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JTable table1;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton updateButton;
    private JTextField txtid;
    private JPanel Main;
    private JScrollPane tabel_1;


    //we need create the main method.it will not be auto create.we need rightclick on
    // "public Employee()"> Generate>main form
    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //Connect database
    Connection con;
    PreparedStatement pst;

    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3307/rbcompany","root","");
            System.out.println("Successfully connected to database");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    //making table method
    void table_load()
    {
        try
        {
        pst=con.prepareStatement("select * from employee");
            ResultSet rs=pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();

        //save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = txtName.getText();
                String salary = txtSalary.getText();
                String mobile = txtMobile.getText();

                try {
                    pst=con.prepareStatement("insert into employee(empName,salary,mobile) values(?,?,?)");

                    pst.setString(1,name);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Employee added successfully");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


            }
        });

        //Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                String empid=txtid.getText();

                pst=con.prepareStatement("select empname,salary,mobile from employee where id=?");
                pst.setString(1,empid);
                ResultSet rs=pst.executeQuery();

                if(rs.next()==true)
                {
                    String empname=rs.getString(1);
                    String empsalary=rs.getString(2);
                    String empmobile=rs.getString(3);

                    txtName.setText(empname);
                    txtSalary.setText(empsalary);
                    txtMobile.setText(empmobile);
                }
                else
                {
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    JOptionPane.showMessageDialog(null,"Employee not found");
                }
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname = txtName.getText();
                String salary = txtSalary.getText();
                String mobile = txtMobile.getText();
                String empid = txtid.getText();

                try {
                    pst=con.prepareStatement("update employee set empname=? ,salary=?,mobile=?  where id=?");

                    pst.setString(1,empname);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.setString(4,empid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Employee updated successfully");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        //Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid = txtid.getText();

                try {
                    pst=con.prepareStatement("delete from employee where id=?");

                    pst.setString(1,empid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Employee updated successfully");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
