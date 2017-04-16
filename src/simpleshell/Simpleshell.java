package simpleshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.naming.InitialContext;

public class Simpleshell {

	public static void main(String[] args) throws java.io.IOException{
		// TODO Auto-generated method stub
		String commandline;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		
		String currentFile = "/Users";
		
		ArrayList<String> historyCommand = new ArrayList<>();
		// We break out with <control><C>
		while (true) {
			// read what the user entered
			System.out.print("jsh> ");
			commandline = console.readLine();
			
			if(commandline.equals("history")){
				for (int i = 0; i < historyCommand.size(); i++) {
					System.out.printf("%d %s\n", i, historyCommand.get(i));
				}
				continue;
			}
			

			if(commandline.equals("!!")){
				commandline = historyCommand.get(historyCommand.size()-1);
			}
			
			if (commandline.startsWith("!")) {
				int number = Integer.parseInt(commandline.substring(1));
				commandline = historyCommand.get(number);
			}
			
			historyCommand.add(commandline);
			
			//if the user entered a return, just loop again
			if (commandline.equals("")) 
				continue;		
	
			ArrayList<String> command = new ArrayList<>();
			
			for (String string : commandline.split(" ")) {
				command.add(string);
			}
			

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(false);			
			pb.directory(new File(currentFile));
			String currentFile_backup = currentFile;
			if (command.get(0).equals("cd")) {
				String dir = command.get(1);
				if (dir.startsWith("/")  && dir.endsWith("/")) {
					currentFile_backup = currentFile_backup + dir.substring(0, dir.length()-1);
				}else if (dir.startsWith("/")){
					currentFile_backup = currentFile_backup + dir;
				}else if (dir.endsWith("/")){
					currentFile_backup = currentFile_backup + "/" + dir.substring(0, dir.length()-1);
				}else {
					currentFile_backup = currentFile_backup + "/" + dir;
				}
				try {
					pb.directory(new File(currentFile_backup));
					currentFile = currentFile_backup;
					command = null;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			Process process = pb.start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] bytes = new byte[process.getInputStream().available()];
			process.getInputStream().read(bytes);
			System.out.print(new String(bytes));
			
			
			
			
			
			/** The steps are :
			 *  (1)parse the input to obtain the command and any parsementers
			 *  (2)create a ProcessBuilder object
			 *  (3)start the process
			 *  (4)obtain the output stream
			 *  (5)out put the contents returned by the command
			 */
		}
	}

}
