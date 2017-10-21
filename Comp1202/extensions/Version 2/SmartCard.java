
public class SmartCard {
    
    String name;
    Boolean isStaff;

    public SmartCard (String personName) {
        name = personName;
        isStaff = false;
    }
    
    public String getOwner () {
        return name;
    }
    
    public Boolean isStaff () {
        return isStaff;
    }
    
    public void setStaff (Boolean set) {
        isStaff = set;
    }
}