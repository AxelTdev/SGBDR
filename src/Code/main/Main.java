package Code.main;

import java.util.Scanner;

import Code.dbManager.DBManager;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		DBManager dbm = new DBManager();

		dbm.Init();
		String str = "";

		System.out.println("Quel est votre choix?");

		do {
			System.out.println("Quel est votre choix?");
			str = sc.nextLine();
			System.out.println(str);
			dbm.ProcessCommand(str);

		} while (!str.equals("exit"));
		dbm.Finish();

		sc.close();
	}

}
