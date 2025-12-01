import java.sql.*;
import java.util.*;
public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/banking_management_system";
    private static final String user = "root";
    private static final String password = "Prajwal@0103";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection conn = DriverManager.getConnection(url,user,password);
            Scanner sc = new Scanner(System.in);
            User user = new User(conn,sc);
            Accounts acc = new Accounts(conn, sc);
            Account_manager acc_manager = new Account_manager(conn, sc);

            String email;
            long acc_num;

            System.out.println("**** WELCOME TO BANKING SYSTEM ****");
            while(true){
                System.out.println("1. Create Account");
                System.out.println("2. Login to Account");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                switch(ch){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println("User Logged In Successfully");
                            if(!acc.acc_exists(email)){
                                System.out.println("1. Account does not exist Please Open a bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1){
                                    acc_num = acc.open_account(email);
                                    System.out.println("Account Opened Successfully");
                                    System.out.println("Your Account Number is "+acc_num);
                                }else{
                                    break;
                                }
                            }

                            acc_num = acc.getAccount_num(email);
                            int choice = 0;
                            while(choice != 5){
                                System.out.println("1. Deposit/Credit Money");
                                System.out.println("2. Withdraw/Debit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");

                                System.out.println("Enter your choice: ");
                                choice = sc.nextInt();
                                switch (choice){
                                    case 1:
                                        acc_manager.credit_money(acc_num);
                                        break;
                                    case 2:
                                        acc_manager.deposit_money(acc_num);
                                        break;
                                    case 3:
                                        acc_manager.transfer_money(acc_num);
                                        break;
                                    case 4:
                                        acc_manager.get_balance(acc_num);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid Choice");
                                        break;
                                }
                            }
                        }else{
                            System.out.println("Incorrect Email or Password");
                        }
                    case 3:
                        System.out.println("*******Thank you for using BANKING SYSTEM***********");
                        break;
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}