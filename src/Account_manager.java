import java.sql.*;
import java.util.*;
public class Account_manager {
    private Connection conn;
    private Scanner sc;
    public Account_manager(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void credit_money(long acc_num){
        sc.nextLine();
        System.out.print("Enter amount to credit: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String pin = sc.nextLine();

        try{
            conn.setAutoCommit(false);
            if(acc_num !=0){
                String query = "SELECT * FROM accounts WHERE acc_num=? AND pin = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setLong(1, acc_num);
                prest.setString(2, pin);
                ResultSet rs = prest.executeQuery();

                if(rs.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_num=?";
                    PreparedStatement pst = conn.prepareStatement(credit_query);
                    pst.setDouble(1, amount);
                    pst.setLong(2, acc_num);
                    int rowsAffected = pst.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Rs."+amount+" has been credited Successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                    }else{
                        System.out.println("Transaction failed!!");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Credentials");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deposit_money(long acc_num){
        sc.nextLine();
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String pin = sc.nextLine();

        try{
            conn.setAutoCommit(false);
            if(acc_num !=0){
                String query = "SELECT * FROM accounts WHERE acc_num=? AND pin = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setLong(1, acc_num);
                prest.setString(2, pin);
                ResultSet rs = prest.executeQuery();
                if(rs.next()){
                    double curr_bal = rs.getDouble("balance");
                    if(amount <= curr_bal){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_num = ? ";
                        PreparedStatement prestmt = conn.prepareStatement(debit_query);
                        prestmt.setDouble(1, amount);
                        prestmt.setLong(2, acc_num);
                        int  rowsAffected = prestmt.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs."+amount+" has been debited Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction failed!!");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance!!");
                    }
                }
                else{
                    System.out.println("Invalid Credentials");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void transfer_money(long sender_acc_num){
        sc.nextLine();
        System.out.print("Enter receiver's account number: ");
        long receiver_acc_num = sc.nextLong();
        System.out.print("Enter amount to transfer: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String pin = sc.nextLine();
        try{
            conn.setAutoCommit(false);
            if(sender_acc_num !=0 && receiver_acc_num !=0){
                String query = "SELECT * FROM accounts WHERE acc_num=? AND pin = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setLong(1, sender_acc_num);
                prest.setString(2, pin);
                ResultSet rs = prest.executeQuery();
                if(rs.next()){
                    double curr_bal = rs.getDouble("balance");
                    if(amount<= curr_bal){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_num=?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_num=?";
                        PreparedStatement pst = conn.prepareStatement(debit_query);
                        PreparedStatement prestmt = conn.prepareStatement(credit_query);
                        pst.setDouble(1, amount);
                        pst.setLong(2, sender_acc_num);
                        prestmt.setDouble(1, amount);
                        prestmt.setLong(2, receiver_acc_num);
                        int rowsAffected1 = pst.executeUpdate();
                        int rowsAffected2 = prestmt.executeUpdate();
                        if(rowsAffected1>0 && rowsAffected2>0){
                            System.out.println("Transfer successful!!");
                            System.out.println("Rs."+amount+" has been transferred Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction failed!!");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!!");
                    }
                }
                else{
                    System.out.println("Invalid Credentials");
                }
            }else{
                System.out.println("Invalid Credentials");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void get_balance(long acc_num){
        sc.nextLine();
        System.out.print("Enter a security PIN: ");
        String pin = sc.nextLine();
        try{
            String query = "SELECT balance FROM accounts WHERE acc_num = ? AND pin = ?";
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setLong(1,acc_num);
            prst.setString(2,pin);
            ResultSet rs = prst.executeQuery();
            if(rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Your Current Balance is "+balance);
            }
            else{
                System.out.println("Invalid Credentials");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
