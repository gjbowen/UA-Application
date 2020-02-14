public class Driver {
	public static void main(String[] args){
		if(args.length>0)
			new public_package.Login(args[0]); //shortcut mode
		else
			new public_package.Login(null);
	}
}