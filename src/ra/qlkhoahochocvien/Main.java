package ra.qlkhoahochocvien;

import ra.qlkhoahochocvien.presentation.AdminView;
import ra.qlkhoahochocvien.presentation.StudentView;
import ra.qlkhoahochocvien.utils.Helper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("┌──────────     HỆ THỐNG QUẢN LÝ ĐÀO TẠO     ──────────┐");
            System.out.println("│ 1. Đăng nhập với tư cách Quản trị viên               │");
            System.out.println("│ 2. Đăng nhập với tư cách Học viên                    │");
            System.out.println("│ 3. Thoát                                             │");
            System.out.println("└──────────────────────────────────────────────────────┘");
            int choice = Helper.getIntInput(scanner, "Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    AdminView.showMenuLogin(scanner);
                    break;
                case 2:
                    StudentView.showMenuLogin(scanner);
                    break;
                case 3:
                    System.out.println("Đang thoát chương trình!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn sai, vui lòng chọn lại: ");
            }
        }
    }
}
