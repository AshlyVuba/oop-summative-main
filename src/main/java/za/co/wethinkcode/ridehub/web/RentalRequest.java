package za.co.wethinkcode.ridehub.web;

/** The JSON body a client sends to POST /api/rentals. */
public class RentalRequest {

    public String riderName;
    public String serialNo;

    public RentalRequest() {
    }
}
