package rmiClient;

public class Main 
{
	public static void main(String[] args)
	{
		 

		      System.setProperty("java.security.policy", "file:allowall.policy");

		        MathService service = null;
		        try {
		            service = (MathService) Naming.lookup("//localhost/CalculatorService");

		            System.out.println  ("Number of Clients : " + service.addClient());
		            System.out.println  ("Add : " + service.add(2,2));
		            System.out.println  ("Subtract : " + service.subtract(5,2));
		            System.out.println  ("Multiply : " + service.multiply(2,6));

		            System.out.println  ("Divide : " +  service.divide(4,2));
		 
		        } catch (NotBoundException ex) {
		            System.err.println(ex.getMessage());
		        } catch (MalformedURLException ex) {
		            System.err.println(ex.getMessage());
		        } catch (RemoteException ex) {
		            System.err.println(ex.getMessage());
		        }
		    }

}
