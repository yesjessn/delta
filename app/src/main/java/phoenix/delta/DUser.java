package phoenix.delta;

////////////////////
// File:  DUser.java
// Class: DUser
//
// Description:
//	DUser keeps a list of usernames and passwords.
//	The passwords are kept as simple, unencrypted strings,
//	meaning that gaining unauthorized access is relatively easy.
//
//	DUser also records the privilege of each user.
//	There are only two privileges: SUPERUSER and ADMIN
//	SUPERUSER privileges grant read and write permissions to any file.
//	ADMIN privileges grant write permissions to any file.
//	A user who is not logged in can only write to DUser (for registeration).
//
//	The csv files created by DUser have the following format:
//		1: Username
//		2: Password
//		3: Privilege
//
// Public methods:
//	void addAdmin(String username, String password)
//		adds a user with ADMIN privileges.
//	void addSuper(String username, String password)
//		adds a user with SUPERUSER privileges.
//	boolean checkAdmin(String username, String password)
//		checks whether a user has ADMIN privileges.
//		It returns true on success.
//	boolean checkSuper(String username, String password)
//		checks whether a user has SUPERUSER privileges.
//		It returns true on success.
//
// Usage:
//	Adding a user to an existing file
//	|	DUser du = new DUser();
//	|	du.readFile("User.csv");  // error checking omitted
//	|	du.addAdmin("Tiffany", "catmonster");
//	|	du.writeFile("User.csv");
//	Authenticating a user
//	|	DUser du = new DUser();
//	|	du.readFile("User.csv");
//	|
//	|	// assume that String user and pw are defined already
//	|	if      (checkAdmin(user, pw)) System.out.println("ADMIN privileges");
//	|	else if (checkSuper(user, pw)) System.out.println("SUPERUSER privileges");
//	|	else                           System.out.println("Authentication failed");
//

import java.util.ArrayList;

public class DUser {

    static final int ADMINISTRATOR = 1;
    static final int TEACHER = 2;

    private String username, password;
    private int type;
    private int id;

    public DUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public DUser(String username, String password, int type) {
        this.username = username;

        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean checkPassword (String pw) {return (pw.compareTo(password) == 0);}

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString () {return username +"," + password + ","+type;}

    /*

    public boolean LoginValidation(String username, String password, int type) {
        return(this.username == username && this.password == password && this.type == type);
    }


    //////////////////////////////////////////////////////
    public void addAdmin(String username, String password) {
        // adds a tuple of the form (username, password, "ADMIN").
        String[] entry = {username, password, "ADMIN"};
        //this.addEntry(entry);
    } // public void addAdmin(String username, String password)

    //////////////////////////////////////////////////////
    public void addSuper(String username, String password) {
        // adds a tuple of the form (username, password, "SUPERUSER").
        String[] entry = {username, password, "SUPERUSER"};
        //this.addEntry(entry);
    } // public void addSuper(String username, String password)

    ///////////////////////////////////////////////////////////
    public boolean checkAdmin(String username, String password) {
        // checks whether a login exists as an admin.
        String[] entry = {username, password, "ADMIN"};
        //return this.findEntry(entry);
    } // public boolean checkAdmin(String username, String password)

    ///////////////////////////////////////////////////////////
    public boolean checkSuper(String username, String password) {
        // checks whether login exists as a password.
        String[] entry = {username, password, "SUPERUSER"};
        //return this.findEntry(entry);
    } // public boolean checkSuper(String username, String password)
    */

} // public class DUser extends DFile