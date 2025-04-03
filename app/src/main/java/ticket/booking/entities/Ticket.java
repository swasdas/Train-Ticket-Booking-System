package ticket.booking.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;

    public Ticket(){};

    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train){
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }
    // getter functions
    public String getTicketInfo(){
        return String.format("Ticket ID: %s belongs to user %s from %s to %s on %s", ticketId, userId, source, destination, dateOfTravel);
    }
    public String getTicketId(){
        return ticketId;
    }
    public String getSource(){
        return source;
    }
    public String getUserId(){
        return userId;
    }
    public String getDestination(){
        return destination;
    }
    public String getDateOfTravel(){
        return dateOfTravel;
    }
    public Train getTrain(){
        return train;
    }

    // setter functions
    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }
    public void setSource(String source){
        this.source = source;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public void setDateOfTravel(String dateOfTravel){
        this.dateOfTravel = dateOfTravel;
    }
    public void setTrain(Train train){
        this.train = train;
    }
}
