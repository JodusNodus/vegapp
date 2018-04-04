package be.tabtabstudio.veganapp.data.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<ResultT> {
    public String status;
    public String message;
    public ResultT result;
}
