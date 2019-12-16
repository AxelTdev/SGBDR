package main;

import java.io.IOException;
import java.util.Scanner;

import gestionCouche.DBManager;

public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Scanner sc = new Scanner(System.in);
		//DBManager dbm = new DBManager();

		DBManager.init();
		String str = "";

		

		do {
			System.out.println("Quel est votre choix?");
			str = sc.nextLine();
			
			DBManager.processCommand(str);

		} while (!str.equals("exit"));
		DBManager.finish();

		sc.close();
	}

}
