package custom_exceptions;

public class InvalidObject extends Exception{
    private String message;

    public InvalidObject(){}
    public InvalidObject(String message){
        super(message);
    }

//    @Override
//    public String getMessage()
//    {
//        return this.message;
//    }
}
