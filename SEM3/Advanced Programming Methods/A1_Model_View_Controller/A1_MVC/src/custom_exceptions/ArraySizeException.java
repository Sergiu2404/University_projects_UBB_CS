package custom_exceptions;

public class ArraySizeException extends Exception{
    private String message;

    public ArraySizeException(){}

    public ArraySizeException(String m){
        super(m);
    }

//    @Override
//    public String getMessage(){return this.message;}
}
