package Code.util;

import java.io.IOException;

import Code.diskManager.DiskManager;

public class Test {
	public static void main(String [] args) {
		DiskManager i = new DiskManager();
		i.CreateFile(2);
		try {
			byte [] buff = new byte[Constants.pageSize];
			i.WritePage(i.AddPage(2), buff);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
