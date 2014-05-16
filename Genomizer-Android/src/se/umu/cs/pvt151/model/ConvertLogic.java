/**
 * 
 */
package se.umu.cs.pvt151.model;

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
	
	

}
