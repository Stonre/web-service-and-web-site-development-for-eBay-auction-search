import java.io.*;
import java.lang.*;
import java.security.*;
import java.math.BigInteger;  

public class ComputeSHA {
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		// TODO Auto-generated method stub
		FileReader in=null;
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		String str="";
		String tmp="";
		try{
			in=new FileReader(args[0]);
			BufferedReader reader=new BufferedReader(in);
			while((tmp=reader.readLine())!=null) str=str+tmp+'\n';
		}finally{
			if(in!=null){
				in.close();
			}
		}
		md.update(str.getBytes());
		byte[] output = md.digest();
		System.out.println(new BigInteger(1,output).toString(16));
	}
}
