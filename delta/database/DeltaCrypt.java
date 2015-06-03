/////////////////////////
// File:  DeltaCrypt.java
// Class: DeltaCrypt
//
// Description:
// 	DeltaCrypt hashes a username and password combination
// 	using the SHA1-algorithm.
//
// Public methods:
//	public static String hash(String username, String password)
//		generates a hash code based on the username and password.
//
// Usage:
//	Adding a user
// 	|	DUser du = new DUser();
//	|	du.readfile("User.csv");
//	|	
//	|	String pw_hash = DeltaCrypt.hash("Steve", "password");
//	|	du.addAdmin("Steve", pw_hash);
//	|	du.writeFile("User.csv");
//	Authenticating a user
// 	|	DUser du = new DUser();
//	|	du.readfile("User.csv");
//	|
//	|	// Assume that user and pw are defined already	
//	|	String pw_hash = DeltaCrypt.hash(user, pw);
//	|	if      (du.checkAdmin(user, pw_hash)) System.out.println("ADMIN privileges");
//	|	else if (du.checkSuper(user, pw_hash)) System.out.println("SUPERUSER privileges");
//	|	else                                   System.out.println("Authentication failed");
//

package delta.database;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeltaCrypt {

	///////////////////////////////////////////////////////////
	public static String hash(String username, String password) {
	// hashes the password using the SHA-1 algorithm.
	// The username is used as a salt.

		// Reference: http://www.mkyong.com/java/java-sha-hashing-example/

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(username.concat(password).getBytes());
			byte byteData[] = md.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : byteData) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			System.err.println("Error hashing password");
			return "ERROR_HASHING_PASSWORD";
		}

	}

}
