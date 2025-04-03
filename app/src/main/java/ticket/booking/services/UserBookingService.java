package ticket.booking.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String USERS_PATH = "app/src/main/java/ticket/booking/localDB/users.json";  // "../localDB/users.json";
    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }
    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public void loadUserListFromFile() throws IOException {
        File allUsers = new File(USERS_PATH);
        userList = objectMapper.readValue(allUsers, new TypeReference<List<User>>() {}); // json -> Object(User) // Deserialize
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // camel case to snake case

        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList); // Object(User) -> json  // Serialize
    }

    public void fetchBookings(){
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if(ticketId == null || ticketId.isEmpty()){
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId = ticketId; // because strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId));

        if(removed){
            System.out.println("Ticket with ID "+ ticketId + " has been canceled.");
            return Boolean.TRUE;
        } else {
            System.out.println("No ticket found with ID "+ ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat){
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> rowSeats = train.getSeats();

            if(row >= 0 && row < rowSeats.size() && seat >= 0 && seat < rowSeats.get(row).size()){
                if(rowSeats.get(row).get(seat) == 0){
                    rowSeats.get(row).set(seat, 1);  // make 0 -> 1
                    train.setSeats(rowSeats);
                    trainService.addTrain(train);
                    return true;  // Booking successful
                } else {
                    return false; // seat is already Booked
                }
            } else {
                return false; // Invalid row or seat number
            }
        } catch (IOException ex){
            return Boolean.FALSE;
        }
    }




}
