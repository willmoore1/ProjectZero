package com.revature.util;

import java.util.Scanner;

public class GetInput { // make singleton scanner class to avoid closure issues
	
	private GetInput() {}
	
	
	private static Scanner scan = null;
	public static Scanner getScan() {
		
		if(scan == null) {
			scan = new Scanner(System.in);
			return scan;
		} else {
			return(scan);
		}
		
	}
	public static void closeScan() {
		scan.close();
	}
	
}
