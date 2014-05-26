/**
 * 
 */
package se.umu.cs.pvt151.model;

import java.util.ArrayList;

/**
 * @author Anders
 *
 */
public class ConvertLogic {

	public boolean verifyWindowSize(int test) {


		return test > 0;		
	}

	public boolean verifySmoothType(int test) {

		return (test == 0 || test == 1);
	}

	public boolean verifyMinStep(int test) {

		return test >= 0;
	}

	public boolean verifyPrintMean(int test) {

		return (test == 0 || test == 1);
	}

	public boolean verifyPrintZero(int test) {

		return (test == 0 || test == 1);
	}

	public void checkParameters(ArrayList<String> processParameters) {
		int result = 1;
		String tempString;
		String[] strings;
		
		for (int i = 0; i < processParameters.size(); i++) {
			switch (i) {
			case 0:
				if (processParameters.get(i).isEmpty()) {
					result = 2;
				}
				break;
			case 1:
				tempString = processParameters.get(i);
				strings = tempString.split(" ");
				
				
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;

			default:
				break;
			}
		}

	}



}
