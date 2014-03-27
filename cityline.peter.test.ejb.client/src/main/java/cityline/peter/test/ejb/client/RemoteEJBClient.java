package cityline.peter.test.ejb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import cityline.peter.ejb.CalculatorBean;
import cityline.peter.ejb.CounterBean;
import cityline.peter.ejb.RemoteCalculator;
import cityline.peter.ejb.RemoteCounter;

public class RemoteEJBClient {
	public static void testEJB() throws Exception{
        // Invoke a stateless bean
        invokeStatelessBean();
        
        // Invoke a stateful bean
        invokeStatefulBean();
    }
 
    /**
     * Looks up a stateless bean and invokes on it
     *
     * @throws NamingException
     */
    private static void invokeStatelessBean() throws NamingException {
        // Let's lookup the remote stateless calculator
        final RemoteCalculator statelessRemoteCalculator = lookupRemoteStatelessCalculator();
        System.out.println("Obtained a remote stateless calculator for invocation");
        
        
        // invoke on the remote calculator
        int a = 204;
        int b = 340;
        System.out.println("Adding " + a + " and " + b + " via the remote stateless calculator deployed on the server");
        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator returned sum = " + sum);
        if (sum != a + b) {
            throw new RuntimeException("Remote stateless calculator returned an incorrect sum " + sum + " ,expected sum was " + (a + b));
        }
        // try one more invocation, this time for subtraction
        int num1 = 3434;
        int num2 = 2332;
        System.out.println("Subtracting " + num2 + " from " + num1 + " via the remote stateless calculator deployed on the server");
        int difference = statelessRemoteCalculator.subtract(num1, num2);
        System.out.println("Remote calculator returned difference = " + difference);
        if (difference != num1 - num2) {
            throw new RuntimeException("Remote stateless calculator returned an incorrect difference " + difference + " ,expected difference was " + (num1 - num2));
        }
        
        
    }

 
    /**
     * Looks up and returns the proxy to remote stateless calculator bean
     *
     * @return
     * @throws NamingException
     */
    private static RemoteCalculator lookupRemoteStatelessCalculator() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        // The app name is the application name of the deployed EJBs. This is typically the ear name
        // without the .ear suffix. However, the application name could be overridden in the application.xml of the
        // EJB deployment on the server.
        // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
        final String appName = "";
        // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
        // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
        // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
        // jboss-as-ejb-remote-app
        final String moduleName = "cityline.peter.ejb-0.0.1-SNAPSHOT";
        // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
        // our EJB deployment, so this is an empty string
        final String distinctName = "";
        // The EJB name which by default is the simple class name of the bean implementation class
        final String beanName = CalculatorBean.class.getSimpleName();
        // the remote view fully qualified class name
        final String viewClassName = RemoteCalculator.class.getName();
        // let's do the lookup
        return (RemoteCalculator) context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
    }
    
    private static void invokeStatefulBean() throws NamingException {
        // Let's lookup the remote stateful counter
        final RemoteCounter statefulRemoteCounter = lookupRemoteStatefulCounter();
        System.out.println("Obtained a remote stateful counter for invocation");
        // invoke on the remote counter bean
        final int NUM_TIMES = 20;
        System.out.println("Counter will now be incremented " + NUM_TIMES + " times");
        for (int i = 0; i < NUM_TIMES; i++) {
            System.out.println("Incrementing counter");
            statefulRemoteCounter.increment();
            System.out.println("Count after increment is " + statefulRemoteCounter.getCount());
        }
        
        pressAKey();
        
        // now decrementing
        System.out.println("Counter will now be decremented " + NUM_TIMES + " times");
        for (int i = NUM_TIMES; i > 0; i--) {
            System.out.println("Decrementing counter");
            statefulRemoteCounter.decrement();
            System.out.println("Count after decrement is " + statefulRemoteCounter.getCount());
        }
    }
    
    private static RemoteCounter lookupRemoteStatefulCounter() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        // The app name is the application name of the deployed EJBs. This is typically the ear name
        // without the .ear suffix. However, the application name could be overridden in the application.xml of the
        // EJB deployment on the server.
        // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
        final String appName = "";
        // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
        // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
        // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
        // jboss-as-ejb-remote-app
        final String moduleName = "cityline.peter.ejb-0.0.1-SNAPSHOT";
        // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
        // our EJB deployment, so this is an empty string
        final String distinctName = "";
        // The EJB name which by default is the simple class name of the bean implementation class
        final String beanName = CounterBean.class.getSimpleName();
        // the remote view fully qualified class name
        final String viewClassName = RemoteCounter.class.getName();
        // let's do the lookup (notice the ?stateful string as the last part of the jndi name for stateful bean lookup)
        return (RemoteCounter) context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName + "?stateful");
    }
    
    // Compatible with Eclipse Environment
    public static void pressAKey() {
        InputStreamReader istream=null;
        BufferedReader bufRead=null;

        istream = new InputStreamReader(System.in) ;
        bufRead = new BufferedReader(istream) ;
        String returnval = null;
        try {
            returnval =  bufRead.readLine();
            bufRead.close();
            istream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
