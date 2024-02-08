package custom_exceptions;

public class InvalidType extends Exception{
    private String message;

    public InvalidType(){}
    public InvalidType(String message){
        super(message);
    }

//    @Override
//    public String getMessage()
//    {
//        return this.message;
//    }
}

