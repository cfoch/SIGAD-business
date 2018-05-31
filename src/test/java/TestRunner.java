
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cfoch
 */
public class TestRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Result result;

        result = JUnitCore.runClasses(DefaultTestSuite.class);
        result.getFailures().forEach((failure) -> {
            System.out.println(failure.toString());
        });
        System.out.println(result.wasSuccessful());
    }

}
