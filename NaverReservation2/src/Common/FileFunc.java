package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileFunc {
	public List fileRead(String fPath) {
		//"/home/mjs/test/text.txt"
		List<String> reserveList = new ArrayList();
		
		Path path = Paths.get(fPath);
		try (BufferedReader reader = Files.newBufferedReader(path)) {
		    String line;
		    while ((line = reader.readLine()) != null) {
		        //System.out.println(line);
		        reserveList.add(line);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return reserveList;
	}
}
