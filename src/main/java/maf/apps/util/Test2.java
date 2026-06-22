package maf.apps.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test2 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		try {
			int x = 10 / 0;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.fillInStackTrace());
		}
	}

}
