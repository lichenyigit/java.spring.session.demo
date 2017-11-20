package session.demo.bean;

public class Result<T> {

    protected int errorCode;
    protected String descrpition;
    protected T result;

    public Result(){
        this.errorCode = 0;
        this.descrpition = "success";
    }

    public Result(int errorCode, String descrpition){
        this.errorCode = errorCode;
        this.descrpition = descrpition;
    }

    public static Result build(){
        return new Result();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Result setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getDescrpition() {
        return descrpition;
    }

    public Result setDescrpition(String descrpition) {
        this.descrpition = descrpition;
        return this;
    }

    public T getResult() {
        return result;
    }

    public Result setResult(T result) {
        this.result = result;
        return this;
    }
}
