import java.sql.*;
import java.util.*;
public class Accounts {
    private Connection conn;
    private Scanner sc;
    public Accounts(Connection conn, Scanner sc){
        this.conn = conn;
        this.sc = sc;
    }

    public long open_account(String email){
        if(!acc_exists(email)){
            String query = "INSERT INTO accounts(acc_num,full_name,email,balance,pin) VALUES (?,?,?,?,?)";
            sc.nextLine();
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter Initial Balance: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter PIN: ");
            String pin = sc.nextLine();
            try{
                long acc_num = generateAccount_num();
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setLong(1,acc_num);
                pstmt.setString(2,name);
                pstmt.setString(3,email);
                pstmt.setDouble(4,balance);
                pstmt.setString(5,pin);
                int rowsAffected = pstmt.executeUpdate();
                if(rowsAffected>0){
                    return acc_num;
                }
                else{
                    System.out.println("Account Creation Failed");
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }
        throw  new IllegalArgumentException("Account already exists");
    }

    public long getAccount_num(String email){
        String query = "select acc_num from accounts where email = ?";
        try{
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setString(1,email);
            ResultSet rs = prst.executeQuery();
            if(rs.next()){
                return rs.getLong("acc_num");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account number not found");
    }

    private long generateAccount_num(){
        try{
            Statement stmt = conn.createStatement();
            String query = "select acc_num from accounts ORDER BY acc_num DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                long lastAcc_num = rs.getLong("acc_num");
                return lastAcc_num+1;
            }
            else{
                return 10000100;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 10000100;
    }

    public boolean acc_exists(String email){
        String query = "select acc_num from accounts where email = ?";
        try{
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setString(1, email);
            ResultSet rs = prst.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
