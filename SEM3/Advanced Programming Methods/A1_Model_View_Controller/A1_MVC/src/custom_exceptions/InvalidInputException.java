package custom_exceptions;

public class InvalidInputException extends Exception{
    private String message;

    public InvalidInputException(){}

    public InvalidInputException(String m){
        super(m);
    }
}
