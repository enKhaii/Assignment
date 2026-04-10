/* abstract prevent "ghost" users or "ghost" objects that does not belong to any
   entities in the system, it forces every entities(subclass) to override and implement methods
   IN SHORT, acts as a template for all users (Sender, Driver, etc)
   You cannot create a "Person" directly, you must create a SPECIFIC TYPE */

/* GROUP NOTE: 
  1. If you add a new entity (like 'Manager'), make it "extends Person"
  2. You MUST use @Override for displayInfo() or the code won't compile
  3. Use setName() in your constructor to trigger the blank-name check
*/

public abstract class Person{  
    private String personID, loginID, name, password, email, phoneNum;

    // Parameterized Constructor - OBJECT that needs login (ADMIN, COURIER)
    public Person(String personID, String loginID, String name, String password, String email, String phoneNum){
        this.personID = personID;
        this.loginID = loginID;
        setName(name);
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // METHOD OVERLOADING
    // OBJECT that doesn't needs loginID (SENDER)
    public Person(String personID, String name, String password, String email, String phoneNum){
        this.personID = personID;
        this.loginID = "N/A";   // N/A for display purpose
        setName(name);
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // Getter & Setter for "personID"
    public String getPersonID(){
        return personID;
    }

    public void setPersonID(String personID){
        this.personID = personID;
    }

    // Getter & Setter for "loginID"
    public String getLoginID(){
        return loginID;
    }

    public void setLoginID(String loginID){
        this.loginID = loginID;
    }

    // Getter & Setter for "name"
    public String getName(){
        return name;
    }

    public void setName(String name){  // if name no value(null), or just white spaces
        if (name == null || name.trim().isEmpty()){
            System.out.println("ERROR! There is no name for ID: " + this.personID);
            this.name = "MISSING_NAME"; // placeholder for empty name, so it won't be "null"
        }
        else{
            this.name = name;
        }
    }

    // Getter & Setter for "password"
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // Getter & Setter for "email"
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    // Getter & Setter for "email"
    public String getPhoneNum(){
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    /*
        Every class that extends PERSON must implement this method
        This ensure no matter what kind of person we add later,
        they will always have a way to print their own unique details
        "Every entities will need to display info"
    */
    public abstract void displayInfo();
}