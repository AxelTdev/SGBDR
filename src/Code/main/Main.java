package Code.main;

import java.util.Scanner;

import Code.dbManager.DBManager;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		//DBManager dbm = new DBManager();

		DBManager.Init();
		String str = "";

		System.out.println("Quel est votre choix?");

		do {
			System.out.println("Quel est votre choix?");
			str = sc.nextLine();
			System.out.println(str);
			DBManager.ProcessCommand(str);

		} while (!str.equals("exit"));
		DBManager.Finish();

		sc.close();
	}

}
