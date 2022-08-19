package Togefit.server.response;

public class OperationResponse {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public OperationResponse(){

    }

    public OperationResponse(String result){
        this.result = result;
    }
}
