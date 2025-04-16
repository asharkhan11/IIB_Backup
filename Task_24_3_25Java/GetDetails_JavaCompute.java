import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;

public class GetDetails_JavaCompute extends MbJavaComputeNode {
	
	
	
	public String sendToSocket(String ip, String port, String data) throws Exception{
		
		 // Connect to the Python server
        Socket socket = new Socket("localhost", 8765);
        
        

        // Send a request
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(data);

        // Receive response
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = reader.readLine();

        // Close the socket
        socket.close();

		
		
		return response;
	}
	
	
    public void evaluate(MbMessageAssembly inAssembly) throws MbException {
        MbOutputTerminal out = getOutputTerminal("out");

        try {
        	
        	String ip = "";
        	String port = "";
        	String data = "0156                    **  000026    003000100000006163060460000000000     0         00000000        00000000000000000000000000000000000083069841233   5000000A,";
        	
        	MbMessage inMessage = new MbMessage(inAssembly.getMessage());
        	
        	Log4jLogger.logMessage("INFO", "success from jcn");
        	
        	
        	String response = sendToSocket(ip,port,data);
        	
        	
        	Pattern pattern = Pattern.compile("[A-Za-z.]+(?:\\s[A-Za-z]+)*");
            Matcher matcher = pattern.matcher(response);

            String name ="";
            
            // Finding and printing the matched name
            while (matcher.find()) {
                name = name.concat(matcher.group().trim()) ;
            }
        	
           
            // Create a new message with the response
            MbMessage outMessage = new MbMessage(inAssembly.getMessage());
            MbElement rootElement = outMessage.getRootElement();
            MbElement body = rootElement.getLastChild().getLastChild();
            body.setValue(name);

            // Propagate message
            MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly, outMessage);
            out.propagate(outAssembly);

        } catch (Exception e) {
            throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
        }
    }
}
