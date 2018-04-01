package be.tabtabstudio.veganapp.api;

class ApiResponse<ResultT> {
    public String status;
    public String message;
    public ResultT result;
}
