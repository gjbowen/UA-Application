
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Driver {

	public static void main(String[] args){
		String mode;
		if(args.length>0)
			mode = args[0];
		else
			mode = null;

		new public_package.Login("concur");
	}
}